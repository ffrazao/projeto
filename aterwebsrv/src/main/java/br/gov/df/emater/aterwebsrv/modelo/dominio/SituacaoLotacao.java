package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum SituacaoLotacao {
	/*
	 * Situa��o da lota��o:
	 * 
	 * T - Transit�ria. Saindo de um indo para outra E - Efetiva. Tornando uma
	 * Transit�ria em efetiva. P - Provis�ria. Atividades provis�rias em outras
	 * unidades da institui��o.
	 */

	E("Efetiva"), P("Provis�ria"), T("Transit�ria");

	private String descricao;

	private SituacaoLotacao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}