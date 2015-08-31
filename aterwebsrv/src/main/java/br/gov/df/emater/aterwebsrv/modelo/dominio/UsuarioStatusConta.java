package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum UsuarioStatusConta {

	A("Ativo"), B("Bloqueado"), I("Inativo"), R("Renovar Senha");

	private String descricao;

	private UsuarioStatusConta(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}