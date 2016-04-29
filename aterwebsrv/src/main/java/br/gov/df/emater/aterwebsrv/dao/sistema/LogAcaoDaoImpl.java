package br.gov.df.emater.aterwebsrv.dao.sistema;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.modelo.dto.LogAcaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dto.TagDto;

public class LogAcaoDaoImpl implements LogAcaoDaoCustom {

	@PersistenceContext
	private EntityManager em;

	public LogAcaoDaoImpl() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> filtrar(LogAcaoCadFiltroDto filtro) {
		// objetos de trabalho
		List<Object[]> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql, sqlTemp;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select                a.id").append("\n");
		sql.append("                    , a.nome_usuario").append("\n");
		sql.append("                    , a.data").append("\n");
		sql.append("                    , a.comando_chain").append("\n");
		sql.append("from sistema.log_acao a").append("\n");
		sql.append("where (1 = 1)").append("\n");
		if (!CollectionUtils.isEmpty(filtro.getUsuario())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (TagDto nome : filtro.getUsuario()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				String n = nome.getText().replaceAll("\\s", "%");
				params.add(String.format("%%%s%%", n));
				sqlTemp.append(" (a.nome_usuario like ?").append(params.size()).append(")").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}
		if (filtro.getInicio() != null) {
			params.add(filtro.getInicio());
			sql.append("and a.data >= ?").append(params.size()).append("\n");
		}
		if (filtro.getTermino() != null) {
			params.add(filtro.getTermino());
			sql.append("and a.data <= ?").append(params.size()).append("\n");
		}
		sql.append("order by              a.nome_usuario").append("\n");
		sql.append("                    , a.data desc").append("\n");
		sql.append("                    , a.comando_chain").append("\n");

		// criar a query
		Query query = em.createNativeQuery(sql.toString());

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