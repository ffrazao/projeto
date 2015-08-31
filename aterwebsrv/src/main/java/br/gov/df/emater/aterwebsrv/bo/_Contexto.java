package br.gov.df.emater.aterwebsrv.bo;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.chain.impl.ContextBase;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class _Contexto extends ContextBase {

	private static final String ACAO = "acao";

	private static final String ERRO = "erro";

	private static final String REQUISICAO = "requisicao";

	private static final String RESPOSTA = "resposta";

	private static final long serialVersionUID = 1L;

	public static final String USUARIO = "usuario";

	public _Contexto(Principal usuario, String acao) {
		this(usuario, acao, (Map) null);
	}

	public _Contexto(Principal usuario, String acao, Map map) {
		super(map == null ? new HashMap<String, Object>() : map);
		setUsuario(usuario);
		setAcao(acao);
	}

	public _Contexto(Principal usuario, String acao, Object requisicao) {
		setUsuario(usuario);
		setAcao(acao);
		setRequisicao(requisicao);
	}

	public String getAcao() {
		return (String) super.get(ACAO);
	}

	public Object getErro() {
		return super.get(ERRO);
	}

	public Object getRequisicao() {
		return super.get(REQUISICAO);
	}

	public Object getResposta() {
		return super.get(RESPOSTA);
	}

	public Principal getUsuario() {
		return (Principal) super.get(USUARIO);
	}

	public void setAcao(String erro) {
		super.putIfAbsent(ACAO, erro);
	}

	public void setErro(Object erro) {
		super.putIfAbsent(ERRO, erro);
	}

	public void setRequisicao(Object requisicao) {
		super.putIfAbsent(REQUISICAO, requisicao);
	}

	public void setResposta(Object resposta) {
		super.putIfAbsent(RESPOSTA, resposta);
	}

	public void setUsuario(Principal erro) {
		super.putIfAbsent(USUARIO, erro);
	}

}