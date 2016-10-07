-- MySQL Workbench Synchronization
-- Generated: 2016-10-07 10:54
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: frazao

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

ALTER TABLE `indice_producao`.`producao_forma` 
DROP FOREIGN KEY `fk_producao_forma_1`;

ALTER TABLE `indice_producao`.`producao` 
RENAME TO  `indice_producao`.`producao_proprietario` ;

ALTER TABLE `indice_producao`.`producao_forma` 
CHANGE COLUMN `producao_id` `producao_proprietario_id` INT(11) NOT NULL COMMENT '' ;

ALTER TABLE `indice_producao`.`producao_forma` 
ADD CONSTRAINT `fk_producao_forma_1`
  FOREIGN KEY (`producao_proprietario_id`)
  REFERENCES `indice_producao`.`producao_proprietario` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
