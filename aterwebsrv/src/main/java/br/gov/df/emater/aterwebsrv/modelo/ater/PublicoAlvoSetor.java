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
 * The persistent class for the publico_alvo_setor database table.
 * 
 */
@Entity
@Table(name = "publico_alvo_setor", schema = EntidadeBase.ATER_SCHEMA)
public class PublicoAlvoSetor extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "publico_alvo_id")
	private PublicoAlvo publicoAlvo;

	@ManyToOne
	@JoinColumn(name = "setor_id")
	private Setor setor;

	public PublicoAlvoSetor() {
	}

	@Override
	public Integer getId() {
		return id;
	}

	public PublicoAlvo getPublicoAlvo() {
		return publicoAlvo;
	}

	public Setor getSetor() {
		return setor;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPublicoAlvo(PublicoAlvo publicoAlvo) {
		this.publicoAlvo = publicoAlvo;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

}