use `j2cms`;

DROP TABLE IF EXISTS `to_fetch_url`;
CREATE TABLE `to_fetch_url` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `url` text,
  `fetched` boolean default false,
  PRIMARY KEY (`id`)
) CHARSET=utf8;
