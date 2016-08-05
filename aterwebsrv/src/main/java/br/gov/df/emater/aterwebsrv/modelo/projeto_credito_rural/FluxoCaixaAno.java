package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import java.math.BigDecimal;

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
@Table(name = "fluxo_caixa_ano", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class FluxoCaixaAno extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<FluxoCaixaAno> {

	private static final long serialVersionUID = 1L;

	private Integer ano;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "projeto_credito_rural_fluxo_caixa_id")
	private ProjetoCreditoRuralFluxoCaixa projetoCreditoRuralFluxoCaixa;

	@Column(name = "valor")
	private BigDecimal valor;

	public FluxoCaixaAno() {
		super();
	}

	public FluxoCaixaAno(Integer id) {
		super(id);
	}

	public FluxoCaixaAno(Integer ano, Integer id, BigDecimal valor) {
		super();
		this.ano = ano;
		this.id = id;
		this.valor = valor;
	}

	public Integer getAno() {
		return ano;
	}

	public Integer getId() {
		return id;
	}

	public ProjetoCreditoRuralFluxoCaixa getProjetoCreditoRuralFluxoCaixa() {
		return projetoCreditoRuralFluxoCaixa;
	}

	public BigDecimal getValor() {
		return valor;
	}

	@Override
	public FluxoCaixaAno infoBasica() {
		return new FluxoCaixaAno(this.ano, this.id, this.valor);
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setProjetoCreditoRuralFluxoCaixa(ProjetoCreditoRuralFluxoCaixa projetoCreditoRuralFluxoCaixa) {
		this.projetoCreditoRuralFluxoCaixa = projetoCreditoRuralFluxoCaixa;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}