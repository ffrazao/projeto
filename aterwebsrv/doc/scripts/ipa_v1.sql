-- MySQL Workbench Synchronization
-- Generated: 2016-10-07 10:30
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: frazao

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

RENAME TABLE `indice_producao`.`bem` to `indice_producao`.`bem_classificado`;

ALTER TABLE `indice_producao`.`producao` 
DROP FOREIGN KEY `fk_producao_1`;

ALTER TABLE `indice_producao`.`bem_forma_producao_media` 
DROP FOREIGN KEY `fk_bem_forma_producao_media_5`;

ALTER TABLE `indice_producao`.`bem_forma_producao_media_composicao` 
DROP FOREIGN KEY `fk_bem_forma_producao_media_forma_producao_valor_1`;

ALTER TABLE `indice_producao`.`producao` 
CHANGE COLUMN `bem_id` `bem_classificado_id` INT(11) NOT NULL COMMENT '' ;

ALTER TABLE `indice_producao`.`bem_forma_producao_media` 
CHANGE COLUMN `bem_id` `bem_classificado_id` INT(11) NOT NULL COMMENT '' , RENAME TO  `indice_producao`.`bem_classificado_forma_producao_media` ;

ALTER TABLE `indice_producao`.`bem_forma_producao_media_composicao` 
RENAME TO  `indice_producao`.`bem_classificado_forma_producao_media_composicao` ;

ALTER TABLE `indice_producao`.`producao` 
ADD CONSTRAINT `fk_producao_1`
  FOREIGN KEY (`bem_classificado_id`)
  REFERENCES `indice_producao`.`bem_classificado` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `indice_producao`.`bem_forma_producao_media` 
ADD CONSTRAINT `fk_bem_forma_producao_media_5`
  FOREIGN KEY (`bem_classificado_id`)
  REFERENCES `indice_producao`.`bem_classificado` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `indice_producao`.`bem_forma_producao_media_composicao` 
ADD CONSTRAINT `fk_bem_forma_producao_media_forma_producao_valor_1`
  FOREIGN KEY (`bem_forma_producao_media_id`)
  REFERENCES `indice_producao`.`bem_classificado_forma_producao_media` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `credito_rural`.`custo_producao` 
DROP FOREIGN KEY `fk_custo_producao_1`;

ALTER TABLE `credito_rural`.`custo_producao` 
CHANGE COLUMN `bem_id` `bem_classificado_id` INT(11) NOT NULL COMMENT '' ;

ALTER TABLE `credito_rural`.`custo_producao` 
ADD CONSTRAINT `fk_custo_producao_1`
  FOREIGN KEY (`bem_classificado_id`)
  REFERENCES `indice_producao`.`bem_classificado` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
