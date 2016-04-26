package br.gov.df.emater.aterwebsrv.bo.perfil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.PerfilDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Perfil;

@Service("PerfilExcluirCmd")
public class ExcluirCmd extends _Comando {

	@Autowired
	private PerfilDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Perfil perfil = (Perfil) contexto.getRequisicao();

		dao.delete(perfil);

		contexto.setResposta(perfil);
		return false;
	}

}