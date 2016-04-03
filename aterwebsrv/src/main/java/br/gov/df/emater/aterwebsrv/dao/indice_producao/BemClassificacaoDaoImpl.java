package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.util.StringUtils;

import br.gov.df.emater.aterwebsrv.modelo.dto.BemClassificacaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dto.FiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacaoFormaProducaoItem;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoValor;

public class BemClassificacaoDaoImpl implements BemClassificacaoDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Object> filtrar(BemClassificacaoCadFiltroDto filtro) {
		// objetos de trabalho
		List<Object> result = null;
		List<BemClassificacao> bemClassificacaoList = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql, sqlTemp;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select b").append("\n");
		sql.append("from BemClassificacao b").append("\n");
		sql.append("where b.bemClassificacao is null").append("\n");
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
		TypedQuery<BemClassificacao> query = em.createQuery(sql.toString(), BemClassificacao.class);

		// inserir os parametros
		for (int i = 1; i <= params.size(); i++) {
			query.setParameter(i, params.get(i - 1));
		}

		// definir a pagina a ser consultada
		filtro.configuraPaginacao(query);

		// executar a consulta
		bemClassificacaoList = query.getResultList();

		if (bemClassificacaoList != null) {
			for (BemClassificacao o : bemClassificacaoList) {
				result = fetchBemClassificacao(result, o);
			}
		}

		// retornar
		return result;
	}

	private List<Object> fetchBemClassificacao(List<Object> result, BemClassificacao bc) {
		if (bc != null) {
			List<Object> linha = new ArrayList<Object>();
			linha.add(bc.getId());
			linha.add(bc.getNome());

			List<Object[]> bemClassificacaoFormaProducaoItemList = null;
			if (bc.getBemClassificacaoFormaProducaoItemList() != null) {
				for (BemClassificacaoFormaProducaoItem b : bc.getBemClassificacaoFormaProducaoItemList()) {
					bemClassificacaoFormaProducaoItemList = fetchBemClassificacaoFormaProducaoItem(bemClassificacaoFormaProducaoItemList, b);
				}
			}
			linha.add(bemClassificacaoFormaProducaoItemList);

			List<Object> bemClassificacaoList = null;
			if (bc.getBemClassificacaoList() != null) {
				for (BemClassificacao b : bc.getBemClassificacaoList()) {
					bemClassificacaoList = fetchBemClassificacao(bemClassificacaoList, b);
				}
			}
			linha.add(bemClassificacaoList);

			if (bc.getUnidadeMedida() != null) {
				linha.add(bc.getUnidadeMedida().getNome());
			} else {
				linha.add(null);
			}
			if (bc.getItemANome() != null) {
				linha.add(bc.getItemANome().getNome());
			} else {
				linha.add(null);
			}
			if (bc.getItemBNome() != null) {
				linha.add(bc.getItemBNome().getNome());
			} else {
				linha.add(null);
			}
			if (bc.getItemCNome() != null) {
				linha.add(bc.getItemCNome().getNome());
			} else {
				linha.add(null);
			}
			if (bc.getFormula() != null) {
				linha.add(bc.getFormula().toString());
			} else {
				linha.add(null);
			}

			if (result == null) {
				result = new ArrayList<Object>();
			}
			result.add(linha.toArray());
		}
		return result;
	}

	private List<Object[]> fetchBemClassificacaoFormaProducaoItem(List<Object[]> result, BemClassificacaoFormaProducaoItem bc) {
		if (bc != null) {
			List<Object> linha = new ArrayList<Object>();
			linha.add(bc.getFormaProducaoItem().getId());
			linha.add(bc.getFormaProducaoItem().getNome());

			List<Object[]> formaProducaoValorList = new ArrayList<Object[]>();
			for (FormaProducaoValor v : bc.getFormaProducaoItem().getFormaProducaoValorList()) {
				List<Object> linhaSub = new ArrayList<Object>();
				linhaSub.add(v.getId());
				linhaSub.add(v.getNome());
				formaProducaoValorList.add(linhaSub.toArray());
			}
			linha.add(formaProducaoValorList);

			if (result == null) {
				result = new ArrayList<Object[]>();
			}
			result.add(linha.toArray());
		}
		return result;
	}

}