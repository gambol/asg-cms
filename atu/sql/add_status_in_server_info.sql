use tophey;

ALTER TABLE server_info ADD status varchar(20) comment '站点的状态,包括是否删除, 在线, 隐藏等等';

ALTER TABLE server_info MODIFY status varchar(20) default 'online';

update server_info set status = 'online';

