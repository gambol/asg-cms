#!/usr/bin/env python
#coding=utf-8
# @author gambol
# @date 2012-7-22
# 存储summary

from summary import HTMLSummary
import util

import sys
import re

conn,cursor = util.getConn()

def getHtmlFromDB(url):
    sql = "select html from crawl.site_crawler where url = '%s';" % url
    cursor.execute(sql)
    result = cursor.fetchall()

    html = "error"
    if (len(result) >= 1):
        html = result[0][0]

    return html

def getSummary(html, words=1000):
    #summary = get_summary(html)

    summary =  HTMLSummary(html, words).text()
    return summary

def store(url, summary):
    if (summary != None and summary != "ERROR" and len(summary.strip()) != 0 and summary != 'error'):
        sql = "update tophey.server_info set description = '%s', summary = '%s' where url = '%s'" % (summary.strip(),summary.strip(), url)
        (conn2, cursor2) = util.getConn()
        print sql
        try:
            cursor2.execute(sql)
            conn2.commit()
            conn2.close()
        except Exception, e:
            print e
        

def main():
    sql = "select url from tophey.server_info si join server_sys_info ssi on si.id = ssi.id where si.is_disabled = 0 order by ssi.score desc;"
    cursor.execute(sql)
    result = cursor.fetchall()
    for r in result:
        html = getHtmlFromDB(r[0])
        summary = getSummary(html)
        store(r[0], summary)

if __name__ == "__main__":
    
    main()

