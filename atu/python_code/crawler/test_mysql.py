#!/usr/bin/env python
#coding=utf-8
# @author gambol
# @date 2012-7-18

import MySQLdb  
import sys

reload(sys)
sys.setdefaultencoding('utf-8')

conn = MySQLdb.connect(host='27.120.101.160', user='gambol',passwd='121212', db='tophey')

cursor = conn.cursor()

cursor.execute('set names "utf8"');
# cursor.execute('insert into category(name, description, create_date, display_order) values ("test", "牛逼", current_timestamp, 1)');

cursor.execute('select * from category')
result = cursor.fetchall()

for r in result:
    for data in r:
        print data


conn.close()  
