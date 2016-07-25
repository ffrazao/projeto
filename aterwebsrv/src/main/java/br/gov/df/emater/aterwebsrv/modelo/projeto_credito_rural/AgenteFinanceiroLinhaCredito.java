package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

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
@Table(name = "agente_financeiro_linha_credito", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class AgenteFinanceiroLinhaCredito extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "agente_financeiro_id")
	private AgenteFinanceiro agenteFinanceiro;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "linha_credito_id")
	private LinhaCredito linhaCredito;

	public AgenteFinanceiroLinhaCredito() {
		super();
	}

	public AgenteFinanceiroLinhaCredito(Integer id) {
		super(id);
	}

	public AgenteFinanceiro getAgenteFinanceiro() {
		return agenteFinanceiro;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public LinhaCredito getLinhaCredito() {
		return linhaCredito;
	}

	public void setAgenteFinanceiro(AgenteFinanceiro agenteFinanceiro) {
		this.agenteFinanceiro = agenteFinanceiro;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	public void setLinhaCredito(LinhaCredito linhaCredito) {
		this.linhaCredito = linhaCredito;
	}

}