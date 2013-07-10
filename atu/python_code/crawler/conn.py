#!/usr/bin/env python
#coding=utf-8
# @author gambol

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
