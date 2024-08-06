CREATE TABLE IF NOT EXISTS `store_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 自增',
  `store_no` varchar(50) NOT NULL COMMENT '店铺编号',
  `store_name` varchar(255) NOT NULL COMMENT '店铺名称',
  `client_id` varchar(255) NOT NULL COMMENT '应用程序ID',
  `client_secret` varchar(255) NOT NULL COMMENT '应用程序密码',
  `account_id` varchar(255) NOT NULL COMMENT '账户ID',
  `created_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE (`store_no`)
) COMMENT '比价店铺信息' ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE IF NOT EXISTS`goods_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键 自增',
  `store_no` varchar(50) NOT NULL COMMENT '店铺编号',
  `goods_no` varchar(50) NOT NULL COMMENT '商品编号',
  `goods_limit_price` bigint DEFAULT NULL COMMENT '商品比价限制价格',
  `created_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE (`goods_no`)
) COMMENT '商品信息' ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

	