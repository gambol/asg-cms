#!/usr/bin/env python
#coding=utf-8
# @author gambol
# @date 2012-7-18
# 通用的工具相关

import MySQLdb  
import sys
import re
import urllib2
import getopt
import time
import socket
import re
from BeautifulSoup import BeautifulSoup
import copy

reload(sys)
sys.setdefaultencoding( "utf-8" )

socket.setdefaulttimeout(10)

# 获得db连接
def getConn():
    conn = MySQLdb.connect(host='127.0.0.1', user='gambol', passwd="121212", unix_socket="/tmp/mysql.socket", charset="utf8", db="tophey")
#    conn = MySQLdb.connect(host='27.120.101.160', user='gambol', passwd='121212', charset="utf8", db="crawl")
    cursor = conn.cursor()
    cursor.execute('set names "utf8"');
    cursor.execute("set interactive_timeout=24*3600");
#    cursor.execute("set max_allowed_packet=10*1000*1000*1000"); # 10m
    conn.autocommit(True);
    return (conn, cursor)

def fetchUrl(url):
    html = ""
    try:
        request = urllib2.Request(url)
        request.add_header('User-Agent', 'Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 (FoxPlus) Firefox/2.0.0.14')
        html = urllib2.urlopen(request).read()
    except:
        print "error in get content of "+url;
        
    return html
    

def html_entities(html):
    spacePattern = re.compile(r"(&nbsp;)+")
    html = spacePattern.subn(" ", html)[0]
    html = html.replace('&amp;', "&").replace('&lt;', "<").replace('&gt;', '>').replace('&quot;', '"').replace('&#39;', "'")
    return html
    


