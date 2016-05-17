package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PendenciaCodigo {

	CNPJ(PendenciaTipo.E), CPF(PendenciaTipo.E), INSCRICAO_ESTADUAL(PendenciaTipo.E), NIS(PendenciaTipo.A), RG(PendenciaTipo.A), TELEFONE(PendenciaTipo.A), EMAIL(PendenciaTipo.A);

	private PendenciaTipo tipo;

	PendenciaCodigo(PendenciaTipo tipo) {
		this.tipo = tipo;
	}

	public PendenciaTipo getTipo() {
		return tipo;
	}

}
