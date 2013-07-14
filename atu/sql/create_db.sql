-- script for build the site

DROP DATABASE IF EXISTS tophey;
CREATE DATABASE tophey CHARACTER SET utf8 COLLATE utf8_bin;
USE tophey;


DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) DEFAULT NULL,
  `password`  varchar(32) NOT NULL,
  `username` varchar(20) NOT NULL,
  `info` longtext,
  `telephone` varchar(20) DEFAULT NULL,
  `zipCode` varchar(10) DEFAULT NULL,
  `group_id` int(11) DEFAULT NULL,
  `ip`     varchar(20) DEFAULT NULL, 
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) CHARSET=utf8;

DROP TABLE IF EXISTS `server_info`;
CREATE TABLE IF NOT EXISTS `server_info` (
  `id` int(10) NOT NULL auto_increment,
  `name` varchar(128),
  `line` varchar(64),
  `description` varchar(2000),
  `url` varchar(256) character set utf8 NOT NULL,
  `title` varchar(100),
  `banner_url` varchar(200),
  `category_id`   int(10) NOT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  `is_disabled`  int(1)  NOT NULL default 0, 
  `disable_date`  datetime default NULL,
  `disable_reason`  varchar(200) character set utf8,
  PRIMARY KEY  (`id`)
--  UNIQUE KEY(`md5(url)`);
) CHARSET=utf8;


DROP TABLE IF EXISTS `server_sys_info`;
CREATE TABLE IF NOT EXISTS `server_sys_info` (
  `id` int(10),
  `name` varchar(20) character set utf8 NOT NULL,
  `category_id`   int(10) NOT NULL DEFAULT 0, 
  `refresh_date` datetime NOT NULL,
  `score`     double NOT NULL DEFAULT 0,
  `ping`     int (100),
  `server_num`  int(5) DEFAULT 0,    
  `server_create_time` datetime not NULL, 
  `server_new_open_time`  datetime not NULL,
  `vote_in`   int (10) DEFAULT 0,
  `vote_out`   int(10) DEFAULT 0,
  `privilege`   int(5) DEFAULT 0,      
  PRIMARY KEY  (`id`)
) CHARSET=utf8;


DROP TABLE IF EXISTS `category`;
CREATE TABLE IF NOT EXISTS `category` (
  `id` int(10) NOT NULL auto_increment,
  `name` varchar(20) character set utf8 NOT NULL,
  `image_url`  varchar(1024),
  `description` varchar(2000),
  `create_date` datetime NOT NULL,
  `display_order`  int(3) NOT NULL, 
  `is_disabled`    int(1) default 0,
  PRIMARY KEY  (`id`)
) CHARSET=utf8;

DROP TABLE IF EXISTS `group`;
CREATE TABLE IF NOT EXISTS `group` (
  `id` int(10) NOT NULL auto_increment,
  `name` varchar(20) character set utf8 NOT NULL,
  `description` varchar(2000)  NOT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY  (`id`)
) CHARSET=utf8;


DROP DATABASE IF EXISTS crawl;

create database crawl CHARACTER SET utf8 COLLATE utf8_bin;
use  crawl;

DROP TABLE IF EXISTS `crawl_source`;
CREATE TABLE IF NOT EXISTS `crawl_source` (
  `id` int(10) NOT NULL auto_increment,
  `url` varchar(1024) character set utf8 NOT NULL,
  `category_id`   int(10) NOT NULL, 
  `crawl_date` datetime default null, 
  `create_date` datetime NOT NULL,
  PRIMARY KEY  (`id`)
) CHARSET=utf8;

DROP TABLE IF EXISTS `parser_result`;

CREATE TABLE IF NOT EXISTS `parser_result` (
  `id` int(10) NOT NULL auto_increment,
  `name` varchar(20) character set utf8 ,
  `line` varchar(20) character set utf8 ,
  `description` varchar(2000) character set utf8,
  `url` varchar(200) character set utf8,
  `title` varchar(100),
  `banner_url` varchar(200),
  `category_id`   int(10),
  `create_date`  datetime,
  `jieshao` varchar(18) character set utf8 ,
  `banben` varchar(4) character set utf8,
  `qq` varchar(10) character set utf8,
  `exp`  varchar(10) character set utf8,
  `origin_position` int(10) default 0,
  PRIMARY KEY  (`id`)
) CHARSET=utf8;

DROP TABLE IF EXISTS `crawl_history`;
CREATE TABLE IF NOT EXISTS `crawl_history` (
  `id` int(10) NOT NULL auto_increment,
  `url` varchar(1024) character set utf8 NOT NULL,
  `http_code` int(3)  NOT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY  (`id`)
) CHARSET=utf8;

DROP TABLE IF EXISTS `site_crawler`;
CREATE TABLE IF NOT EXISTS `site_crawler` (
  `url` varchar(1024) character set utf8 NOT NULL,
  `http_code` int(3),
  `refresh_date` datetime,
  `html`   text
) CHARSET=utf8;
