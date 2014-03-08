#!/usr/bin/env python
#coding=utf-8
# @author gambol
# @date 2012-7-22
# 提取某个html的摘要

import re, sys
#from bs4 import BeautifulSoup as BS, NavigableString, Tag
from BeautifulSoup import BeautifulSoup as BS, NavigableString, Tag
import util

UTFPattern = re.compile(r"charset=utf", re.I);
endlFormatPattern = re.compile("[\n\t\r]+")
spacePattern = re.compile(r"(&nbsp;)+")

reload(sys)
sys.setdefaultencoding( "utf-8" )

class HTMLSummary():
    ignore_tags = ('title', 'style', 'script', 'SCRIPT', 'STYLE')
    # 多个连续的空白字符将被替换成一个, 下面是re模式.
    multi_spaces_pat = re.compile(r'\s{2,}')
 
    def __init__(self, html, char_limit=300):
        self.char_limit = char_limit
        self.char_cnt = 0
        try:
            if (UTFPattern.search(html) == None):
                html = html.decode("gbk").encode("utf-8")
        except Exception,e:
            html = html

        self._doc = BS(html)
        if (self._doc.body != None):
            self._doc = self._doc.body
            
        self._exec(self._doc)
 
    def __str__(self):
        return str(self._doc)
 
    def _exec(self, doc):
        for i in doc:
            if type(i) == NavigableString:
                pretty_str = HTMLSummary.multi_spaces_pat.sub(' ', i)
                if i.strip() and self.char_cnt + len(pretty_str) >= self.char_limit:
                    self._cut_after(i)
                    to_pick = pretty_str[0: self.char_limit - self.char_cnt]
                    i.replaceWith(to_pick)
                    '''
                    以实际有效(被浏览器显示)的字符数为准, 该方法并不精确:
                    比如: <span> inline text</span>, 前面的空格将被计算在内,
                    而它是否显示, 则取决于它是否是一个块元素的第一个子元素;
                    另外, <p> para</p> 中的前导空格并未排除,
                    因为无法预知它是否被CSS定义成行内元素.
                    '''
                    self.char_cnt += len(to_pick)
                    break;
                elif i.strip(): # 忽略只有空白字符的文本元素
                    self.char_cnt += len( HTMLSummary.multi_spaces_pat.sub(' ', i) )
            elif type(i) == Tag:
                if i.name not in HTMLSummary.ignore_tags:
                    self._exec(i)
                else:
                    i.extract()
                
 
    def _cut_after(self, node):
        '''删除指定节点之后所有的同级节点,
        然后删除该节点父节点的所有同级节点(即比自己父亲小的所有叔节点),
        以此递归...
        '''
        cur = node.nextSibling
        parent = node.parent      # 父节点
        while cur:
            nextpeer = cur.nextSibling
            if type(cur) == Tag and cur.name in HTMLSummary.ignore_tags:
                pass
            else:
                cur.extract()
            cur.extract()
            cur = nextpeer
        if parent:
            self._cut_after(parent)
 
    def prettify(self): return self._doc.prettify()

    def text(self):
        #re = self._doc.get_text()
        re = "".join(self._doc.findAll(text=True))
        re = endlFormatPattern.subn(" ", re)
        re = util.html_entities(re[0])
        return re
 
 
############# TEST #################
import sys
def main():
    html = file(sys.argv[1]).read();
    su = HTMLSummary(html, int(sys.argv[2]))
 
if __name__ == "__main__":
    main()
