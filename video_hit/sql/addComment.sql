use  shuakua;

drop table if exists `comment`;

create table IF NOT EXISTS `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comment` varchar(1024) DEFAULT NULL,
   `disabled` boolean default false,
  PRIMARY KEY (`id`)
  ) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
  
insert into comment(comment) values ('赞!');  
insert into comment(comment) values ('想快速增加视频dianji，请使用shuakua， 快速免费。赞！');  
insert into comment(comment) values ('视频成功的关键，刷跨！快速免费');  
insert into comment(comment) values ('我们都是“刷跨”的粉丝，你也不要错过了。快而免费');  
insert into comment(comment) values ('刷跨，不用不知道，一用真奇妙 ');  
insert into comment(comment) values ('喜欢这个视频。做的真棒，希望更多人看到它。请试试shuakua'); 
 -- 想快速增加视频dianji，请使用shuakua， 快速免费。赞！
 -- 视频成功的关键，刷跨！快速免费
 -- 我们都是“刷跨”的粉丝，你也不要错过了。快而免费
 -- 刷跨，不用不知道，一用真奇妙 
 -- 
 
drop table if exists `website_account`;

create table IF NOT EXISTS `website_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(1024) DEFAULT NULL,
  `password` varchar(1024) DEFAULT NULL,
  `web` varchar(1024) DEFAULT NULL,
   `disabled` boolean default false,
  PRIMARY KEY (`id`)
  ) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
  
insert into website_account(username, password, web) values ('shuakua@gmail.com', '121212', 'tudou');
insert into website_account(username, password, web) values ('huangcan85@gmail.com', 'zxcv100305', 'tudou');
insert into website_account(username, password, web) values ('baidukspm@gmail.com', '121212', 'tudou');
insert into website_account(username, password, web) values ('beckybeckyhc@gmail.com', '121212', 'tudou');

create table IF NOT EXISTS `fetch_list_url` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` varchar(1024) DEFAULT NULL,
  `web` varchar(1024) DEFAULT NULL,
   `disabled` boolean default false,
  PRIMARY KEY (`id`)
  ) ENGINE=InnoDB  DEFAULT CHARSET=utf8;
  
 insert into fetch_list_url(url, web) values ('http://www.tudou.com/cate/ich10a-2b-2c-2d-2e-2f-2g-2h-2i-2j-2k-2l-2m-2n-2o-2so2pe2pa1.html','tudou');
 insert into fetch_list_url(url, web) values ('http://www.tudou.com/cate/ich10a540b-2c-2d-2e-2f-2g-2h-2i-2j-2k-2l-2m-2n-2o-2so2pe2pa1.html','tudou');
 insert into fetch_list_url(url, web) values ('http://www.tudou.com/cate/ich10a546b-2c-2d-2e-2f-2g-2h-2i-2j-2k-2l-2m-2n-2o-2so2pe2pa1.html','tudou');
 insert into fetch_list_url(url, web) values ('http://www.tudou.com/cate/ich10a542b-2c-2d-2e-2f-2g-2h-2i-2j-2k-2l-2m-2n-2o-2so2pe2pa1.html','tudou');

