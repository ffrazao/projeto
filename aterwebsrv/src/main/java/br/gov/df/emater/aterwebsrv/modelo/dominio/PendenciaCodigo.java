package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PendenciaCodigo {

	CNPJ(PendenciaTipo.E), CPF(PendenciaTipo.E), EMAIL(PendenciaTipo.A), INSCRICAO_ESTADUAL(PendenciaTipo.E), NIS(PendenciaTipo.A), RELACIONAMENTO(PendenciaTipo.A), RG(PendenciaTipo.A), TELEFONE(PendenciaTipo.A);

	private PendenciaTipo tipo;

	PendenciaCodigo(PendenciaTipo tipo) {
		this.tipo = tipo;
	}

	public PendenciaTipo getTipo() {
		return tipo;
	}

}
