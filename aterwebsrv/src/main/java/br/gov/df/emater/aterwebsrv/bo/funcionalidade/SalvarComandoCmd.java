package br.gov.df.emater.aterwebsrv.bo.funcionalidade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.ComandoDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Comando;

@Service("FuncionalidadeSalvarComandoCmd")
public class SalvarComandoCmd extends _Comando {

	@Autowired
	private ComandoDao dao;

	public SalvarComandoCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Comando result = (Comando) contexto.getRequisicao();

		dao.saveAndFlush(result);

		contexto.setResposta(result.getId());
		return false;
	}
}