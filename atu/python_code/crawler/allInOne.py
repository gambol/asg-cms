#!/usr/bin/env python
#coding=utf-8
# @author gambol
# @date 2013-7-1
# 爬虫定时任务的合集


import crawler
import transfer
import site_content_crawler
import store_summary
import ping
import scorer

crawler.run()
print "--------------crawler over---------------"
transfer.run()
print "--------------transfer over---------------"
site_content_crawler.crawlSites()
print "--------------site content over---------------"

store_summary.main()
print "--------------store summary over---------------"

ping.pingSites()
print "--------------ping over---------------"

#scorer.main()
#print "--------------scorer over---------------"
