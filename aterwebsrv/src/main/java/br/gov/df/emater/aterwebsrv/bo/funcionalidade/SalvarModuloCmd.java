package br.gov.df.emater.aterwebsrv.bo.funcionalidade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.ModuloDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Modulo;

@Service("FuncionalidadeSalvarModuloCmd")
public class SalvarModuloCmd extends _Comando {

	@Autowired
	private ModuloDao dao;

	public SalvarModuloCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Modulo result = (Modulo) contexto.getRequisicao();

		dao.save(result);

		contexto.setResposta(result.getId());
		return false;
	}
}