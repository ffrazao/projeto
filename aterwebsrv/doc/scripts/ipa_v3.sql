-- MySQL Workbench Synchronization
-- Generated: 2016-10-07 13:02
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: frazao

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

ALTER TABLE `indice_producao`.`producao_forma_composicao` 
DROP FOREIGN KEY `fk_producao_forma_forma_producao_valor_1`;

ALTER TABLE `indice_producao`.`producao_forma` 
RENAME TO  `indice_producao`.`producao` ;

ALTER TABLE `indice_producao`.`producao_forma_composicao` 
CHANGE COLUMN `producao_forma_id` `producao_id` INT(11) NOT NULL COMMENT '' , RENAME TO  `indice_producao`.`producao_composicao` ;

ALTER TABLE `indice_producao`.`producao_composicao` 
ADD CONSTRAINT `fk_producao_forma_forma_producao_valor_1`
  FOREIGN KEY (`producao_id`)
  REFERENCES `indice_producao`.`producao` (`id`)
  ON DELETE CASCADE
  ON UPDATE NO ACTION;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
