#!/usr/bin/env python
#coding=utf-8
# @author gambol
# @date 2012-7-18
# 一个略为有点功能的fetcher
# 将来希望能支持meta refresh 和 302跳转等

import urllib2
import urllib
import cookielib
import socket
import time

SOCKET_DEFAULT_TIMEOUT = 10

class DownloadManager:

    def __init__(self, cookie = None, timeout = None):
        # cookie
        if cookie == None:
            self.cookie = cookielib.LWPCookieJar() 
        else:
            self.cookie = cookie
        opener = urllib2.build_opener(urllib2.HTTPRedirectHandler, urllib2.HTTPCookieProcessor(self.cookie))
        urllib2.install_opener(opener)
        # socket timeout
        if timeout == None:
            timeout = SOCKET_DEFAULT_TIMEOUT
            socket.setdefaulttimeout(timeout)
                
    def clean_cookie(self):
        self.cookie = cookielib.LWPCookieJar() 
        
    def download(self, url, data = None):  #download the html page from server. 

        redirected_url = None
v        error_msg = None
        html = None
        # 1. URL Request Head
        user_agent = 'Mozilla/4.0 (compatible; MSIE 5.5; Windows NT)'
        headers = { 'User-Agent' : user_agent }
        if data != None:
            req = urllib2.Request(url, urllib.urlencode(data), headers)
        else:
            req = urllib2.Request(url, data, headers)
        # 2. URL Request
        try:
            response = urllib2.urlopen(req)
        except urllib2.URLError, e:
            if hasattr(e, 'reason'):
                #raise HtmlPageError('[fetch] failed to reach a server.' + \
                #            ' Reason: '+ str(e.reason) )
                error_msg = 'network-error'
            elif hasattr(e, 'code'):
                #raise HtmlPageError('[fetch] server couldn\'t fulfill the request.'+\
                #            ' Error code: '+ str(e.code) )
                error_msg = 'server-error'
            else:
                #raise HtmlPageError('[fetch] URLError, unknown reason.')
                error_msg = 'other-error'
        except KeyboardInterrupt:
            raise
        except:
            #raise HtmlPageError('[fetch] Unexpected urlopen() error: ' + 
            #            str(sys.exc_info()[0]) )
            error_msg = 'urlopen-error'
        
        # 3. Read Html 
        if error_msg != None:
            return error_msg, url, redirected_url, html
        try:
            html = response.read()
        except KeyboardInterrupt:
            raise
        except:
            #raise HtmlPageError('[fetch] Unexpected response.read() error: ' + \
            #            str(sys.exc_info()[0]) )
            error_msg = 'reading-error'

        # 4. check if there is a redirect
        if error_msg != None:
            f = open("undownloaded_urls.log", 'a')
            f.write(time.strftime("%Y/%m/%d %H:%M:%S") + "\t" + error_msg + "\t" + url + "\n")
            f.close()
            return error_msg, url, redirected_url, html

        nurl = response.geturl()
        if url != nurl:
            redirected_url = nurl
        else:
            redirected_url = None            
        
        return error_msg, url, redirected_url, html
    
if __name__ == "__main__":
    url = "http://new.yx150.com/"
    downloader = DownloadManager()
    print downloader.download(url)

