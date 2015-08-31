package br.gov.df.emater.aterwebsrv.rest;

public class Resposta {

	private String mensagem;

	private Object resultado;

	public Resposta() {
	}

	public Resposta(Object resultado) {
		this.resultado = resultado;
	}

	public Resposta(Exception resultado) {
		this.resultado = resultado;
	}

	public Resposta(String mensagem) {
		this.mensagem = mensagem;
	}

	public Resposta(String mensagem, Object resultado) {
		this.mensagem = mensagem;
	}

	public String getMensagem() {
		return mensagem;
	}

	public Object getResultado() {
		return resultado;
	}

	public void setResultado(Object resultado) {
		this.resultado = resultado;
	}

}
