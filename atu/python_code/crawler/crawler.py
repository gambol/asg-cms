#!/usr/bin/env python
#coding=utf-8
# @author gambol
# @date 2012-7-18
# 爬虫, 怕几个私服发布站

import MySQLdb  
import sys
import re
import urllib2
import getopt
import time
import socket
import re
from bs4 import BeautifulSoup
import copy

reload(sys)
sys.setdefaultencoding( "utf-8" )

socket.setdefaulttimeout(10)

endlFormatPattern = re.compile("[\n\t\r]+")
noAsciiFormatPattern = re.compile("[\x00-\x08\x0b-\x0c\x0e-\x1f]")
GBKPattern = re.compile(r"charset=gb", re.I);

conn = MySQLdb.connect(host='127.0.0.1', user='gambol', passwd="121212", unix_socket="/tmp/mysql.socket", charset="utf8", db="crawl")
cursor = conn.cursor()
cursor.execute('set names "utf8"');

new_site_count = 0
old_site_count = 0

commonPositionMap = {"名称|名字|私服":0,
              "线路" :0,
              "描述|介绍": 0,
              "链接" : 0,
              "地址" : 0,
              "标题" : 0,
              "版本|服务器说明" : 0,
              "IP"   : 0,
              "QQ"   : 0};

fieldCastDict = {"名称|名字|私服":'name',
              "线路" : 'line',
              "描述|介绍": 'description',
              "链接" : '',
              "地址" : 'exp',
              "标题" : 'title',
              "版本|服务器说明" : 'banben',
              "IP"   : 'jieshao',
              "url"  : "url",
              "QQ"   : 'qq'};


def fetchUrl(url):
    html = ""
    try:
        request = urllib2.Request(url)
        request.add_header('User-Agent', 'Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14) Gecko/20080404 (FoxPlus) Firefox/2.0.0.14')
        html = urllib2.urlopen(request).read()
    except:
        print "error in get content of "+url;
        
    return html

# deal with url, and put parse result into db
def dealUrlAndCategory(l):
    url = l[0]
    category_id = l[1]
    print "url:%s" %url
    
    html = fetchUrl(url)
    if (html == None or html == ""):
        return

    parserHtml(html, category_id)

def parserHtml(html, category_id):
    #print html
    soup = None
    if (GBKPattern.search(html) != None):
#    if (html.find("charset=gb") != -1):
        soup = BeautifulSoup(html, from_encoding='gb18030')
    else:
        soup = BeautifulSoup(html)
        
    (goodPositionMap,goodLineCounter, goodLinePartNum) = getGoodPostionMap(soup);
    print goodPositionMap
    print goodLineCounter
    print goodLinePartNum
    
    if (goodLineCounter < 4):
        print "fuck, it is error"
        return

    trs = soup.findAll(["tr", "dl"]);
    index = 1;
    for trContent in  trs:
        contentMap = getContent(trContent, goodPositionMap,  goodLinePartNum);
        if (contentMap != None):
            insertIntoDb(contentMap, category_id, index)
            index += 1

def stringEntities(str):
    result = noAsciiFormatPattern.subn("", str)
    result = endlFormatPattern.subn(" ", result[0])
    return result[0]

def insertIntoDb(contentMap, category_id, index):
    if (contentMap == None or not contentMap.has_key('url')):
        return


    allSqlFields = ["category_id", "origin_position"];
    valueFields = [str(category_id), str(index)];

    for key in contentMap.keys():
        value = "'" + contentMap[key] + "'"
        sqlField =  fieldCastDict[key] 
        valueFields.append(value)
        allSqlFields.append(sqlField)

    field  = ",".join(allSqlFields)
    value = ",".join(valueFields)
    value = stringEntities(value)
    name = contentMap['名称|名字|私服']
    url = contentMap['url']
    
    print  "to insert into db url:" + url

    select_url_sql = "select url, origin_position from crawl.parser_result where url = '%s'" % url;
#    print select_url_sql
    cursor.execute(select_url_sql);
    result = cursor.fetchall();
    if (len(result) == 0):
        sql = "insert into crawl.parser_result (%s) values (%s)" % (field, value)
        # sql = "update tophey.server_info set tophey.server_info.name = '%s' where url = '%s' and category_id = 2;" % (name, url)
        # print sql
    else:
        origin_position = result[0][1]
        new_postion = (origin_position + index) / 2
        sql = "update crawl.parser_result set origin_position = '%s' where url = '%s'" % (new_postion, url)
        
    cursor.execute(sql)

