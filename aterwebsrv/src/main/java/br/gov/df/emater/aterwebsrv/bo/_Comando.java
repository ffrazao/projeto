package br.gov.df.emater.aterwebsrv.bo;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.beans.factory.annotation.Autowired;

import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

public abstract class _Comando implements Command {

	@Autowired
	private UsuarioDao usuarioDao;

	public abstract boolean executar(_Contexto contexto) throws Exception;

	@Override
	public boolean execute(Context contexto) throws Exception {
		return executar((_Contexto) contexto);
	}

	public Usuario getUsuario(String userName) {
		return usuarioDao.findByUsername(userName);
	}

}
