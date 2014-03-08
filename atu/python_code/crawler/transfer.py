#!/usr/bin/env python
#coding=utf-8
# @author gambol
# @date 2012-7-18
# 将parser的数据，插入到tophey数据库之中
# 主要工作，url去重, url规整, 并进行初步算分

import MySQLdb  
import sys
import re
import time
import socket
import re
import copy
import urlparse
import util

reload(sys)
sys.setdefaultencoding( "utf-8" )

(conn, cursor) = util.getConn();
cursor.execute('set names "utf8"');

MAX_POSITION = 999999

def transfer(result):
    lasturl = ""
    for r in result:
        id = r[0];
        name = r[1];
        line = r[2];
        description = r[3]
        url = urlNorm(r[4])
        title = r[5]
        category_id = r[6]
        origin_position = r[7]

        if (url == lasturl):
            pass
        else:
            addUrl(name, line, description, url, title, category_id, origin_position)


# 将url归一化
# 暂时不能做太多的事情
def urlNorm(url):
    url = url.strip()
    parsedTuple = urlparse.urlparse(url)
    #newUrl = urlparse.urljoin(parsedTuple)
    path = parsedTuple.path
    if [path == '']:
        path = "/"

    unparsedURL = ""
    if (parsedTuple.netloc.find("wan50.com") != -1):
        queryDict = urlparse.parse_qs(parsedTuple.query)
        if (queryDict.has_key('u')):
            for candidate in queryDict['u']:
                if candidate.find("http:") == 0:
                    return candidate
        else:
            unparsedURL = url
        pass
    else:
        unparsedURL = urlparse.urlunparse((parsedTuple.scheme, parsedTuple.netloc, path, parsedTuple.params, parsedTuple.query, ''))
    
    return unparsedURL

def updateUrlScore(url, origin_position):
    server_info_sql = "update tophey.server_info set  origin_position = %d, where url = '%s'" % (origin_position, url);
    cursor.execute(select_url_sql);

def addUrl(name, line, description, url, title, category_id, origin_position):
    select_url_sql = "select url, id from tophey.server_info where url = '%s'" % url;
#    print select_url_sql
    cursor.execute(select_url_sql);
    result = cursor.fetchall();
    # 不需要更新了
    if (len(result) > 0):
        # server_info_sql = "update tophey.server_info set name = '%s', description = '%s', title = '%s' where `url` = '%s' and category_id = %d" % (name, description, title,  url, category_id)
        # print server_info_sql
        # cursor.execute(server_info_sql)
        server_sys_info = "update tophey.server_sys_info set  origin_position = %d where id = %d" % (origin_position, result[0][1]);
        cursor.execute(server_sys_info);
#        print result
        return
    
    server_info_sql = "insert into tophey.server_info(name, line, description, url, title, category_id, create_date, update_date) values('%s', '%s','%s', '%s', '%s', '%s', current_timestamp, current_timestamp);" % (name, line, description, url, title, category_id)
#    print server_info_sql
    try:
        cursor.execute(server_info_sql)
    except:
        return

    cursor.execute("select last_insert_id();");
    lastId = str(int(cursor.fetchone()[0]))  
#    print lastId

    server_sys_info_sql = "insert into tophey.server_sys_info(id, name, category_id, origin_position, refresh_date) values ('%s', '%s', %d, %d, current_timestamp);" % (lastId, name, category_id, origin_position)
    
 #   print server_sys_info_sql
    cursor.execute(server_sys_info_sql)

def run():
    cursor.execute('select id,name, line, description, url, title,category_id, origin_position from crawl.parser_result  where id > 0  order by url, origin_position asc');
    result = cursor.fetchall()
    transfer(result)
    conn.commit();
    conn.close()
    print "transfer run over\n";
    

if __name__ == "__main__":
    cursor.execute('select id,name, line, description, url, title,category_id, origin_position from crawl.parser_result  where id > 0  order by url, origin_position asc');
    result = cursor.fetchall()
    transfer(result)
    # url = urlNorm("http://advz/");
    # print url

    # insertDB('aaa', 'line', 'description', 'url', 'title', 4, 5)
    # print(urlNorm("http://cc.com"));
    # print(urlNorm("http://www.wan50.com/url.htm?u=http://www.femswow.com "));

# conn.commit();
# conn.close()
