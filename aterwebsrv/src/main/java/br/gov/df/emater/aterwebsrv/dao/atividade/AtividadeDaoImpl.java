package br.gov.df.emater.aterwebsrv.dao.atividade;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Transactional;

import br.gov.df.emater.aterwebsrv.modelo.dto.AtividadeCadFiltroDto;

public class AtividadeDaoImpl implements AtividadeDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional(readOnly = true)
	public List<Object> filtrar(AtividadeCadFiltroDto filtro) {
		// objetos de trabalho
		List<Object> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select a ").append("\n");
		sql.append("from Atividade a").append("\n");

		//-- até aki
		
		// criar a query
		TypedQuery<Object> query = em.createQuery(sql.toString(), Object.class);

		// inserir os parametros
		for (int i = 1; i <= params.size(); i++) {
			query.setParameter(i, params.get(i - 1));
		}

		// definir a pagina a ser consultada
		filtro.configuraPaginacao(query);

		// executar a consulta
		result = query.getResultList();

		// retornar
		return result;
	}

}