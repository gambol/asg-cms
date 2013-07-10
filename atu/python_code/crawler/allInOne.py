#!/usr/bin/env python
#coding=utf-8
# @author gambol
# @date 2013-7-1
# 爬虫定时任务的合集


import crawler
import transfer
import site_content_crawler
import store_summary

crawler.run()
transfer.run()
site_content_crawler.crawlSites()
store_summary.main()
