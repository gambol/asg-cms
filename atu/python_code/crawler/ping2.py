#!/usr/bin/env python
#coding=utf-8
# @author gambol
# @date 2012-7-21

# 抓取某个站的html内容，判断这个站的速度快慢

import os, re, threading

import MySQLdb  
import sys
import re
import urllib2
import time
import socket
import threadpool

RESULT_PATTERN = re.compile(r'time=', re.M)

socket.setdefaulttimeout(5)

reload(sys)
sys.setdefaultencoding( "utf-8" )

conn = MySQLdb.connect(host='127.0.0.1', user='gambol', unix_socket="/home/zhenbao.zhou/.local/share/akonadi/socket-localhost/mysql.socket", charset="utf8", db="tophey")
cursor = conn.cursor()
cursor.execute('set names "utf8"');

def bulk_ping(r):
    ping = 10000
    
    serverId = r[0]
    url = r[1]
    sysId = r[2]
    category_id = r[3]
    name = r[4]
    
    print "url:" + url

    try:
        startTime = time.time()

        request = urllib2.Request(url)
        request.add_header('User-Agent', 'Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 (FoxPlus) Firefox/2.0.0.14')
        urllib2.urlopen(request).read()
        endTime = time.time()
        ping = (endTime - startTime) * 1000
    except Exception, e:
        print "error in get url:" + url
        # 毫秒为单位

    sql = ""
    if (sysId == None or sysId == "NULL"):
        sql = "insert into tophey.server_sys_info(id, name, category_id, refresh_date, score, ping) values (%d, %s, %d, current_timestamp, 0, %d) " % (serverId, name, category_id, ping)
        print sql
    else:
        sql = "update tophey.server_sys_info set ping = %d where id = %d" % (ping, sysId)

    cursor.execute(sql)

#    ping_thread = Ping(url, serverId, sysId, category_id, name)
#    ping_thread.start()

def nouse(request, result):
    pass
    
def pingSites():
    sql = 'select server_info.id, url, server_sys_info.id, server_info.category_id, server_info.name from server_info left join server_sys_info on server_info.id = server_sys_info.id'
    cursor.execute(sql)

    data = cursor.fetchall()

    l = []
    for r in data:
        l.append(list(r))
    
    pool = threadpool.ThreadPool(10)
    requests = threadpool.makeRequests(bulk_ping, l, nouse)
    [pool.putRequest(req) for req in requests]
    pool.wait()
    
if __name__ == "__main__":
    
    pingSites()

conn.close()
