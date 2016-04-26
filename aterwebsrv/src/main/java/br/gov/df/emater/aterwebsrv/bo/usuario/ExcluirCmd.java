package br.gov.df.emater.aterwebsrv.bo.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service("UsuarioExcluirCmd")
public class ExcluirCmd extends _Comando {

	@Autowired
	private UsuarioDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Usuario usuario = (Usuario) contexto.getRequisicao();

		dao.delete(usuario);

		contexto.setResposta(usuario);
		return false;
	}

}