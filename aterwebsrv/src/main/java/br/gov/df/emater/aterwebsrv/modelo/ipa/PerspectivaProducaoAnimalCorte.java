package br.gov.df.emater.aterwebsrv.modelo.ipa;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;

/**
 * The persistent class for the perspectiva_corte database table.
 * 
 */
@Entity
@Table(name = "perspectiva_corte", schema = EntidadeBase.IPA_SCHEMA)
@PrimaryKeyJoinColumn(name = "id")
public class PerspectivaProducaoAnimalCorte extends PerspectivaProducaoAnimal {

	private static final long serialVersionUID = 1L;

}