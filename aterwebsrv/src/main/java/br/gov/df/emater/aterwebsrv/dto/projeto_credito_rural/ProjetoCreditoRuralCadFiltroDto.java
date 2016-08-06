package br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural;

import java.util.Set;

import br.gov.df.emater.aterwebsrv.dto.atividade.AtividadeCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FinanciamentoTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ProjetoCreditoRuralStatus;

public class ProjetoCreditoRuralCadFiltroDto extends AtividadeCadFiltroDto {

	private static final long serialVersionUID = 1L;

	private Set<Integer> agenteFinanceiroIdList;

	private Set<FinanciamentoTipo> financiamentoTipoList;

	private Set<Integer> linhaCreditoIdList;

	private Set<ProjetoCreditoRuralStatus> statusList;

	public Set<Integer> getAgenteFinanceiroIdList() {
		return agenteFinanceiroIdList;
	}

	public Set<FinanciamentoTipo> getFinanciamentoTipoList() {
		return financiamentoTipoList;
	}

	public Set<Integer> getLinhaCreditoIdList() {
		return linhaCreditoIdList;
	}

	public Set<ProjetoCreditoRuralStatus> getStatusList() {
		return statusList;
	}

	public void setAgenteFinanceiroIdList(Set<Integer> agenteFinanceiroIdList) {
		this.agenteFinanceiroIdList = agenteFinanceiroIdList;
	}

	public void setFinanciamentoTipoList(Set<FinanciamentoTipo> financiamentoTipoList) {
		this.financiamentoTipoList = financiamentoTipoList;
	}

	public void setLinhaCreditoIdList(Set<Integer> linhaCreditoIdList) {
		this.linhaCreditoIdList = linhaCreditoIdList;
	}

	public void setStatusList(Set<ProjetoCreditoRuralStatus> statusList) {
		this.statusList = statusList;
	}

}