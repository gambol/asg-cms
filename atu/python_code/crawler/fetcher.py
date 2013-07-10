#!/usr/bin/env python
#coding=utf-8

__created__ = "2009/09/27"
__author__ = "xlty.0512@gmail.com"
__author__ = "牧唐 杭州"

import urllib, urllib2, urlparse, cookielib
import re, time, sys, os
import gzip
from StringIO import StringIO
import mimetypes
import logging
import copy

class FetcherUtil():
    @staticmethod
    def _fix_url(u):
        """
                        格式化相对地址为绝对地址
        """
        domain = path = None
        s, h, path, _, _ = urlparse.urlsplit(u)
        domain = s + "://" + h
        def _(locate):
            if not locate.lower().startswith("http"):
                if locate.startswith("/"):
                    #相对web root的地址
                    return domain + locate
                else: 
                    if u.endswith("/"):
                        #domain以/结尾, 有2种形式: 
                        #simple url: http://www.sample.com/
                        #rest url: http://www.sample.com/people/1/
                        return u + locate
                    else:
                        #domain不以/结尾, 有2种形式: 
                        #simple url: http://www.sample.com
                        #rest url: http://www.sample.com/people/1.htm
                        #取parent dir
                        r = -1
                        if path: r = path.rfind("/")
                        return domain + (path and r >= 0 and path[:r + 1] or "") + locate
            return locate
        return _

    @staticmethod
    def get_content(res):
        """
        gzip 解码
        """
        ce = res.headers.get("Content-Encoding")
        if ce and 'zip' in ce:
            return gzip.GzipFile(fileobj=StringIO(res.read())).read() 
        return res.read()
    
    @staticmethod
    def decode(content, to="utf-8", default=sys.getdefaultencoding()):
        """
                                要安装: http://chardet.feedparser.org/
                                安装命令: easy_install chardet
        """
        enc, confidence = FetcherUtil.charset(content)
        
        if enc == to.lower():
            return content

        if confidence > 0.5:
            return content.decode(enc).encode(to)
        else:
            #没法检测时, 指定系统默认
            return content.decode(default).encode(to)

    @staticmethod
    def charset(content):
        """
                                要安装: http://chardet.feedparser.org/
                                安装命令: easy_install chardet
        """
        import chardet
        l = len(content)
        half = l / 2
        limit = 500 #这是经验数字, 要检测的字符为limit*2, 效果就不错
        pre = half > limit and limit or half
        cs = chardet.detect(content[:pre] + content[-pre:])
        return cs["encoding"] and cs["encoding"].lower(), cs["encoding"] and cs["confidence"]

class FetchedData():
    def __init__(self, url, code, msg, headers, content):
        self.url = url
        self.code = code
        self.msg = msg
        self.content = content
        self.headers = headers

    def __str__(self):
        return "url: %s, code: %s, content-len: %s" % (self.url, self.code, self.content and len(self.content) or 0) 

    def ok(self):
        return self.code == 200

    @staticmethod
    def create(response):
        c = None
        if response.code == 200:
            c = FetcherUtil.get_content(response)
        return FetchedData(response.url, response.code, response.msg, response.headers, c)

