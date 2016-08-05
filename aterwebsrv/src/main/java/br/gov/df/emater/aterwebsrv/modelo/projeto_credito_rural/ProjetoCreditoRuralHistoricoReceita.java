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

@Entity
@Table(name = "projeto_credito_rural_historico_receita", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class ProjetoCreditoRuralHistoricoReceita extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	private String descricao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "projeto_credito_rural_id")
	private ProjetoCreditoRural projetoCreditoRural;

	@Column(name = "receita_ano_1")
	private BigDecimal receitaAno1;

	@Column(name = "receita_ano_2")
	private BigDecimal receitaAno2;

	@Column(name = "receita_ano_3")
	private BigDecimal receitaAno3;

	public ProjetoCreditoRuralHistoricoReceita() {
		super();
	}

	public ProjetoCreditoRuralHistoricoReceita(Integer id) {
		super(id);
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public ProjetoCreditoRural getProjetoCreditoRural() {
		return projetoCreditoRural;
	}

	public BigDecimal getReceitaAno1() {
		return receitaAno1;
	}

	public BigDecimal getReceitaAno2() {
		return receitaAno2;
	}

	public BigDecimal getReceitaAno3() {
		return receitaAno3;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setProjetoCreditoRural(ProjetoCreditoRural projetoCreditoRural) {
		this.projetoCreditoRural = projetoCreditoRural;
	}

	public void setReceitaAno1(BigDecimal receitaAno1) {
		this.receitaAno1 = receitaAno1;
	}

	public void setReceitaAno2(BigDecimal receitaAno2) {
		this.receitaAno2 = receitaAno2;
	}

	public void setReceitaAno3(BigDecimal receitaAno3) {
		this.receitaAno3 = receitaAno3;
	}

}