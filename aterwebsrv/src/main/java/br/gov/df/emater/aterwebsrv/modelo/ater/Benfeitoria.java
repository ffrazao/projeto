package br.gov.df.emater.aterwebsrv.modelo.ater;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.rest.json.JsonFormatarBigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * The persistent class for the benfeitoria database table.
 * 
 */
@Entity
@Table(name = "benfeitoria", schema = EntidadeBase.ATER_SCHEMA)
@Indexed
public class Benfeitoria extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Field(index = Index.YES, store = Store.YES)
	private String caracteristica;

	@Field(index = Index.YES, store = Store.YES)
	private String especificacao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "propriedade_rural_id")
	private PropriedadeRural propriedadeRural;

	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal quantidade;

	private String unidade;

	@Column(name = "valor_total")
	@NumberFormat(style = Style.CURRENCY)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal valorTotal;

	@Column(name = "valor_unitario")
	@NumberFormat(style = Style.CURRENCY)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal valorUnitario;

	public Benfeitoria() {
	}

	public String getCaracteristica() {
		return caracteristica;
	}

	public String getEspecificacao() {
		return especificacao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public PropriedadeRural getPropriedadeRural() {
		return propriedadeRural;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public String getUnidade() {
		return unidade;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setCaracteristica(String caracteristica) {
		this.caracteristica = caracteristica;
	}

	public void setEspecificacao(String especificacao) {
		this.especificacao = especificacao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPropriedadeRural(PropriedadeRural propriedadeRural) {
		this.propriedadeRural = propriedadeRural;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

}