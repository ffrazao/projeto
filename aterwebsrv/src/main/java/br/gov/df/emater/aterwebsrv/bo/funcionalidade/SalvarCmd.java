package br.gov.df.emater.aterwebsrv.bo.funcionalidade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.FuncionalidadeDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Funcionalidade;

@Service("FuncionalidadeSalvarCmd")
public class SalvarCmd extends _Comando {

	@Autowired
	private FuncionalidadeDao dao;

	public SalvarCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Funcionalidade result = (Funcionalidade) contexto.getRequisicao();

		dao.saveAndFlush(result);

		contexto.setResposta(result.getId());
		return false;
	}
}