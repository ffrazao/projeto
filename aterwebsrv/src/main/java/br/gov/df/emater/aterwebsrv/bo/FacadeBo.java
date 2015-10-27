package br.gov.df.emater.aterwebsrv.bo;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.chain.Command;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.modelo.dto.PessoaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Service
public class FacadeBo implements BeanFactoryAware {

	private BeanFactory beanFactory;

	@Transactional
	private _Contexto _executar(Principal usuario, String comandoNome) throws Exception {
		return this._executar(usuario, comandoNome, null);
	}

	@Transactional
	private _Contexto _executar(Principal usuario, String comandoNome, Object requisicao) throws Exception {
		_Contexto result = new _Contexto(usuario, comandoNome, requisicao);
		this._getComando(comandoNome).execute(result);
		return result;
	}

	private Command _getComando(String comandoNome) {
		return (Command) this.beanFactory.getBean(comandoNome);
	}

	@Transactional(readOnly = true)
	public _Contexto pessoaFiltroExecutar(Principal usuario, PessoaCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "PessoaFiltroExecutarCh", filtro);
	}

	@Transactional(readOnly = true)
	public _Contexto pessoaFiltroNovo(Principal usuario) throws Exception {
		return this._executar(usuario, "PessoaFiltroNovoCmd");
	}

	@Transactional(readOnly = true)
	public _Contexto pessoaNovo(Principal usuario, PessoaTipo pessoaTipo) throws Exception {
		return this._executar(usuario, "PessoaNovoCh", pessoaTipo);
	}

	@Transactional
	public _Contexto pessoaSalvar(Principal usuario, Pessoa pessoa) throws Exception {
		return this._executar(usuario, "PessoaSalvarCh", pessoa);
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Transactional(readOnly = true)
	public _Contexto dominio(Principal usuario, String[] ent, String npk, String vpk, String order, String[] fetchs) throws Exception {
		Map<String, Object> requisicao = new HashMap<String, Object>();
		requisicao.put("entidade", ent);
		requisicao.put("nomeChavePrimaria", npk);
		requisicao.put("valorChavePrimaria", vpk);
		requisicao.put("order", order);
		requisicao.put("fetchs", fetchs);

		return this._executar(usuario, "DominioCh", requisicao);
	}

}