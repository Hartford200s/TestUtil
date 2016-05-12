CREATE TABLE `book` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bookname` varchar(255) DEFAULT NULL COMMENT '圖書名稱',
  `bookprice` double DEFAULT NULL COMMENT '圖書價格',
  `bookimage` varchar(255) DEFAULT NULL COMMENT '圖書封面',
  `createtime` datetime DEFAULT NULL COMMENT '創建時間',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
