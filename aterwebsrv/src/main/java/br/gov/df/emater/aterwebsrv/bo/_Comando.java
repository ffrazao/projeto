package br.gov.df.emater.aterwebsrv.bo;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo._LogInclusaoAlteracao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

public abstract class _Comando implements Command {

	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private UsuarioDao usuarioDao;

	@SuppressWarnings({ "rawtypes" })
	public void logAtualizar(EntidadeBase entidadeBase, _Contexto contexto) {
		if (!(entidadeBase instanceof _LogInclusaoAlteracao) || !(entidadeBase instanceof _ChavePrimaria)) {
			return;
		}
		_LogInclusaoAlteracao result = (_LogInclusaoAlteracao) entidadeBase;
		if (((_ChavePrimaria) result).getId() == null) {
			result.setInclusaoUsuario(getUsuario(contexto.getUsuario() == null ? null : contexto.getUsuario().getName()));
		} else {
			result.setInclusaoUsuario(getUsuario(result.getInclusaoUsuario() == null ? null : result.getInclusaoUsuario().getUsername()));
		}
		result.setAlteracaoUsuario(getUsuario(contexto.getUsuario() == null ? null : contexto.getUsuario().getName()));
	}

	public abstract boolean executar(_Contexto contexto) throws Exception;

	@Override
	public boolean execute(Context contexto) throws Exception {
		return executar((_Contexto) contexto);
	}

	public Usuario getUsuario(String userName) {
		if (userName == null) {
			return null;
		}
		return usuarioDao.findByUsername(userName);
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

}
