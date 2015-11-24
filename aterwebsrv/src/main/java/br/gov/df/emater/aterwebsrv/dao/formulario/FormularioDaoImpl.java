package br.gov.df.emater.aterwebsrv.dao.formulario;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import br.gov.df.emater.aterwebsrv.modelo.dto.FiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dto.FormularioCadFiltroDto;

public class FormularioDaoImpl implements FormularioDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> filtrar(FormularioCadFiltroDto filtro) {
		// objetos de trabalho
		List<Object[]> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql, sqlTemp;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select f").append("\n");
		sql.append("from Formulario f").append("\n");
		sql.append("where 1 = 1").append("\n");
		if (!StringUtils.isEmpty(filtro.getNome())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (String nome : filtro.getNome().split(FiltroDto.SEPARADOR_CAMPO)) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				params.add(String.format("%%%s%%", nome.trim()));
				sqlTemp.append(" (f.nome like ?").append(params.size()).append(")").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}

		sql.append("order by f.nome").append("\n");

		// criar a query
		TypedQuery<Object[]> query = em.createQuery(sql.toString(), Object[].class);

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