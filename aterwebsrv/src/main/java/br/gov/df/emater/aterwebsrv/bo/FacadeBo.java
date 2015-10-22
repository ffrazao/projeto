package br.gov.df.emater.aterwebsrv.bo;

import java.security.Principal;

import org.apache.commons.chain.Command;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.df.emater.aterwebsrv.modelo.dto.PessoaCadFiltroDto;

@Service
public class FacadeBo implements BeanFactoryAware {

	private BeanFactory beanFactory;

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

	@Transactional
	public _Contexto pessoaFiltroExecutar(Principal usuario, PessoaCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "PessoaFiltroExecutarCh", filtro);
	}

	@Transactional
	public _Contexto pessoaFiltroNovo(Principal usuario) throws Exception {
		return this._executar(usuario, "PessoaFiltroNovoCmd");
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public void rollBack() throws Exception {
		this._executar(null, "PersistenciaRollBackCmd");
	}

}