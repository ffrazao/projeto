package br.gov.df.emater.aterwebsrv.modelo.ater;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

/**
 * The persistent class for the propriedade_rural_forma_utilizacao_espaco_rural
 * database table.
 * 
 */
@Entity
@Table(name = "propriedade_rural_forma_utilizacao_espaco_rural", schema = EntidadeBase.ATER_SCHEMA)
public class PropriedadeRuralFormaUtilizacaoEspacoRural extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "forma_utilizacao_espaco_rural_id")
	private FormaUtilizacaoEspacoRural formaUtilizacaoEspacoRural;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "propriedade_rural_id")
	private PropriedadeRural propriedadeRural;

	public PropriedadeRuralFormaUtilizacaoEspacoRural() {
	}

	public FormaUtilizacaoEspacoRural getFormaUtilizacaoEspacoRural() {
		return formaUtilizacaoEspacoRural;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public PropriedadeRural getPropriedadeRural() {
		return propriedadeRural;
	}

	public void setFormaUtilizacaoEspacoRural(FormaUtilizacaoEspacoRural formaUtilizacaoEspacoRural) {
		this.formaUtilizacaoEspacoRural = formaUtilizacaoEspacoRural;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPropriedadeRural(PropriedadeRural propriedadeRural) {
		this.propriedadeRural = propriedadeRural;
	}

}