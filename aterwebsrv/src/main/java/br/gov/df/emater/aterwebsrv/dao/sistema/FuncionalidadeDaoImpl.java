package br.gov.df.emater.aterwebsrv.dao.sistema;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.dto.FuncionalidadeCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.TagDto;

public class FuncionalidadeDaoImpl implements FuncionalidadeDaoCustom {

	@PersistenceContext
	private EntityManager em;

	public FuncionalidadeDaoImpl() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> filtrar(FuncionalidadeCadFiltroDto filtro) {
		// objetos de trabalho
		List<Object[]> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql, sqlTemp;

		// construção do sql
		sql = new StringBuilder();

		sql.append("select distinct a.id").append("\n");
		sql.append("        , a.nome").append("\n");
		sql.append("        , a.codigo").append("\n");
		sql.append("        , a.ativo").append("\n");
		sql.append("from            sistema.funcionalidade a").append("\n");
		sql.append("left join       sistema.modulo_funcionalidade b").append("\n");
		sql.append("on              b.funcionalidade_id  = a.id").append("\n");
		sql.append("left join       sistema.modulo b1").append("\n");
		sql.append("on              b.modulo_id  = b1.id").append("\n");
		sql.append("left join       sistema.funcionalidade_comando c").append("\n");
		sql.append("on              c.funcionalidade_id  = a.id").append("\n");
		sql.append("left join       sistema.comando c1").append("\n");
		sql.append("on              c.comando_id  = c1.id").append("\n");
		sql.append("where (1 = 1)").append("\n");
		if (!CollectionUtils.isEmpty(filtro.getFuncionalidade())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (TagDto nome : filtro.getFuncionalidade()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				String n = nome.getText().replaceAll("\\s", "%");
				params.add(String.format("%%%s%%", n));
				sqlTemp.append(" (a.nome like ?").append(params.size()).append(")").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getModulo())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (TagDto nome : filtro.getModulo()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				String n = nome.getText().replaceAll("\\s", "%");
				params.add(String.format("%%%s%%", n));
				sqlTemp.append(" (b1.nome like ?").append(params.size()).append(")").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getComando())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (TagDto nome : filtro.getComando()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				String n = nome.getText().replaceAll("\\s", "%");
				params.add(String.format("%%%s%%", n));
				sqlTemp.append(" (c1.nome like ?").append(params.size()).append(")").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}
		sql.append("order by        a.nome").append("\n");

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