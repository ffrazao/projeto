package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FinanciamentoTipo;

@Entity
@Table(name = "projeto_credito_financiamento", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class ProjetoCreditoRuralFinanciamento extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	private String descricao;

	@Column(name = "epoca_liberacao")
	private String epocaLiberacao;

	@Column(name = "nome_lote")
	private String nomeLote;

	public String getNomeLote() {
		return nomeLote;
	}

	public void setNomeLote(String nomeLote) {
		this.nomeLote = nomeLote;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "percentual_proprio")
	private BigDecimal percentualProprio;

	@ManyToOne
	@JoinColumn(name = "projeto_credito_id")
	private ProjetoCreditoRural projetoCreditoRural;

	@Column(name = "quantidade")
	private BigDecimal quantidade;

	@Enumerated(EnumType.STRING)
	private FinanciamentoTipo tipo;

	private String unidade;

	@Column(name = "valor_financiado")
	private BigDecimal valorFinanciado;

	@Column(name = "valor_orcado")
	private BigDecimal valorOrcado;

	@Column(name = "valor_proprio")
	private BigDecimal valorProprio;

	@Column(name = "valor_unitario")
	private BigDecimal valorUnitario;

	public ProjetoCreditoRuralFinanciamento() {
		super();
	}

	public ProjetoCreditoRuralFinanciamento(Integer id) {
		super(id);
	}

	public String getDescricao() {
		return descricao;
	}

	public String getEpocaLiberacao() {
		return epocaLiberacao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public BigDecimal getPercentualProprio() {
		return percentualProprio;
	}

	public ProjetoCreditoRural getProjetoCreditoRural() {
		return projetoCreditoRural;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public FinanciamentoTipo getTipo() {
		return tipo;
	}

	public String getUnidade() {
		return unidade;
	}

	public BigDecimal getValorFinanciado() {
		return valorFinanciado;
	}

	public BigDecimal getValorOrcado() {
		return valorOrcado;
	}

	public BigDecimal getValorProprio() {
		return valorProprio;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setEpocaLiberacao(String epocaLiberacao) {
		this.epocaLiberacao = epocaLiberacao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPercentualProprio(BigDecimal percentualProprio) {
		this.percentualProprio = percentualProprio;
	}

	public void setProjetoCreditoRural(ProjetoCreditoRural projetoCreditoRural) {
		this.projetoCreditoRural = projetoCreditoRural;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public void setTipo(FinanciamentoTipo tipo) {
		this.tipo = tipo;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public void setValorFinanciado(BigDecimal valorFinanciado) {
		this.valorFinanciado = valorFinanciado;
	}

	public void setValorOrcado(BigDecimal valorOrcado) {
		this.valorOrcado = valorOrcado;
	}

	public void setValorProprio(BigDecimal valorProprio) {
		this.valorProprio = valorProprio;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

}