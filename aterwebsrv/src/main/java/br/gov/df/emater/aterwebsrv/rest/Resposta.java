package br.gov.df.emater.aterwebsrv.rest;

import java.io.Serializable;

public class Resposta implements Serializable {

	private static final String OK = "OK";

	private static final long serialVersionUID = 1L;

	private String mensagem;

	private Object resultado;

	public Resposta() {
	}

	public Resposta(Exception resultado) {
		this.resultado = resultado;
	}

	public Resposta(Object resultado) {
		this.resultado = resultado;
		this.mensagem = OK;
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