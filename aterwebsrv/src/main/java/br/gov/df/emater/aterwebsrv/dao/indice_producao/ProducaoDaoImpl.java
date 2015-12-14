package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.gov.df.emater.aterwebsrv.modelo.dominio.FormulaProduto;
import br.gov.df.emater.aterwebsrv.modelo.dto.IndiceProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ItemNome;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoFormaComposicao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.UnidadeMedida;

public class ProducaoDaoImpl implements ProducaoDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private ProducaoDao dao;

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> filtrar(IndiceProducaoCadFiltroDto filtro) {
		// objetos de trabalho
		List<Object[]> result = null;
		List<Producao> producaoList = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql;// , sqlTemp;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select p").append("\n");
		sql.append("from Producao p").append("\n");
		sql.append("where p.propriedadeRural is null").append("\n");
		sql.append("and   p.publicoAlvo is null").append("\n");
		sql.append("order by p.ano").append("\n");

		// criar a query
		TypedQuery<Producao> query = em.createQuery(sql.toString(), Producao.class);

		// inserir os parametros
		for (int i = 1; i <= params.size(); i++) {
			query.setParameter(i, params.get(i - 1));
		}

		// definir a pagina a ser consultada
		filtro.configuraPaginacao(query);

		// executar a consulta
		producaoList = query.getResultList();

		// fetch no resultado
		result = fetch(producaoList);

		// retornar
		return result;
	}

	private List<Object[]> fetch(List<Producao> producaoList) {
		List<Object[]> result = null;
		if (producaoList != null) {
			for (Producao producao : producaoList) {
				List<Object> linha = new ArrayList<Object>();
				linha.add(producao.getId());
				linha.add(producao.getAno());
				linha.add(String.format("%s/%s", producao.getComunidade().getUnidadeOrganizacionalComunidadeList().get(0).getUnidadeOrganizacional().getNome(), producao.getComunidade().getNome()));

				List<Object> obj = new ArrayList<Object>();
				fetchBemClassificacao(producao.getBem().getBemClassificacao(), obj);

				linha.add(obj.get(1));
				linha.add(producao.getBem().getNome());
				linha.add(obj.get(0));
				linha.add(fetchProducaoForma(producao.getProducaoFormaList()));
				// adicionar os registros coletados
				linha.add(fetchProdutores(producao));
				linha.add(obj.get(2));
				linha.add(obj.get(3));
				linha.add(obj.get(4));
				linha.add(obj.get(5));
				if (result == null) {
					result = new ArrayList<Object[]>();
				}
				result.add(linha.toArray());
			}
		}
		return result;
	}

	private List<Object[]> fetchProdutores(Producao producao) {
		List<Object[]> result = null;
		if (producao != null) {
			List<Producao> producaoList = dao.findByAnoAndBemAndPropriedadeRuralComunidade(producao.getAno(), producao.getBem(), producao.getComunidade());
			for (Producao p : producaoList) {
				List<Object> linha = new ArrayList<Object>();
				linha.add(p.getPropriedadeRural().getNome());
				linha.add(p.getPublicoAlvo().getPessoa().getNome());
				linha.add(fetchProducaoForma(p.getProducaoFormaList()));
				if (result == null) {
					result = new ArrayList<Object[]>();
				}
				result.add(linha.toArray());
			}
		}
		return result;
	}

	private List<Object[]> fetchProducaoForma(List<ProducaoForma> producaoFormaList) {
		List<Object[]> result = null;
		if (producaoFormaList != null) {
			for (ProducaoForma pf : producaoFormaList) {
				List<Object> linha = new ArrayList<Object>();
				linha.add(fetchProducaoFormaComposicao(pf.getProducaoFormaComposicaoList()));
				linha.add(pf.getVolume());
				linha.add(0);
				linha.add(pf.getQuantidadeProdutores());
				linha.add(0);
				linha.add(pf.getItemAValor());
				linha.add(pf.getItemBValor());
				linha.add(pf.getItemCValor());
				linha.add(pf.getValorUnitario());
				linha.add(pf.getValorTotal());
				linha.add(pf.getDataConfirmacao());				
				if (result == null) {
					result = new ArrayList<Object[]>();
				}
				result.add(linha.toArray());
			}
		}
		return result;
	}

	private List<Object[]> fetchProducaoFormaComposicao(List<ProducaoFormaComposicao> producaoFormaComposicaoList) {
		List<Object[]> result = null;
		if (producaoFormaComposicaoList != null) {
			for (ProducaoFormaComposicao pfc : producaoFormaComposicaoList) {
				List<Object> linha = new ArrayList<Object>();
				linha.add(pfc.getFormaProducaoValor().getFormaProducaoItem().getNome());
				linha.add(pfc.getFormaProducaoValor().getNome());
				linha.add(pfc.getOrdem());
				if (result == null) {
					result = new ArrayList<Object[]>();
				}
				result.add(linha.toArray());
			}
		}
		return result;
	}

	private void fetchBemClassificacao(BemClassificacao bemClassificacao, List<Object> obj) {
		if (bemClassificacao != null) {
			UnidadeMedida unidadeMedida = null;
			FormulaProduto formulaProduto = null;
			ItemNome itemA = null;
			ItemNome itemB = null;
			ItemNome itemC = null;
			List<BemClassificacao> lista = new ArrayList<BemClassificacao>();
			BemClassificacao b = bemClassificacao;
			do {
				lista.add(b);
				if (unidadeMedida == null && b.getUnidadeMedida() != null) {
					unidadeMedida = b.getUnidadeMedida();
				}
				if (formulaProduto == null && b.getFormula() != null) {
					formulaProduto = b.getFormula();
				}
				if (itemA == null && b.getItemANome() != null) {
					itemA = b.getItemANome();
				}
				if (itemB == null && b.getItemBNome() != null) {
					itemB = b.getItemBNome();
				}
				if (itemC == null && b.getItemCNome() != null) {
					itemC = b.getItemCNome();
				}
				b = b.getBemClassificacao();
			} while (b != null);

			Collections.reverse(lista);
			StringBuilder sb = new StringBuilder();
			for (BemClassificacao bem : lista) {
				if (sb.length() > 0) {
					sb.append("/");
				}
				sb.append(bem.getNome());
			}
			obj.add(unidadeMedida != null ? unidadeMedida.getNome() : null);
			obj.add(sb != null ? sb.toString() : null);
			obj.add(formulaProduto != null ? formulaProduto.toString() : null);
			obj.add(itemA != null ? itemA.getNome() : null);
			obj.add(itemB != null ? itemB.getNome() : null);
			obj.add(itemC != null ? itemC.getNome() : null);
		}
	}

}