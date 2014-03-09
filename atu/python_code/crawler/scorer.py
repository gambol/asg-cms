#!/usr/bin/env python
#coding=utf-8
# @author gambol
# @date 2012-7-18
# 根据原则，给各个站点打分

import util
import sys

reload(sys)
sys.setdefaultencoding( "utf-8" )

conn, cursor = util.getConn()

def main():
    sql = "select s.id, s.name, s.line, s.description, s.url, s.title, s.category_id, ss.ping, ss.origin_position, site_from from tophey.server_info as s left join tophey.server_sys_info as ss on s.id = ss.id  where s.is_disabled = 0 and ss.name is not NULL and status = 'online';"
    cursor.execute(sql);
    result = cursor.fetchall()

    for r in result:
        s = score(r)
        storeScore(r[0], s)

    conn.commit()
    conn.close()

def score(r):
    id = r[0]
    name = r[1]
    line = r[2]
    desc = r[3]
    url = r[4]
    title = r[5]
    category_id = r[6]
    ping = r[7]
    origin_position = r[8]
    site_from = r[9]

    # 原始分
    score = 50
    print "---------------------start--------------------"
    if (name == None or name.strip == '' or name == 'NULL'):
        score -=  200

#    print "line:" + line
    if (line.find("双线") != -1 or (line.find("电信") != -1 and line.find("网通") != -1)):
        score += 5

#    print "desc:" + desc
    if (desc == None or desc.strip == '' or desc == 'NULL'):
        score -= 200
    elif (len(desc) > 50):
        score += 10
    elif (len(desc) < 30):
        score -= 20

    print "url:" + url
    if (url[8:].find(":") != -1):
        score -= 20
    elif(url.find("\.com")):
        score += 5

    if (url.find("user.qq") != -1 or url.find("baidu") != -1):
        score -= 400;

    if (url.find("//www") != -1):
        score += 2
        
    #http://www.6666666.com/
    if (len(url) < 23):
        score += 10

    # 用户输入的，永远排在前面
    if (site_from == 'atu'):
        score += 100

    try:
        if (ping != "NULL" and ping != None and int(ping) < 800):
            score += 5
        if (ping == 10000 or ping == None) :
            score = -400 # 无法读取内容, 放在最后
    except Exception, e:
        print e
        pass

    
    try:
        if (origin_position != 0 and origin_position != None) :
            if (int(origin_position) < 30 and int(origin_position)):
                score += 10
            elif (int(origin_position) < 60):
                score += 5
    except Exception, e:
        print e
        pass

    if (score < 0):
        score = 0
        
    print "url:" + url + " score:" + str(score)
    print "-============end---------------------"
    return score

def storeScore(id, score):
    sql = "update tophey.server_sys_info set score = %d where id = %d;" % (score, id)
    cursor.execute(sql)
    conn.commit()

if __name__ == "__main__":
    main()

