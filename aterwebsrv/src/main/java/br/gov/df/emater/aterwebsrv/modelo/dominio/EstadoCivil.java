package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum EstadoCivil {

	C("Casado"), D("Desquitado"), P("Separado"), S("Solteiro"), U("União Estável"), V("Viúvo");

	private String descricao;

	private EstadoCivil(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}
