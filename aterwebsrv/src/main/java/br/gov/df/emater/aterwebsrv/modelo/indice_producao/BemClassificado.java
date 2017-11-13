package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import java.io.Serializable;

import javax.persistence.Column;
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
@Table(name = "bem_classificado", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class BemClassificado extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<BemClassificado> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "bem_classificacao_id")
	private BemClassificacao bemClassificacao;

	private String formula;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	@Column(name = "produtividade_max")
	private Integer produtividadeMax;

	@Column(name = "produtividade_min")
	private Integer produtividadeMin;
	                

	private String tipo;

	
	public BemClassificado() {
		super();
	}

	public BemClassificado(Integer id, String nome, BemClassificacao bemClassificacao, String tipo, String formula, Integer produtividadeMin, Integer produtividadeMax) {
		this(id);
		setNome(nome);
		setBemClassificacao(bemClassificacao);
		setTipo(tipo);
		setFormula(formula);
		setProdutividadeMin(produtividadeMin);
		setProdutividadeMax(produtividadeMax);
	}

	public BemClassificado(Serializable id) {
		super(id);
	}

	public BemClassificacao getBemClassificacao() {
		return bemClassificacao;
	}

	public String getFormula() {
		return formula;
	}

	@Override
	public Integer getId() {
		return id;
	}

	
	public String getNome() {
		return nome;
	}

	public Integer getProdutividadeMax() {
		return produtividadeMax;
	}

	public Integer getProdutividadeMin() {
		return produtividadeMin;
	}

	public String getTipo() {
		return tipo;
	}

	@Override
	public BemClassificado infoBasica() {
		return new BemClassificado(this.id, this.nome, this.bemClassificacao != null ? new BemClassificacao(this.bemClassificacao.getId()) : null, this.tipo, this.formula, this.produtividadeMin, this.produtividadeMax);
	}

	public void setBemClassificacao(BemClassificacao bemClassificacao) {
		this.bemClassificacao = bemClassificacao;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setProdutividadeMax(Integer produtividadeMax) {
		this.produtividadeMax = produtividadeMax;
	}

	public void setProdutividadeMin(Integer produtividadeMin) {
		this.produtividadeMin = produtividadeMin;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}