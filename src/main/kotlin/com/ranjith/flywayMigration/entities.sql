CREATE TABLE `orders` (
  `order_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `order_type` varchar(50) DEFAULT NULL,
  `stock_id` bigint DEFAULT NULL,
  `original_quantity` int DEFAULT NULL,
  `remaining_quantity` int DEFAULT NULL,
  `price` double DEFAULT NULL,
  `order_accepted_at` timestamp NULL DEFAULT NULL,
  `order_status` varchar(50) DEFAULT NULL,
  `version` bigint DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `created_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`order_id`),
  KEY `user_id` (`user_id`),
  KEY `stock_id` (`stock_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  CONSTRAINT `orders_ibfk_2` FOREIGN KEY (`stock_id`) REFERENCES `stocks` (`stock_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `stocks` (
  `stock_id` bigint NOT NULL AUTO_INCREMENT,
  `stock_symbol` varchar(50) DEFAULT NULL,
  `stock_name` varchar(255) DEFAULT NULL,
  `stock_price` double DEFAULT NULL,
  `version` bigint DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `created_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`stock_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `trades` (
  `trade_id` bigint NOT NULL AUTO_INCREMENT,
  `trade_type` varchar(50) DEFAULT NULL,
  `buyer_order_id` bigint DEFAULT NULL,
  `seller_order_id` bigint DEFAULT NULL,
  `stock_id` bigint DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `price` double DEFAULT NULL,
  `trade_timestamp` timestamp NULL DEFAULT NULL,
  `version` bigint DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `created_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`trade_id`),
  UNIQUE KEY `buyer_order_id` (`buyer_order_id`),
  UNIQUE KEY `seller_order_id` (`seller_order_id`),
  KEY `stock_id` (`stock_id`),
  CONSTRAINT `trades_ibfk_1` FOREIGN KEY (`buyer_order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `trades_ibfk_2` FOREIGN KEY (`seller_order_id`) REFERENCES `orders` (`order_id`),
  CONSTRAINT `trades_ibfk_3` FOREIGN KEY (`stock_id`) REFERENCES `stocks` (`stock_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `email_id` varchar(255) DEFAULT NULL,
  `version` bigint DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `created_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_on` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
