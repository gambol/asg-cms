#!/usr/bin/env python

import os, re, threading

RESULT_PATTERN = re.compile(r'time=', re.M)

class Ping(threading.Thread):
    def __init__(self, ip_address):
        threading.Thread.__init__(self)
        self.ip_address = ip_address

    def run(self):
        result = os.popen('ping -c 1 -W 2 %s' % self.ip_address).read()
        ms='time=\d+.\d+'
        mstime=re.search(ms,result)
        MS = ""
        if not mstime:
            MS='timeout'
        else:
            MS=mstime.group().split('=')[1]

        print "MS:%s" % MS
        

def bulk_ping(url):
    print 'pinging url:%s' % url
    ping_thread = Ping(url)
    ping_thread.start()

if __name__ == '__main__':
    global lock
    lock = threading.Lock()
    bulk_ping('www.iozz.cn')
