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
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;

@Entity
@Table(name = "projeto_credito_garantia", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class ProjetoCreditoRuralPropriedadeRural extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "projeto_credito_id")
	private ProjetoCreditoRural projetoCreditoRural;

	@ManyToOne
	@JoinColumn(name = "propriedade_rural_id")
	private PropriedadeRural propriedadeRural;

	@Column(name = "renda_liquida")
	private BigDecimal rendaLiquida;

	public ProjetoCreditoRuralPropriedadeRural() {
		super();
	}

	public ProjetoCreditoRuralPropriedadeRural(Integer id) {
		super(id);
	}

	@Override
	public Integer getId() {
		return id;
	}

	public ProjetoCreditoRural getProjetoCreditoRural() {
		return projetoCreditoRural;
	}

	public PropriedadeRural getPropriedadeRural() {
		return propriedadeRural;
	}

	public BigDecimal getRendaLiquida() {
		return rendaLiquida;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setProjetoCreditoRural(ProjetoCreditoRural projetoCreditoRural) {
		this.projetoCreditoRural = projetoCreditoRural;
	}

	public void setPropriedadeRural(PropriedadeRural propriedadeRural) {
		this.propriedadeRural = propriedadeRural;
	}

	public void setRendaLiquida(BigDecimal rendaLiquida) {
		this.rendaLiquida = rendaLiquida;
	}

}