package br.gov.df.emater.aterwebsrv.bo;

import java.security.Principal;
import java.util.Map;

import org.apache.commons.chain.Command;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Contexto;

@Service
public class FacadeBoImportar implements BeanFactoryAware {

	private BeanFactory beanFactory;

	private _Contexto _executar(Principal usuario, String comandoNome) throws Exception {
		return this._executar(usuario, comandoNome, null, null);
	}

	private _Contexto _executar(Principal usuario, String comandoNome, Object requisicao, Map<Object, Object> map)
			throws Exception {
		Command comando = (Command) this.beanFactory.getBean(comandoNome);
		_Contexto result = new _Contexto(usuario, comando.getClass().getName(), requisicao);
		if (map != null) {
			result.putAll(map);
		}
		result.setUsuario(usuario);
		comando.execute(result);
		return result;
	}

	public _Contexto importar(Principal usuario) throws Exception {
		return this._executar(usuario, "ImportarCh");
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	public _Contexto sisater(Principal usuario, Map<Object, Object> map) throws Exception {
		return this._executar(usuario, "SisaterCh", null, map);
	}

	public _Contexto sisaterEmpregado(Principal usuario, Map<Object, Object> map) throws Exception {
		return this._executar(usuario, "SisaterEmpregadoCmd", null, map);
	}
}