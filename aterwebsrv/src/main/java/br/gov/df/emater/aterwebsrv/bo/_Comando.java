package br.gov.df.emater.aterwebsrv.bo;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

public abstract class _Comando implements Command {
	
	protected final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private UsuarioDao usuarioDao;

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
