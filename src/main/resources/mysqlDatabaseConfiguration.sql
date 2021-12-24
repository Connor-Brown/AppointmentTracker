DROP TABLE IF EXISTS `appointments`;
CREATE TABLE `appointments` (
  `id` int NOT NULL,
  `description` varchar(1024) NOT NULL,
  `date` datetime NOT NULL,
  `person_id` int DEFAULT NULL,
  `location_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `person_id_idx` (`person_id`),
  KEY `location_id_idx` (`location_id`),
  CONSTRAINT `location_id` FOREIGN KEY (`location_id`) REFERENCES `locations` (`id`),
  CONSTRAINT `person_id` FOREIGN KEY (`person_id`) REFERENCES `people` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

DROP TABLE IF EXISTS `locations`;
CREATE TABLE `locations` (
  `id` int NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

DROP TABLE IF EXISTS `people`;
CREATE TABLE `people` (
  `id` int NOT NULL,
  `name` varchar(255) NOT NULL,
  `affiliation` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;