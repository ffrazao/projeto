package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum EstadoCivil {

	S("Solteiro(a)", "Nunca se casou, ou teve o casamento anulado", 1), 
	U("União Estável", "Relação de convivência entre dois cidadãos que é duradoura e estabelecida com o objetivo de constituição familiar", 2), 
	C("Casado(a)", "Contraiu matrimônio, independente do regime de bens adotado", 3), 
	P("Separado(a)/Desquitado(a)", "Vínculo jurídico do casamento existe, mas foi dissolvida a sociedade conjugal por escritura pública ou decisão judicial", 4), 
	D("Divorsiado(a)", "Após a homologação do divórcio pela justiça ou por uma escritura pública", 5), 
	V("Viúvo(a)", "Cujo cônjuge está falecido", 6);

	private String descricao;
	
	private String explicacao;
	
	public String getExplicacao() {
		return explicacao;
	}

	private Integer ordem;

	private EstadoCivil(String descricao, String explicacao, Integer ordem) {
		this.descricao = descricao;
		this.explicacao = explicacao;
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
