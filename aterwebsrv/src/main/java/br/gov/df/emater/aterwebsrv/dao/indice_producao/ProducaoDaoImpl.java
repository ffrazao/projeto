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

	@Autowired
	private ProducaoDao dao;

	@PersistenceContext
	private EntityManager em;

	private List<Object[]> fetch(List<Producao> producaoList) {
		List<Object[]> result = null;
		if (producaoList != null) {
			for (Producao producao : producaoList) {
				List<Object> linha = new ArrayList<Object>();
				linha.add(producao.getId());
				linha.add(producao.getAno());
				linha.add(String.format("%s/%s", producao.getComunidade().getUnidadeOrganizacionalComunidadeList().get(0).getUnidadeOrganizacional().getNome(), producao.getComunidade().getNome()));
				List<Object> bemClassificacaoList = fetchBemClassificacao(producao.getBem().getBemClassificacao());
				linha.add(bemClassificacaoList.get(1));
				linha.add(producao.getBem().getNome());
				linha.add(bemClassificacaoList.get(0));
				linha.add(fetchProducaoFormaList(producao.getProducaoFormaList()));
				linha.add(fetchProdutores(producao));
				linha.add(bemClassificacaoList.get(2));
				linha.add(bemClassificacaoList.get(3));
				linha.add(bemClassificacaoList.get(4));
				linha.add(bemClassificacaoList.get(5));
				linha.add(producao.getComunidade().getId());
				linha.add(producao.getBem().getId());
				if (result == null) {
					result = new ArrayList<Object[]>();
				}
				result.add(linha.toArray());
			}
		}
		return result;
	}

	private List<Object> fetchBemClassificacao(BemClassificacao bemClassificacao) {
		List<Object> result = null;
		if (bemClassificacao != null) {
			UnidadeMedida unidadeMedida = null;
			FormulaProduto formulaProduto = null;
			ItemNome itemA = null;
			ItemNome itemB = null;
			ItemNome itemC = null;
			List<BemClassificacao> bemClassificacaoList = new ArrayList<BemClassificacao>();

			// percorrer a classificacao ate a raiz da arvore
			BemClassificacao b = bemClassificacao;
			do {
				bemClassificacaoList.add(b);
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

			Collections.reverse(bemClassificacaoList);
			StringBuilder bemClassificacaoSb = new StringBuilder();
			for (BemClassificacao item : bemClassificacaoList) {
				if (bemClassificacaoSb.length() > 0) {
					bemClassificacaoSb.append("/");
				}
				bemClassificacaoSb.append(item.getNome());
			}
			if (result == null) {
				result = new ArrayList<Object>();
			}
			result.add(unidadeMedida != null ? unidadeMedida.getNome() : null);
			result.add(bemClassificacaoSb != null ? bemClassificacaoSb.toString() : null);
			result.add(formulaProduto != null ? formulaProduto.toString() : null);
			result.add(itemA != null ? itemA.getNome() : null);
			result.add(itemB != null ? itemB.getNome() : null);
			result.add(itemC != null ? itemC.getNome() : null);
		}
		return result;
	}

	private List<Object[]> fetchProducaoFormaList(List<ProducaoForma> producaoFormaList) {
		List<Object[]> result = null;
		if (producaoFormaList != null) {
			for (ProducaoForma producaoForma : producaoFormaList) {
				List<Object> linha = new ArrayList<Object>();
				linha.add(fetchProducaoFormaComposicaoList(producaoForma.getProducaoFormaComposicaoList()));
				linha.add(producaoForma.getVolume());
				linha.add(0);
				linha.add(producaoForma.getQuantidadeProdutores());
				linha.add(0);
				linha.add(producaoForma.getItemAValor());
				linha.add(producaoForma.getItemBValor());
				linha.add(producaoForma.getItemCValor());
				linha.add(producaoForma.getValorUnitario());
				linha.add(producaoForma.getValorTotal());
				linha.add(producaoForma.getDataConfirmacao());
				if (result == null) {
					result = new ArrayList<Object[]>();
				}
				result.add(linha.toArray());
			}
		}
		return result;
	}

	private List<Object[]> fetchProducaoFormaComposicaoList(List<ProducaoFormaComposicao> producaoFormaComposicaoList) {
		List<Object[]> result = null;
		if (producaoFormaComposicaoList != null) {
			for (ProducaoFormaComposicao producaoFormaComposicao : producaoFormaComposicaoList) {
				List<Object> linha = new ArrayList<Object>();
				linha.add(producaoFormaComposicao.getFormaProducaoValor().getFormaProducaoItem().getNome());
				linha.add(producaoFormaComposicao.getFormaProducaoValor().getNome());
				linha.add(producaoFormaComposicao.getOrdem());
				linha.add(producaoFormaComposicao.getFormaProducaoValor().getId());
				if (result == null) {
					result = new ArrayList<Object[]>();
				}
				result.add(linha.toArray());
			}
		}
		return result;
	}

	private List<Object> fetchProdutor(Producao producao) {
		List<Object> result = null;
		if (producao != null) {
			result = new ArrayList<Object>();
			result.add(producao.getPropriedadeRural() != null ? producao.getPropriedadeRural().getNome() : null);
			result.add(producao.getPublicoAlvo() != null ? producao.getPublicoAlvo().getPessoa().getNome() : null);
			result.add(fetchProducaoFormaList(producao.getProducaoFormaList()));
			result.add(producao.getId());
		}
		return result;
	}

	private List<Object[]> fetchProdutores(Producao producao) {
		List<Object[]> result = null;
		if (producao != null) {
			List<Producao> producaoList = dao.findByAnoAndBemAndPropriedadeRuralComunidade(producao.getAno(), producao.getBem(), producao.getComunidade());
			if (producaoList != null) {
				for (Producao p : producaoList) {
					if (result == null) {
						result = new ArrayList<Object[]>();
					}
					result.add(fetchProdutor(p).toArray());
				}
			}
		}
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Object[]> filtrar(IndiceProducaoCadFiltroDto filtro) {
		// ATENCAO: quando o parametro id do filtro estiver preenchido significa
		// que este método vai coletar uma producao especifica, ou seja, é
		// utilizado para recuperar os dados vinculados a uma producao principal
		// que é representada por um conjunto especifico dos valores dos
		// parametros ano e comunidade

		// objetos de trabalho
		List<Object[]> result = null;
		List<Object> params = new ArrayList<Object>();
		StringBuilder sql;// , sqlTemp;
		List<Producao> producaoList = null;

		// construção do sql
		sql = new StringBuilder();
		sql.append("select p").append("\n");
		sql.append("from Producao p").append("\n");
		if (filtro.getId() != null) {
			params.add(filtro.getId());
			sql.append("where p.id = ?").append(params.size()).append("\n");
		} else {
			sql.append("where p.propriedadeRural is null").append("\n");
			sql.append("and   p.publicoAlvo is null").append("\n");
			if (filtro.getAno() != null) {
				params.add(filtro.getAno());
				sql.append("and p.ano = ?").append(params.size()).append("\n");
			}
			// TODO implementar o restante do filtro
		}
		sql.append("order by p.ano").append("\n");

		// criar a query
		TypedQuery<Producao> query = em.createQuery(sql.toString(), Producao.class);

		// inserir os parametros
		for (int i = 1; i <= params.size(); i++) {
			query.setParameter(i, params.get(i - 1));
		}

		// definir a pagina a ser consultada
		if (filtro.getId() == null) {
			filtro.configuraPaginacao(query);
		}

		// executar a consulta
		producaoList = query.getResultList();

		// fetch no resultado
		if (filtro.getId() != null) {
			if (producaoList != null && producaoList.size() == 1) {
				result = new ArrayList<Object[]>();
				result.add(fetchProdutor(producaoList.get(0)).toArray());
			}
		} else {
			result = fetch(producaoList);
		}

		// retornar
		return result;
	}

}