package br.gov.df.emater.aterwebsrv.bo.funcionalidade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.FuncionalidadeDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Funcionalidade;

@Service("FuncionalidadeExcluirCmd")
public class ExcluirCmd extends _Comando {

	@Autowired
	private FuncionalidadeDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Funcionalidade funcionalidade = (Funcionalidade) contexto.getRequisicao();

		dao.delete(funcionalidade);

		contexto.setResposta(funcionalidade);
		return false;
	}

}