package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;

@Service("IndiceProducaoVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private ProducaoDao dao;

	@Autowired
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		Producao result = dao.findOne(id);

		if (result == null) {
			throw new BoException("Registro não localizado");
		}

		em.detach(result);
		contexto.setResposta(result);

		return false;
	}
}