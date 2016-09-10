package br.gov.df.emater.aterwebsrv.dao.atividade;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;

public class AtividadeChaveSisaterDaoImpl implements AtividadeChaveSisaterDaoCustom {

	@Autowired
	private EntityManager em;

	@Override
	public List<Integer> findAtividadePorChaveSisater(String base) {
		// objetos de trabalho
		List<Integer> result = null;

		// construção do sql
		StringBuilder sql = new StringBuilder();
		sql.append("select a.atividade.id").append("\n");
		sql.append("from AtividadeChaveSisater a").append("\n");
		sql.append("where a.chaveSisater like ?1").append("\n");

		// criar a query
		TypedQuery<Integer> query = em.createQuery(sql.toString(), Integer.class);

		// inserir os parametros
		query.setParameter(1, base);

		// executar a consulta
		result = query.getResultList();

		// retornar
		return result;
	}

}