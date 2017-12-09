package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;

@Entity
@Table(name = "ipa_producao_bem_classificado", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class IpaProducaoBemClassificado extends EntidadeBase{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "ipa_producao_id")
	private IpaProducao ipaProducao;
	
	@ManyToOne
	@JoinColumn(name = "bem_classificado_id")
	private BemClassificado bemClassificado;
	
	private Float produtividade;
	
	private Float producao;
	
	@Column(name="qtd_produtores")
	private Integer quantidadeProdutores;
	
	@Column(name="valor_unitario")
	private Float valorUnitario;
	
	@Column(name="valor_total")
	private Float valorTotal;	

	public IpaProducaoBemClassificado() {
		super();
	}

	public IpaProducaoBemClassificado(Integer id, IpaProducao ipaProducao, BemClassificado bemClassificado,
			Float produtividade, Float producao, Float valorUnitario, Float valorTotal) {
		super();
		this.id = id;
		this.ipaProducao = ipaProducao;
		this.bemClassificado = bemClassificado;
		this.produtividade = produtividade;
		this.producao = producao;
		this.valorUnitario = valorUnitario;
		this.valorTotal = valorTotal;
	}



	public IpaProducaoBemClassificado(IpaProducaoBemClassificado pbc) {
		this.id = pbc.getId();
		//this.ipaProducao = pbc.getIpaProducao();
		this.bemClassificado = pbc.getBemClassificado();
		this.produtividade = pbc.getProdutividade();
		this.producao = pbc.getProducao();
		this.valorUnitario = pbc.getValorUnitario();
		this.valorTotal = pbc.getValorTotal();
	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public IpaProducao getIpaProducao() {
		return ipaProducao;
	}



	public void setIpaProducao(IpaProducao ipaProducao) {
		this.ipaProducao = ipaProducao;
	}



	public BemClassificado getBemClassificado() {
		return bemClassificado;
	}



	public void setBemClassificado(BemClassificado bemClassificado) {
		this.bemClassificado = bemClassificado;
	}



	public Float getProdutividade() {
		return produtividade;
	}



	public void setProdutividade(Float produtividade) {
		this.produtividade = produtividade;
	}



	public Float getProducao() {
		return producao;
	}



	public void setProducao(Float producao) {
		this.producao = producao;
	}



	public Float getValorUnitario() {
		return valorUnitario;
	}



	public void setValorUnitario(Float valorUnitario) {
		this.valorUnitario = valorUnitario;
	}



	public Float getValorTotal() {
		return valorTotal;
	}



	public void setValorTotal(Float valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Integer getQuantidadeProdutores() {
		return quantidadeProdutores;
	}

	public void setQuantidadeProdutores(Integer quantidadeProdutores) {
		this.quantidadeProdutores = quantidadeProdutores;
	}


}
