-- ---
-- Globals
-- ---

-- SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
-- SET FOREIGN_KEY_CHECKS=0;

-- ---
-- Table 'User'
-- 
-- ---

DROP TABLE IF EXISTS `User`;
        
CREATE TABLE `User` (
  `id` integer(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `email` varchar(64) NOT NULL,
  `password` varchar(128) NOT NULL,
  `active` integer NOT NULL DEFAULT 1,
  `version` integer(11) NOT NULL DEFAULT 1,
  `id_role` integer(11) NOT NULL,
  PRIMARY KEY (`id`)
);

-- ---
-- Table 'Role'
-- 
-- ---

DROP TABLE IF EXISTS `Role`;
        
CREATE TABLE `Role` (
  `id` integer(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
);

-- ---
-- Table 'Book'
-- 
-- ---

DROP TABLE IF EXISTS `Book`;
        
CREATE TABLE `Book` (
  `id` integer(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `author` varchar(11) NOT NULL,
  `version` integer(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
);

-- ---
-- Table 'Unit'
-- 
-- ---

DROP TABLE IF EXISTS `Unit`;
        
CREATE TABLE `Unit` (
  `id` integer(11) NOT NULL AUTO_INCREMENT,
  `version` integer(11) NOT NULL DEFAULT 1,
  `id_book` integer(11) NOT NULL,
  PRIMARY KEY (`id`)
);

-- ---
-- Table 'Borrow'
-- 
-- ---

DROP TABLE IF EXISTS `Borrow`;
        
CREATE TABLE `Borrow` (
  `id` integer(11) NOT NULL AUTO_INCREMENT,
  `begin` date NOT NULL,
  `end` date NULL DEFAULT NULL,
  `id_unit` integer(11) NOT NULL,
  `id_user` integer(11) NOT NULL,
  PRIMARY KEY (`id`)
);

-- ---
-- Foreign Keys 
-- ---

ALTER TABLE `User` ADD FOREIGN KEY (id_role) REFERENCES `Role` (`id`);
ALTER TABLE `Unit` ADD FOREIGN KEY (id_book) REFERENCES `Book` (`id`);
ALTER TABLE `Borrow` ADD FOREIGN KEY (id_unit) REFERENCES `Unit` (`id`);
ALTER TABLE `Borrow` ADD FOREIGN KEY (id_user) REFERENCES `User` (`id`);

-- ---
-- Table Properties
-- ---

-- ALTER TABLE `User` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `Role` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `Book` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `Unit` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `Borrow` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;