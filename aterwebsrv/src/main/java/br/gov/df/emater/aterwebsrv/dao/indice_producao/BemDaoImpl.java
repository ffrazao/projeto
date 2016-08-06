package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.util.StringUtils;

import br.gov.df.emater.aterwebsrv.dto.BemProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.FiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;

public class BemDaoImpl implements BemDaoCustom {

	@PersistenceContext
	private EntityManager em;

	private void fetchBemClassificacao(BemClassificacao bc) {
		if (bc != null && bc.getBemClassificacao() != null) {
			fetchBemClassificacao(bc.getBemClassificacao());
		}
	}

	@Override
	public List<Object[]> filtrar(BemProducaoCadFiltroDto filtro) {
		// objetos de trabalho
		List<Object[]> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql, sqlTemp;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select b.id").append("\n");
		sql.append("     , b.nome").append("\n");
		sql.append("     , b.bemClassificacao").append("\n");
		sql.append("from Bem b").append("\n");
		sql.append("join b.bemClassificacao").append("\n");
		sql.append("where 1 = 1").append("\n");
		if (!StringUtils.isEmpty(filtro.getNome())) {
			sql.append("and (").append("\n");
			sqlTemp = new StringBuilder();
			for (String nome : filtro.getNome().split(FiltroDto.SEPARADOR_CAMPO)) {
				if (sqlTemp.length() > 0) {
					sqlTemp.append(" or ");
				}
				params.add(String.format("%%%s%%", nome.trim()));
				sqlTemp.append(" b.nome like ?").append(params.size());
			}
			sql.append(sqlTemp);
			sql.append(" )").append("\n");
		}

		sql.append("order by b.nome").append("\n");

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

		if (result != null) {
			for (Object ob : result) {
				Object[] o = (Object[]) ob;
				fetchBemClassificacao((BemClassificacao) o[2]);
			}
		}

		// retornar
		return result;
	}

}