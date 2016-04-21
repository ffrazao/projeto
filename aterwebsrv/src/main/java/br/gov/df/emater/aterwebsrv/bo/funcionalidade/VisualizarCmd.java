package br.gov.df.emater.aterwebsrv.bo.funcionalidade;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.FuncionalidadeDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Funcionalidade;

@Service("FuncionalidadeVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private FuncionalidadeDao dao;

	@Autowired
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		Funcionalidade result = dao.findOne(id);

		if (result == null) {
			throw new BoException("Registro não localizado");
		}

		em.detach(result);
		contexto.setResposta(result);

		return false;
	}
}