class LazyIO(object):
    """
    通过mock一个read方法, 达到延迟读数据的目的, 因为httpconnection判断了如果有read方法就用8192block去读的
    """
    def __init__(self, buffers=[]):
        """
            support file, StringIO
        """
        self.buffer = StringIO()
        self.buffs = []
        self.pos = 0
        self.len = 0
        for buff in buffers:
            if issubclass(type(buff), basestring):
                self.buffer.write(buff)
                self.len += len(buff)
            elif self.is_support(buff):
                _len = self.get_buff_len(buff)
                self.buffs.append((self.len, _len, buff))
                self.len += _len
        self.buffer.seek(0)
    def is_support(self, buff):
        return (type(buff) is file) or (hasattr(buff, "len") and hasattr(buff, "read"))

    def get_buff_len(self, buff):
        if type(buff) is file:
            return os.stat(buff.name).st_size
        elif hasattr(buff, "len") and hasattr(buff, "read"):
            return buff.len

    def find_from_lazy_buff(self, size_to_read):
        size_to_read = self.size > size_to_read and size_to_read or self.size
        end = self.pos + size_to_read
        logging.debug("=================size_to_read %s end %s" % (size_to_read, end))
        read = ""
        expected = size_to_read

        if end < self.buffs[0][0]:
            read = self.buffer.read(expected)
            self.pos += len(read)
            logging.info("from buffer pos:%s, read:%s" % (self.pos, len(read))) 
            return read
        while len(read) < expected:
            for start, _len, buff in self.buffs:
                logging.debug(">>>foreach buffs start:%s, _len:%s, expected:%s" % (start, _len, expected))
                #需要这个文件
                if end >= start and self.pos < (start + _len):
                    logging.debug("detect buffs start:%s, _len:%s, expected:%s" % (start, _len, expected))
                    r = ""
                    #先把字符串补齐, pre read
                    if start > self.pos:
                        r = self.buffer.read(start - self.pos)
                        self.pos += len(r)
                        expected -= len(r)
                        read += r
                        logging.debug("pre read from buffers start:%s, _len:%s, pos:%s, read:%s" % (start, _len, self.pos, len(r)))
                    remain = start + _len - self.pos
                    logging.debug("buffs start:%s, _len:%s, remain:%s,  expect:%s, pos:%s, r:%s" % (start, _len, remain, expected, self.pos, len(r)))
                    #需要这个文件的前n个字符
                    if expected <= remain:
                        r = buff.read(expected)
                        self.pos += len(r)
                        remain -= len(r)
                        expected -= len(r)
                        read += r
                        logging.debug("buffs read_0:%s, _len:%s, remain:%s,  expect:%s, pos:%s, r:%s" % (start, _len, remain, expected, self.pos, len(r)))
                    #需要从这个文件的末尾读一部分
                    elif expected > remain:
                        r = buff.read(remain)
                        self.pos += len(r)
                        remain -= len(r)
                        expected -= len(r)
                        read += r
                        logging.debug("buffs read_1:%s, _len:%s, remain:%s,  expect:%s, pos:%s, r:%s" % (start, _len, remain, expected, self.pos, len(r)))
                    logging.debug("%s %s ending.... r:%s read:%s" % (self.pos, expected, len(r), len(read)))
                if end < start: break

            if end > self.pos:
                r = self.buffer.read(expected)
                logging.info("from buffer pos:%s, expected:%s read:%s, current:%s-%s" % (self.pos, expected, len(r), self.buffer.len, self.buffer.pos)) 
                self.pos += len(r)
                read += r
                expected -= len(r)
            logging.debug("pos %s end %s" % (self.pos, end))
            if end == self.pos: break
            import time
            time.sleep(1)
        return read
    def read(self, size_to_read=1024 * 1024 * 1024 * 1024):
        return self.find_from_lazy_buff(size_to_read)
    
    size = property(lambda self:self.len - self.pos)

class Fetcher(object):
    headers = {}
    headers['User-Agent'] = """Mozilla/5.0 (Windows; U; Windows NT 5.2; en-US; rv:1.9.2.3) Gecko/20100401 Firefox/3.6.3 GTB6"""
    headers['Accept'] = 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8'
    headers['Accept-Encoding'] = 'gzip,deflate'
    headers['Accept-Language'] = "zh,en-us;q=0.7,en;q=0.3"
    headers['Accept-Charset'] = "ISO-8859-1,utf-8;q=0.7,*;q=0.7"
    headers['Connection'] = "keep-alive"
    headers['Keep-Alive'] = "115"
    headers['Cache-Control'] = "no-cache"

    def __init__(self, timeout=3):
        self.timeout = timeout
        self.opener = self._opener()

    def _opener(self):
        """
                        构建自动处理httpcookie的opener
        """
        cj = cookielib.CookieJar()
        opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cj), urllib2.HTTPHandler())
        return opener

    def _wrap(self, url, body=None, ext_headers={}):
        req = urllib2.Request(url, body, self.headers)
        for key, value in ext_headers.items():
            req.add_header(key, value)
        return req

    def head(self, url, body=None, ext_headers={}):
        try:
            ret = self.opener.open(self._wrap(url, body, ext_headers), timeout=self.timeout)
            ret.close()
            return ret.headers
        except:
            from traceback import print_exc
            out = StringIO()
            print_exc(file=out)
            logging.error("fetcher err: [%s] [%s]" % (out.getvalue(), url))
            ex = sys.exc_value
            if isinstance(ex, urllib2.HTTPError):
                return ex.headers
            return {}

    def fetch(self, url, body=None, ext_headers={}):
        """
                        抓取给定页面
        
        post文件的方法:
            fetcher = Fetcher()
            d = fetcher.fetch
            fetch(url, {"x":("1", "2"),"ty":["xv","xxx"],"xx":"1454", "myfile": ("test.txt", open("c:/district.txt", 'rb')),})
            
                        注意: open file的时候, 一定要用rb模式!!, 否则会出现读文件问题
        """
        ct = bd = hd = None
        if type(body) is dict:
            ct, bd = self.encode_multipart_formdata_readable(body)
            hd = ext_headers and copy.copy(ext_headers) or {}
            hd.update({"Content-type":ct})
            hd.update({"Content-length":bd.size})

        try:
            ret = self.opener.open(self._wrap(url, bd or body, hd or ext_headers), timeout=self.timeout)
            data = FetchedData.create(ret)
            ret.close()
            return data
        except:
            from traceback import print_exc
            out = StringIO()
            print_exc(file=out)
            logging.error("fetcher err: [%s] [%s]" % (out.getvalue(), url))          
            ex = sys.exc_value
            if isinstance(ex, urllib2.HTTPError):
                return FetchedData(url, ex.code, ex.msg, None, None)
            return FetchedData(url, 1000, out.getvalue(), None, None)

    def get_content_type(self, filename):
        return mimetypes.guess_type(filename)[0] or 'application/octet-stream'

    def encode_multipart_formdata(self, params):
        """
        fields is a sequence of (name, value) elements for regular form fields.
        files is a sequence of (name, filename, value) elements for data to be uploaded as files
        Return (content_type, body)
        """
        BOUNDARY = '----------ThIs_Is_tHe_bouNdaRY_$'
        CRLF = '\r\n'
        L = []
        for key, value in params.items():
            if type(value) in (tuple, list):
                if hasattr(value[-1], "read"):
                    filename, f = value[-2:]
                    L.append('--' + BOUNDARY)
                    L.append('Content-Disposition: form-data; name="%s"; filename="%s"' % (key, filename))
                    L.append('Content-Type: %s' % self.get_content_type(filename))
                    L.append('')
                    data = f.read()
                    L.append(data)
    #                while data:
    #                    L.append(data)
    #                    data = f.read(8192)
                    continue
                
                #如果不是文件参数
                for v in value:
                    L.append('--' + BOUNDARY)
                    L.append('Content-Disposition: form-data; name="%s"' % key)
                    L.append('')
                    L.append(v)
            else:
                L.append('--' + BOUNDARY)
                L.append('Content-Disposition: form-data; name="%s"' % key)
                L.append('')
                L.append(value)
    
        L.append('--' + BOUNDARY + '--')
        L.append('')
        body = CRLF.join(L)
        content_type = 'multipart/form-data; boundary=%s' % BOUNDARY
        return content_type, body


    def encode_multipart_formdata_readable(self, params):
        """
        fields is a sequence of (name, value) elements for regular form fields.
        files is a sequence of (name, filename, value) elements for data to be uploaded as files
        Return (content_type, body)
        """
        BOUNDARY = '----------ThIs_Is_tHe_bouNdaRY_$'
        CRLF = '\r\n'
        L = []
        for key, value in params.items():
            if type(value) in (tuple, list):
                if hasattr(value[-1], "read"):
                    filename, f = value[-2:]
                    L.append('--' + BOUNDARY)
                    L.append('Content-Disposition: form-data; name="%s"; filename="%s"' % (key, filename))
                    L.append('Content-Type: %s' % self.get_content_type(filename))
                    L.append('')
