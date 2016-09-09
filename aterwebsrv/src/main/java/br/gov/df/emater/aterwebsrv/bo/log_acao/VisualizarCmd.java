package br.gov.df.emater.aterwebsrv.bo.log_acao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.LogAcaoDao;
import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.sistema.LogAcao;

@Service("LogAcaoVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private LogAcaoDao dao;

	@PersistenceContext(unitName = EntidadeBase.PERSISTENCE_UNIT)
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		LogAcao logAcao = dao.findOne(id);

		if (logAcao == null) {
			throw new BoException("Registro não localizado");
		}

		em.detach(logAcao);

		LogAcao result = logAcao;

		contexto.setResposta(result);

		return false;
	}
}