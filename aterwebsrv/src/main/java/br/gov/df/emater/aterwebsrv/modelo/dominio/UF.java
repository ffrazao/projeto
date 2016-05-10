package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum UF {

	AC("Acre", 1), AL("Alagoas", 2), AM("Amazonas", 4), AP("Amapá", 3), BA("Bahia", 5), CE("Ceará", 6), DF("Distrito Federal", 7), ES("Espírito Santo", 8), GO("Goiás", 9), MA("Maranhão", 10), MG("Minas Gerais", 13), MS("Mato Grosso do Sul", 12), MT("Mato Grosso", 11), PA("Pará",
			14), PB("Paraíba", 15), PE("Pernambuco",
					17), PI("Piauí", 18), PR("Paraná", 16), RJ("Rio de Janeiro", 19), RN("Rio Grande do Norte", 20), RO("Rondônia", 22), RR("Roraima", 23), RS("Rio Grande do Sul", 21), SC("Santa Catarina", 24), SE("Sergipe", 26), SP("São Paulo", 25), TO("Tocantins", 27);

	private String descricao;

	private Integer ordem;

	private UF(String descricao, Integer ordem) {
		this.descricao = descricao;
		this.ordem = ordem;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}