# 遍历全页面，找出最合适的一个模式
def getGoodPostionMap(soup):
    goodPositionMap = None
    goodLineCounter = 0
    goodLinePartNum = 0;
    content = ""
    lineContent = soup.findAll(["tr", "dl"])
    for partContent in lineContent:
        (positionMap, lineInfoCounter, linePartNum) = getPostionMap(partContent)
        if (lineInfoCounter > goodLineCounter and linePartNum < 2 * lineInfoCounter):
            goodPositionMap = positionMap
            goodLinePartNum = linePartNum
            goodLineCounter = lineInfoCounter
            content = partContent
            print 'partcontent:' + str(content)
        
    return (goodPositionMap, goodLineCounter, goodLinePartNum)

# 根据模式，选择出内容
def getContent(trContent, positionMap, goodLinePartNum):
    contentMap = {}
    tds = trContent.findAll(["td", "dd", "dt"])
    if (len(tds) != goodLinePartNum):
        return None

    index = 0

    reversedPostionMap = {}
    
    for key in positionMap.keys():
        if (positionMap[key] > 0):
            reversedPostionMap[positionMap[key]] = key

    for td in tds:
        index += 1
        text = "".join(td.findAll(text=True)).encode("UTF-8")
        url = td.findAll("a")
        if (reversedPostionMap.has_key(index)):
            contentMap[reversedPostionMap[index]] = text

        if (url != None and len(url) != 0):
            if (re.search("http://", url[0]["href"]) and not re.search("\.[rar|zip]", url[0]["href"])):
                contentMap['url'] = url[0]["href"]

    return contentMap

def  test() :
    sql = "insert into tophey.`group`(name, description) values('test', 'aaa')"
    #n = cursor.execute(sql);
#    sql = "insert into crawl.parser_result (category_id,origin_position,url,qq,banben,line,name) values (1,4471,'http://www.570808.com','费泡点顶级','推荐','独家','准５３８')";
#    sql = "insert into crawl.`parser_result` (`name`) values ('asdfasd')";
    n = cursor.execute(sql);
    print n;
    

# 根据tr里 td内容，得到相关信息
def getPostionMap(trContent):
    positionMap = copy.deepcopy(commonPositionMap)

    tds = trContent.findAll(['th', 'td', "dt", "dd"])
    index = 0;
    
    for td in tds:
        index += 1
        text = td.text.encode("UTF-8")
        for key in positionMap.keys():
            #if (text.find(key) != -1):
            if (len(text) < 5 * len(key) and re.search(key, text)):
                positionMap[key] = index
                # print "key:" + key
                # print "text:" + text

    lineCounter = 0;
    for key in positionMap.keys():
        if (positionMap[key] != 0):
            lineCounter += 1
    

    # index 是这一行中 所有块的数目
    # lineCounter 是这一行中，含有制定信息的块的数目
    # if (lineCounter > 4):
    #     print positionMap
    #     print lineCounter
    #     print index
    #     print trContent
        
    return (positionMap, lineCounter, index)

def run():
    cursor.execute('select url, category_id from crawl.crawl_source')
    result = cursor.fetchall()
    
    for r in result:
        dealUrlAndCategory(r);
        
    cursor.close();
    conn.commit();
    conn.close()
    print "crawler run over"
    

# main function
if __name__ == "__main__":
    cursor.execute('select url, category_id from crawl.crawl_source')
    result = cursor.fetchall()
    
    for r in result:
        dealUrlAndCategory(r);
    #html = fetchUrl('http://sf.haocq.com/');
#    html = fetchUrl('http://www.zhaosf.com/');
#    html = fetchUrl('http://9pk.118sh.com/');

    #html = open('www.8uu.com.htm').read()
    # html = open('www.yslead.com.htm').read()
    # html = open('www.bitiwow.com.htm').read()
    # html = fetchUrl('http://www.8uu.com/');
    # http://www.xbkdq.com/
    # http://www.zhaosf.com/
    # html = fetchUrl('http://www.bitiwow.com/');
    # html = fetchUrl('http://zhaokf.com/');
    # html = open("sf065Game.htm").read()
    #tml = open("wan50.html").read()
#    parserHtml(html, 1);
#    test();
    cursor.close();
    conn.commit();
    conn.close()
    print "hehe, new_site_count:" + str(new_site_count) + " old_site_count:" + str(old_site_count)
