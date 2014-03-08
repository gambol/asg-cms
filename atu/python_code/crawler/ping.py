#!/usr/bin/env python
#coding=utf-8
# @author gambol
# @date 2012-7-21

import os, re, threading

import MySQLdb  
import sys
import re
import urllib2
import time
import socket
import threadpool

import util

RESULT_PATTERN = re.compile(r'time=', re.M)

socket.setdefaulttimeout(5)


class Ping(threading.Thread):
    def __init__(self, url, serverId, sysId, category_id, name):
        threading.Thread.__init__(self)
        self.url = url
        self.serverId = serverId
        self.sysId = sysId
        self.categoryId = category_id
        self.name = name

    def run(self):
        url = self.url
        startTime = time.time()
        request = urllib2.Request(url)
        request.add_header('User-Agent', 'Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 (FoxPlus) Firefox/2.0.0.14')
        urllib2.urlopen(request).read()
        endTime = time.time()

        # 毫秒为单位
        ping = (endTime - startTime) * 1000
        
        sql = ""
        if (self.sysId == None or self.sysId == "NULL"):
            sql = "insert into tophey.server_sys_info(id, name, category_id, refresh_date, score, ping) values (%d, %s, %d, current_timestamp, 0, %d) " % (self.serverId, self.name, self.category_id, ping)
            print sql
        else:
            sql = "update tophey.server_sys_info set ping = %d where id = %d" % (ping, self.sysId)
            print sql
            
        cursor.execute(sql)

def bulk_ping(r):
    ping = 10000
    serverId = r[0]
    url = r[1]
    sysId = r[2]
    category_id = r[3]
    name = r[4]

    print "ping url:" + url
    try:
        startTime = time.time()

        request = urllib2.Request(url)
        request.add_header('User-Agent', 'Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 (FoxPlus) Firefox/2.0.0.14')
        response = urllib2.urlopen(request)
        html = response.read()

        if (response.getcode() != 200 and response.getcode() != 302):
            ping = 10000;
            
        else:
            endTime = time.time()
            ping = (endTime - startTime) * 1000
    except Exception, e:
        print "error in get url:" + url
        ping = 10000
        # 毫秒为单位

    sql = ""
    if (sysId == None or sysId == "NULL"):
        sql = "insert into server_sys_info(id, name, category_id, refresh_date, score, ping) values (%d, '%s', %d, current_timestamp, 0, %d); " % (serverId, name, category_id, ping)
    else:
        sql = "update server_sys_info set ping = %d where id = %d;" % (ping, sysId)

    (conn2, cursor2) = util.getConn()
    conn2.autocommit(True)
    print sql
    cursor2.execute(sql)
    cursor2.close()
    conn2.commit()
    conn2.close()

#    ping_thread = Ping(url, serverId, sysId, category_id, name)
#    ping_thread.start()
    
def pingSites():
    (conn, cursor) = util.getConn()
    print "start ping sites"
    sql = "select si.id, url, ssi.id, si.category_id, si.name from tophey.server_info si left join tophey.server_sys_info ssi on si.id = ssi.id where is_disabled = 0 and status = 'online' "
    cursor.execute(sql)
    result = cursor.fetchall()
    l = []
    for r in result:
        #bulk_crawl(list(r))
        l.append(list(r))

    pool = threadpool.ThreadPool(50)
    requests = threadpool.makeRequests(bulk_ping, l, nouse)
    [pool.putRequest(req) for req in requests]
    pool.wait()
    print "ping crawler over"
    conn.commit()
    conn.close()

    # for r in result:
    #     bulk_ping(r)

def nouse(request, result):
    pass

if __name__ == "__main__":
    pingSites()

