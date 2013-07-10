#!/usr/bin/env python

import BeautifulSoup
import httplib2
import re
import urllib2
import sys
import socket

reload(sys)
sys.setdefaultencoding("utf-8")

socket.setdefaulttimeout(5)

#js_re = re.compile('autourl[\d+]="http://(.\w+)"', re.IGNORECASE)
js_re = re.compile(r'autourl\[\d+\]="http://(.+)"', re.IGNORECASE)

def redirect(content):
    match = js_re.search(content)
    if match:
        url = match.groups()[0].strip()
        url = "http://" + url
        print  "redirect url:" + url
        return url
    else:
        return meta_redirect(content)
    return None

def meta_redirect(content):
    soup  = BeautifulSoup.BeautifulSoup(content)

    result=soup.find("meta",attrs={"http-equiv":"refresh"})
    if result:
        wait,text=result["content"].split(";")
        if text.lower().startswith("url="):
            url=text[4:]
            return "meta redirect:" + url
            return url
    return None

def get_content(url):
    try:
        request = urllib2.Request(url)
        request.add_header('User-Agent', 'Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 (FoxPlus) Firefox/2.0.0.14')
        html = urllib2.urlopen(request).read()

        new_url = redirect(html)
        while new_url:
            html = urllib2.urlopen(new_url).read()
            new_url = redirect(html)

        if (UTFPattern.search(html) == None):
            html = html.decode("gbk").encode("utf-8")

        html = html.replace("'", "\\'")
    except Exception, e:
        print e
        html = "ERROR"

    return html

if __name__ == "__main__":
#    d = test_pattern('')
    d = get_content('http://03.3xxy.com/');
#    d = get_content('http://001.jnrongwei.com/');
#    d = get_content("http://www.xh78.com/")
    
    print d

