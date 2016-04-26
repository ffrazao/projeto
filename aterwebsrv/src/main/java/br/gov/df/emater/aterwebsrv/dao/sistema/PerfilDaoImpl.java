package br.gov.df.emater.aterwebsrv.dao.sistema;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.modelo.dto.PerfilCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dto.TagDto;

public class PerfilDaoImpl implements PerfilDaoCustom {

	@PersistenceContext
	private EntityManager em;

	public PerfilDaoImpl() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> filtrar(PerfilCadFiltroDto filtro) {
		// objetos de trabalho
		List<Object[]> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql, sqlTemp;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select distinct a.id").append("\n");
		sql.append("              , a.nome").append("\n");
		sql.append("              , a.codigo").append("\n");
		sql.append("              , a.ativo").append("\n");
		sql.append("from            sistema.perfil a").append("\n");
		sql.append("left join       sistema.perfil_funcionalidade_comando b").append("\n");
		sql.append("on              b.perfil_id = a.id").append("\n");
		sql.append("left join       sistema.funcionalidade_comando c").append("\n");
		sql.append("on              c.id = b.funcionalidade_comando_id").append("\n");
		sql.append("left join       sistema.funcionalidade d").append("\n");
		sql.append("on              d.id = c.funcionalidade_id").append("\n");
		sql.append("left join       sistema.comando e").append("\n");
		sql.append("on              e.id = c.comando_id").append("\n");
		sql.append("where (1 = 1)").append("\n");
		if (!CollectionUtils.isEmpty(filtro.getPerfilNome())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (TagDto nome : filtro.getPerfilNome()) {
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
		if (!CollectionUtils.isEmpty(filtro.getPerfilCodigo())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (TagDto nome : filtro.getPerfilCodigo()) {
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
		if (!CollectionUtils.isEmpty(filtro.getFuncionalidadeNome())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (TagDto nome : filtro.getFuncionalidadeNome()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				String n = nome.getText().replaceAll("\\s", "%");
				params.add(String.format("%%%s%%", n));
				sqlTemp.append(" (d.nome like ?").append(params.size()).append(")").append("\n");
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}
		if (!CollectionUtils.isEmpty(filtro.getComandoNome())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (TagDto nome : filtro.getComandoNome()) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				String n = nome.getText().replaceAll("\\s", "%");
				params.add(String.format("%%%s%%", n));
				sqlTemp.append(" (e.nome like ?").append(params.size()).append(")").append("\n");
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