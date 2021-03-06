package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.StringUtils;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.BemClassificacaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacaoFormaProducao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacaoFormaProducaoBemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacaoFormaProducaoItem;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacaoFormaProducaoValor;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoValor;

public class BemClassificacaoDaoImpl implements BemClassificacaoDaoCustom {

	@Autowired
	private EntityManager em;

	private List<Object> fetchBemClassificacao(List<Object> result, BemClassificacao bc) {
		if (bc != null) {
			List<Object> linha = new ArrayList<Object>();
			linha.add(bc.getId());
			linha.add(bc.getNome());

			List<Object[]> bemClassificacaoFormaProducaoList = null;
			if (bc.getBemClassificacaoFormaProducaoList() != null) {
				for (BemClassificacaoFormaProducao b : bc.getBemClassificacaoFormaProducaoList()) {
					bemClassificacaoFormaProducaoList = fetchBemClassificacaoFormaProducao(bemClassificacaoFormaProducaoList, b);
				}
			}

			List<Object[]> bemClassificacaoFormaProducaoItemList = null;
			if (bc.getBemClassificacaoFormaProducaoItemList() != null) {
				for (BemClassificacaoFormaProducaoItem b : bc.getBemClassificacaoFormaProducaoItemList()) {
					bemClassificacaoFormaProducaoItemList = fetchBemClassificacaoFormaProducaoItem(bemClassificacaoFormaProducaoItemList, b);
				}
			}
			if (bc.getBemClassificacaoFormaProducaoValorList() != null) {
				bemClassificacaoFormaProducaoItemList = fetchBemClassificacaoFormaProducaoValor(bemClassificacaoFormaProducaoItemList, bc.getBemClassificacaoFormaProducaoValorList());
			}
			linha.add(bemClassificacaoFormaProducaoItemList);

			List<Object> bemClassificacaoList = null;
			if (bc.getBemClassificacaoList() != null) {
				for (BemClassificacao b : bc.getBemClassificacaoList()) {
					bemClassificacaoList = fetchBemClassificacao(bemClassificacaoList, b);
				}
			}
			linha.add(bemClassificacaoList);

//			if (bc.getUnidadeMedida() != null) {
//				linha.add(bc.getUnidadeMedida().getNome());
//			} else {
//				linha.add(null);
//			}
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
			List<Object[]> bemClassificacaoFormaProducaoValorList = null;
			Object[] formaProducaoItem = null;
			if (bc.getBemClassificacaoFormaProducaoValorList() != null) {
				for (BemClassificacaoFormaProducaoValor bcfpvl : bc.getBemClassificacaoFormaProducaoValorList()) {
					if (formaProducaoItem == null) {
						formaProducaoItem = new Object[] { bcfpvl.getFormaProducaoValor().getFormaProducaoItem().getId(), bcfpvl.getFormaProducaoValor().getFormaProducaoItem().getNome() };
					}
					if (bemClassificacaoFormaProducaoValorList == null) {
						bemClassificacaoFormaProducaoValorList = new ArrayList<Object[]>();
					}
					bemClassificacaoFormaProducaoValorList.add(new Object[] { bcfpvl.getFormaProducaoValor().getId(), bcfpvl.getFormaProducaoValor().getNome() });
				}
			}
			linha.add(formaProducaoItem == null ? null : new Object[] { formaProducaoItem, bemClassificacaoFormaProducaoValorList });
			
			
/*			if( bc.getBemClassificacaoFormaProducaoList() != null){
				for (BemClassificacaoFormaProducao bcfp : bc.getBemClassificacaoFormaProducaoList()) {
					linha.add( bcfp );
				}
			}
			*/
			

			if (result == null) {
				result = new ArrayList<Object>();
			}
			result.add(linha.toArray());
		}
		return result;
	}

	
	private List<Object[]> fetchBemClassificacaoFormaProducao(List<Object[]> result, BemClassificacaoFormaProducao bc) {
		if (bc != null) {
			List<Object> linha = new ArrayList<Object>();

			List<Object[]> bemClassificacaoFormaProducaoBemClassificadoList = new ArrayList<Object[]>();
			for (BemClassificacaoFormaProducaoBemClassificado fpb : bc.getBemClassificacaoFormaProducaoBemClassificadoList()) {
				List<Object> linhaSub = new ArrayList<Object>();
				linhaSub.add(fpb.getId());
				linhaSub.add(fpb.getBemClassificado() );
				linhaSub.add(fpb.getFormula() );
				bemClassificacaoFormaProducaoBemClassificadoList.add(linhaSub.toArray());
			}
			linha.add(bemClassificacaoFormaProducaoBemClassificadoList);

			if (result == null) {
				result = new ArrayList<Object[]>();
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

	private List<Object[]> fetchBemClassificacaoFormaProducaoValor(List<Object[]> result, List<BemClassificacaoFormaProducaoValor> bc) {
		if (bc != null && bc.size() > 0) {
			List<Object> linha = new ArrayList<Object>();

			List<Object> bemClassificacaoFormaProducaoItemList = new ArrayList<Object>();
			bemClassificacaoFormaProducaoItemList.add(bc.get(0).getFormaProducaoValor().getFormaProducaoItem().getId());
			bemClassificacaoFormaProducaoItemList.add(bc.get(0).getFormaProducaoValor().getFormaProducaoItem().getNome());

			List<Object[]> formaProducaoValorList = new ArrayList<Object[]>();

			for (BemClassificacaoFormaProducaoValor v : bc) {
				if (!bemClassificacaoFormaProducaoItemList.get(0).equals(v.getFormaProducaoValor().getFormaProducaoItem().getId())) {
					// adicionar item
					linha.add(bemClassificacaoFormaProducaoItemList.get(0));
					linha.add(bemClassificacaoFormaProducaoItemList.get(1));
					linha.add(formaProducaoValorList);
					if (result == null) {
						result = new ArrayList<Object[]>();
					}
					result.add(linha.toArray());
					// reiniciar agrupadores
					linha = new ArrayList<Object>();
					bemClassificacaoFormaProducaoItemList = new ArrayList<Object>();
					formaProducaoValorList = new ArrayList<Object[]>();
					bemClassificacaoFormaProducaoItemList.add(v.getFormaProducaoValor().getFormaProducaoItem().getId());
					bemClassificacaoFormaProducaoItemList.add(v.getFormaProducaoValor().getFormaProducaoItem().getNome());
				}

				List<Object> linhaSub = new ArrayList<Object>();
				linhaSub.add(v.getFormaProducaoValor().getId());
				linhaSub.add(v.getFormaProducaoValor().getNome());
				formaProducaoValorList.add(linhaSub.toArray());
			}
			linha.add(bemClassificacaoFormaProducaoItemList.get(0));
			linha.add(bemClassificacaoFormaProducaoItemList.get(1));
			linha.add(formaProducaoValorList);

			if (result == null) {
				result = new ArrayList<Object[]>();
			}
			result.add(linha.toArray());
		}
		return result;
	}

	@Override
	// @Cacheable("BemClassificacao")
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
			for (String nome : filtro.getNome().split(CadFiltroDto.SEPARADOR_CAMPO)) {
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

}