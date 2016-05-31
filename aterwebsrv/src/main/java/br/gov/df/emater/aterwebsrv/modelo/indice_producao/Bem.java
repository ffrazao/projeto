package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

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

@Entity
@Table(name = "bem", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class Bem extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<Bem> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "bem_classificacao_id")
	private BemClassificacao bemClassificacao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	public Bem() {
		super();
	}

	public Bem(Integer id, String nome, BemClassificacao bemClassificacao) {
		this(id);
		setNome(nome);
		setBemClassificacao(bemClassificacao.infoBasica());
	}

	public Bem(Serializable id) {
		super(id);
	}

	public BemClassificacao getBemClassificacao() {
		return bemClassificacao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public Bem infoBasica() {
		return new Bem(getId(), getNome(), getBemClassificacao());
	}

	public void setBemClassificacao(BemClassificacao bemClassificacao) {
		this.bemClassificacao = bemClassificacao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}