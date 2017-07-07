
CREATE USER 'studDbUser_mvc2'@'localhost' IDENTIFIED BY 'spring';
GRANT ALL PRIVILEGES ON cs548_springmvc2_studentdb.* TO 'studDbUser_mvc2'@'localhost' WITH GRANT OPTION;
SHOW GRANTS FOR 'studDbUser_mvc2'@'localhost';



CREATE SCHEMA IF NOT EXISTS `cs548_springmvc2_studentdb` ;
USE `cs548_springmvc2_studentdb` ;
DROP TABLE IF EXISTS `cs548_springmvc2_studentdb`.`student`;


CREATE TABLE `cs548_springmvc2_studentdb`.`student` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `lastName` VARCHAR(45) NOT NULL,
  `firstName` VARCHAR(45) NOT NULL,
  `age` SMALLINT NULL,
  `gender` TINYINT NOT NULL,
  PRIMARY KEY (`id`));
  
