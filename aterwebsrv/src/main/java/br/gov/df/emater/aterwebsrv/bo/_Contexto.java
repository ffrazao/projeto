package br.gov.df.emater.aterwebsrv.bo;

import java.security.Principal;

import org.apache.commons.chain.impl.ContextBase;

public class _Contexto extends ContextBase {

	private static final long serialVersionUID = 1L;

	private String acao;

	private Object erro;

	private Object requisicao;

	private Object resposta;

	public Principal usuario;

	public _Contexto(Principal usuario, String acao, Object requisicao) {
		setUsuario(usuario);
		setAcao(acao);
		setRequisicao(requisicao);
	}

	public String getAcao() {
		return this.acao;
	}

	public Object getErro() {
		return this.erro;
	}

	public Object getRequisicao() {
		return requisicao;
	}

	public Object getResposta() {
		return resposta;
	}

	public Principal getUsuario() {
		return usuario;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public void setErro(Object erro) {
		this.erro = erro;
	}

	public void setRequisicao(Object requisicao) {
		this.requisicao = requisicao;
	}

	public void setResposta(Object resposta) {
		this.resposta = resposta;
	}

	public void setUsuario(Principal usuario) {
		this.usuario = usuario;
	}

}