#                    data = f.read()
                    L.append(f)
                    continue
                
                #如果不是文件参数
                for v in value:
                    L.append('--' + BOUNDARY)
                    L.append('Content-Disposition: form-data; name="%s"' % key)
                    L.append('')
                    L.append(v)
            else:
                L.append('--' + BOUNDARY)
                L.append('Content-Disposition: form-data; name="%s"' % key)
                L.append('')
                L.append(value)
    
        L.append('--' + BOUNDARY + '--')
        L.append('')
        nl = []
        for n in L:
            nl.append(n)
            nl.append(CRLF)
        body = LazyIO(nl)
        content_type = 'multipart/form-data; boundary=%s' % BOUNDARY
        return content_type, body

class ImageFetcher(Fetcher):
    def __init__(self, patterns=".*(png|jpg)$"):
        super(ImageFetcher, self).__init__()
        self.p = re.compile(patterns, re.I)

    def head(self, url, body=None, ext_headers={}):
        s, h, path, _, _ = urlparse.urlsplit(url)
        domain = s + "://" + h
        self.headers["Referer"] = domain
        return super(ImageFetcher, self).head(url, body, ext_headers)

    def fetchImage(self, url):
        """
                        获取给定网址的图片
        """
        s, h, path, _, _ = urlparse.urlsplit(url)
        domain = s + "://" + h
        self.headers["Referer"] = domain
        d = super(ImageFetcher, self).fetch(url)
        if d.code == 200:
            return StringIO(d.content)
        return None

    def fetch(self, url, body=None, ext_headers={}):
        """
                        获取给定网址中的图片, 模式通过构造函数指定
        """
        d = super(ImageFetcher, self).fetch(url, body, ext_headers)
        if d.code == 200:
            title = re.findall(r"""title.*?>([^<>]*?)</title""", d.content, re.I | re.S | re.M)
            if title:
                title = title[0]
                try:
                    title = FetcherUtil.decode(title, "utf-8")
                except:
                    cs, _ = FetcherUtil.charset(d.content)
                    if cs: title = title.decode(cs).encode("utf-8")
            else: title = None
            imgs = re.findall(r"""img.*?src=\s*['"]([^'"<> ]*?)['"]""", d.content, re.I | re.S | re.M)
            return title, map(FetcherUtil._fix_url(url), filter(lambda u:self.p.match(u), imgs))
        else: return None, []

if __name__ == "__main__":
    fetcher = ImageFetcher()
    d = fetcher.fetch("http://www.xh78.com/")
    print d#    print FetcherUtil.decode(d.content, "gb2312")
