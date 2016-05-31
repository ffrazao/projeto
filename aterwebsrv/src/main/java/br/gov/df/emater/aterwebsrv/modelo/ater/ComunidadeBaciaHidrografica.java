package br.gov.df.emater.aterwebsrv.modelo.ater;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

/**
 * The persistent class for the comunidade_bacia_hidrografica database table.
 * 
 */
@Entity
@Table(name = "comunidade_bacia_hidrografica", schema = EntidadeBase.ATER_SCHEMA)
public class ComunidadeBaciaHidrografica extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<ComunidadeBaciaHidrografica> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "bacia_hidrografica_id")
	private BaciaHidrografica baciaHidrografica;

	@ManyToOne
	@JoinColumn(name = "comunidade_id")
	private Comunidade comunidade;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	public ComunidadeBaciaHidrografica() {
		super();
	}

	public ComunidadeBaciaHidrografica(Integer id, Comunidade comunidade, BaciaHidrografica baciaHidrografica) {
		setId(id);
		setComunidade(comunidade);
		setBaciaHidrografica(baciaHidrografica);
	}

	public ComunidadeBaciaHidrografica(Serializable id) {
		super(id);
	}

	public BaciaHidrografica getBaciaHidrografica() {
		return baciaHidrografica;
	}

	public Comunidade getComunidade() {
		return comunidade;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public ComunidadeBaciaHidrografica infoBasica() {
		return new ComunidadeBaciaHidrografica(this.id, this.comunidade != null ? this.comunidade.infoBasica() : null, this.baciaHidrografica != null ? this.baciaHidrografica.infoBasica() : null);
	}

	public void setBaciaHidrografica(BaciaHidrografica baciaHidrografica) {
		this.baciaHidrografica = baciaHidrografica;
	}

	public void setComunidade(Comunidade comunidade) {
		this.comunidade = comunidade;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

}