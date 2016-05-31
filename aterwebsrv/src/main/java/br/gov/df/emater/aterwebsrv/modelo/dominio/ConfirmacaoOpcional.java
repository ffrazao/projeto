package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum ConfirmacaoOpcional {

	N("Não"), O("Opcional"), S("Sim");

	private String descricao;

	private ConfirmacaoOpcional(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}