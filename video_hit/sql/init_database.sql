DROP database  if exists shuakua;

create database shuakua;

use  shuakua;

drop table if exists `todo_job`;

create table IF NOT EXISTS `todo_job` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(1024) DEFAULT NULL,
  `add_time` timestamp DEFAULT now(),
  `valid` boolean DEFAULT false,
  `num` int default 0,
  `deal_time` timestamp,
  `user_ip`  varchar(64),
  `user_name` varchar(64),
  `job_class`  text(64),
  PRIMARY KEY (`id`)
  ) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
  
drop table if exists `proxy`;
create table `proxy` (
	 `id` int(11) NOT NULL AUTO_INCREMENT,
	 `ip` varchar(64),
	 `port` int,
	 `disabled` boolean default false,
	 `ping` int,
	 `add_time` timestamp default now(),
	 `disable_time` timestamp,
	 `disable_type` int,
	 `comments` text,
	 `from_source` text,
	 PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
