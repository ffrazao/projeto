package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ferramenta.UtilDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioNumero;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dto.IndiceProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ItemNome;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoFormaComposicao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.UnidadeMedida;

@Service("IndiceProducaoFiltrarCmd")
public class FiltrarCmd extends _Comando {

	private class CalculoItem {

		private ProducaoForma producaoFormaTotal = new ProducaoForma();

		private Set<Integer> publicoAlvoList = new HashSet<Integer>();

	}

	private class ProducaoCalculo {

		private Map<String, CalculoItem> matriz = new HashMap<String, CalculoItem>();

		private BigDecimal acumula(BigDecimal origem, BigDecimal destino) {
			return origem == null ? null : (destino == null ? new BigDecimal("0") : destino).add(origem, UtilitarioNumero.BIG_DECIMAL_PRECISAO);
		}

		public void acumulaItem(String composicaoProducaoFormaId, ProducaoForma producaoForma, Integer publicoAlvoId, boolean confirmado) {
			CalculoItem calculo = null;

			if (matriz.containsKey(composicaoProducaoFormaId)) {
				calculo = matriz.get(composicaoProducaoFormaId);
			} else {
				calculo = new CalculoItem();
			}

			if (!confirmado || (confirmado && producaoForma.getDataConfirmacao() != null)) {
				calculo.producaoFormaTotal.setItemAValor(acumula(producaoForma.getItemAValor(), calculo.producaoFormaTotal.getItemAValor()));
				calculo.producaoFormaTotal.setItemBValor(acumula(producaoForma.getItemBValor(), calculo.producaoFormaTotal.getItemBValor()));
				calculo.producaoFormaTotal.setItemCValor(acumula(producaoForma.getItemCValor(), calculo.producaoFormaTotal.getItemCValor()));
				calculo.producaoFormaTotal.setVolume(acumula(producaoForma.getVolume(), calculo.producaoFormaTotal.getVolume()));
				calculo.producaoFormaTotal.setValorUnitario(acumula(producaoForma.getValorUnitario(), calculo.producaoFormaTotal.getValorUnitario()));
				calculo.producaoFormaTotal.setValorTotal(acumula(producaoForma.getValorTotal(), calculo.producaoFormaTotal.getValorTotal()));

				if (publicoAlvoId != null) {
					calculo.publicoAlvoList.add(publicoAlvoId);
				}
			}

			matriz.put(composicaoProducaoFormaId, calculo);
		}

		public String getComposicao(ProducaoForma producaoForma) {
			List<Integer> idList = new ArrayList<Integer>();
			for (ProducaoFormaComposicao composicao : producaoForma.getProducaoFormaComposicaoList()) {
				idList.add(composicao.getFormaProducaoValor().getId());
			}
			Collections.sort(idList);
			return UtilitarioString.collectionToString(idList);
		}

	}

	@Autowired
	private ProducaoDao dao;

	@Autowired
	private UtilDao utilDao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		IndiceProducaoCadFiltroDto filtro = (IndiceProducaoCadFiltroDto) contexto.getRequisicao();
		List<Object> result = new ArrayList<Object>();
		List<Producao> producaoList = null;
		producaoList = (List<Producao>) dao.filtrar(filtro);

		if (producaoList != null) {
			// fetch no resultado
			if (filtro.getId() != null) {
				if (producaoList != null && producaoList.size() == 1) {
					producaoList = new ArrayList<Producao>();
					// result.add(fetchProdutor(result.get(0)));
				}
			} else {

				// contabilizar a producao
				for (Producao producao : producaoList) {
					List<Object> resultProdutor = null;
					
					Map<String, Object> bemClassificacaoList = utilDao.ipaBemClassificacaoDetalhes(producao.getBem().getBemClassificacao());

					ProducaoCalculo calculoProducaoEsperada = new ProducaoCalculo();
					ProducaoCalculo calculoProducaoConfirmada = new ProducaoCalculo();

					// contabilizar os produtores da producao
					List<Producao> produtorProducaoList = dao.findByAnoAndBemAndPropriedadeRuralComunidadeUnidadeOrganizacional(producao.getAno(), producao.getBem(), producao.getUnidadeOrganizacional());
					if (produtorProducaoList != null) {
						resultProdutor = new ArrayList<Object>();
						for (Producao produtorProducao : produtorProducaoList) {

							if (produtorProducao.getProducaoFormaList() != null) {
								ProducaoCalculo calculoProdutorEsperada = new ProducaoCalculo();
								ProducaoCalculo calculoProdutorConfirmada = new ProducaoCalculo();
								for (ProducaoForma producaoForma : produtorProducao.getProducaoFormaList()) {
									String composicao = calculoProdutorEsperada.getComposicao(producaoForma);
									Integer publicoAlvoId = produtorProducao.getPublicoAlvo().getId();

									calculoProdutorEsperada.acumulaItem("", producaoForma, publicoAlvoId, false);
									calculoProdutorConfirmada.acumulaItem("", producaoForma, publicoAlvoId, true);
									calculoProducaoEsperada.acumulaItem(composicao, producaoForma, publicoAlvoId, false);
									calculoProducaoConfirmada.acumulaItem(composicao, producaoForma, publicoAlvoId, true);
								}
								resultProdutor.add(fetch(produtorProducao, bemClassificacaoList, calculoProdutorEsperada, calculoProdutorConfirmada));
							}

						}
					}

					ProducaoCalculo calculoProducaoTotal = new ProducaoCalculo();
					for (ProducaoForma producaoForma : producao.getProducaoFormaList()) {
						calculoProducaoTotal.acumulaItem("", producaoForma, 0, false);
					}
					
					Integer quantidadeProdutores = 0;
					ProducaoCalculo temp = new ProducaoCalculo();
					for (Map.Entry<String, CalculoItem> item : calculoProducaoEsperada.matriz.entrySet()) {
						temp.acumulaItem("", item.getValue().producaoFormaTotal, 0, false);
						quantidadeProdutores += item.getValue().publicoAlvoList.size();
					}
					temp.matriz.get("").producaoFormaTotal.setQuantidadeProdutores(quantidadeProdutores);
					calculoProducaoEsperada.acumulaItem("", temp.matriz.get("").producaoFormaTotal, 0, false);

					quantidadeProdutores = 0;
					temp = new ProducaoCalculo();
					for (Map.Entry<String, CalculoItem> item : calculoProducaoConfirmada.matriz.entrySet()) {
						temp.acumulaItem("", item.getValue().producaoFormaTotal, 0, false);
						quantidadeProdutores += item.getValue().publicoAlvoList.size();
					}
					temp.matriz.get("").producaoFormaTotal.setQuantidadeProdutores(quantidadeProdutores);
					calculoProducaoConfirmada.acumulaItem("", temp.matriz.get("").producaoFormaTotal, 0, false);

					result.add(fetch(producao, bemClassificacaoList, calculoProducaoEsperada, calculoProducaoConfirmada, resultProdutor, calculoProducaoTotal));
				}
			}
		}

		contexto.setResposta(result);
		return false;
	}

	private List<Object> fetch(Producao producao, Map<String, Object> bemClassificacaoList, ProducaoCalculo calculoProducaoEsperada, ProducaoCalculo calculoProducaoConfirmada) {
		return fetch(producao, bemClassificacaoList, calculoProducaoEsperada, calculoProducaoConfirmada, null, null);
	}

	private List<Object> fetch(Producao producao, Map<String, Object> bemClassificacaoList, ProducaoCalculo esperada, ProducaoCalculo confirmada, List<Object> resultProdutor, ProducaoCalculo total) {
		List<Object> result = new ArrayList<Object>();

		result.add(producao.getId()); // PRODUCAO_ID
		result.add(producao.getAno()); // PRODUCAO_ANO
		result.add(producao.getBem().getId()); // PRODUCAO_BEM_ID
		result.add(producao.getBem().getNome()); // PRODUCAO_BEM_NOME

		result.add(bemClassificacaoList == null ? null : bemClassificacaoList.get("bemClassificacao")); // PRODUCAO_BEM_CLASSIFICACAO
		result.add(bemClassificacaoList == null ? null : bemClassificacaoList.get("unidadeMedida") == null ? null : ((UnidadeMedida) bemClassificacaoList.get("unidadeMedida")).getNome()); // PRODUCAO_UNIDADE_MEDIDA
		result.add(bemClassificacaoList == null ? null : bemClassificacaoList.get("formulaProduto") == null ? null : bemClassificacaoList.get("formulaProduto").toString()); // PRODUCAO_FORMULA
		result.add(bemClassificacaoList == null ? null : bemClassificacaoList.get("itemA") == null ? null : ((ItemNome) bemClassificacaoList.get("itemA")).getNome()); // PRODUCAO_NOME_ITEM_A
		result.add(bemClassificacaoList == null ? null : bemClassificacaoList.get("itemB") == null ? null : ((ItemNome) bemClassificacaoList.get("itemB")).getNome()); // PRODUCAO_NOME_ITEM_B
		result.add(bemClassificacaoList == null ? null : bemClassificacaoList.get("itemC") == null ? null : ((ItemNome) bemClassificacaoList.get("itemC")).getNome()); // PRODUCAO_NOME_ITEM_C

		result.add(producao.getUnidadeOrganizacional() == null ? null : producao.getUnidadeOrganizacional().getId()); // PRODUCAO_UNID_ORG_ID
		result.add(producao.getUnidadeOrganizacional() == null ? null : producao.getUnidadeOrganizacional().getNome()); // PRODUCAO_UNID_ORG_NOME
		result.add(producao.getUnidadeOrganizacional() == null ? null : producao.getUnidadeOrganizacional().getSigla()); // PRODUCAO_UNID_ORG_SIGLA

		result.add(producao.getPublicoAlvo() == null ? null : producao.getPublicoAlvo().getId()); // PRODUCAO_PUBLICO_ALVO_ID
		result.add(producao.getPublicoAlvo() == null ? null : producao.getPublicoAlvo().getPessoa().getNome()); // PRODUCAO_PUBLICO_ALVO_NOME

		result.add(producao.getPropriedadeRural() == null ? null : producao.getPropriedadeRural().getId()); // PRODUCAO_PROPRIEDADE_RURAL_ID
		result.add(producao.getPropriedadeRural() == null ? null : producao.getPropriedadeRural().getNome()); // PRODUCAO_PROPRIEDADE_RURAL_NOME

		result.add(producao.getPropriedadeRural() == null ? null : producao.getPropriedadeRural().getComunidade().getId()); // PRODUCAO_COMUNIDADE_ID
		result.add(producao.getPropriedadeRural() == null ? null : producao.getPropriedadeRural().getComunidade().getNome()); // PRODUCAO_COMUNIDADE_NOME

		// TODO fazer o totalizador final esperado e confirmado
		result.add(fetchProducaoFormaList(producao.getProducaoFormaList(), total, esperada, confirmada)); // PRODUCAO_FORMA_LIST

		result.add(producao.getInclusaoUsuario() == null ? null : producao.getInclusaoUsuario().getPessoa().getNome()); // PRODUCAO_INCLUSAO_NOME
		result.add(producao.getInclusaoUsuario() == null ? null : producao.getInclusaoData()); // PRODUCAO_INCLUSAO_DATA
		result.add(producao.getAlteracaoUsuario() == null ? null : producao.getAlteracaoUsuario().getPessoa().getNome()); // PRODUCAO_ALTERACAO_NOME
		result.add(producao.getAlteracaoUsuario() == null ? null : producao.getAlteracaoData()); // PRODUCAO_ALTERACAO_DATA

		result.add(resultProdutor); // PRODUCAO_PRODUTOR_LIST

		return result;
	}

	private List<Object> fetchProducaoFormaList(List<ProducaoForma> producaoFormaList, ProducaoCalculo total, ProducaoCalculo esperada, ProducaoCalculo confirmada) {
		List<Object> result = new ArrayList<Object>();

		// inserir todas as formas de producao
		if (producaoFormaList != null) {
			List<Object> registro = null;
			for (ProducaoForma producaoForma : producaoFormaList) {
				registro = fetchProducaoForma(producaoForma);
				String composicao = esperada.getComposicao(producaoForma);

				// inserir o totalizador das formas esperadas
				if (esperada != null && esperada.matriz.containsKey(composicao)) {
					CalculoItem calculoItem = esperada.matriz.get(composicao);
					calculoItem.producaoFormaTotal.setQuantidadeProdutores(calculoItem.publicoAlvoList.size());
					registro.addAll(fetchProducaoForma(calculoItem.producaoFormaTotal));
				}

				// inserir o totalizador das formas confirmadas
				if (confirmada != null && confirmada.matriz.containsKey(composicao)) {
					CalculoItem calculoItem = confirmada.matriz.get(composicao);
					calculoItem.producaoFormaTotal.setQuantidadeProdutores(calculoItem.publicoAlvoList.size());
					registro.addAll(fetchProducaoForma(calculoItem.producaoFormaTotal));
				}
				result.add(registro);
			}
		}
		
		

		// inserir o totalizador total das formas esperadas
		if (total != null && total.matriz.containsKey("")) {
			CalculoItem calculoItem = total.matriz.get("");
			result.add(fetchProducaoForma(calculoItem.producaoFormaTotal));
		}

		// inserir o totalizador das formas esperadas
		if (esperada != null && esperada.matriz.containsKey("")) {
			CalculoItem calculoItem = esperada.matriz.get("");
			result.add(fetchProducaoForma(calculoItem.producaoFormaTotal));
		}

		// inserir o totalizador das formas confirmadas
		if (confirmada != null && confirmada.matriz.containsKey("")) {
			CalculoItem calculoItem = confirmada.matriz.get("");
			result.add(fetchProducaoForma(calculoItem.producaoFormaTotal));
		}

		return result;
	}

	private List<Object> fetchProducaoForma(ProducaoForma producaoForma) {
		List<Object> result = new ArrayList<Object>();

		result.add(fetchProducaoFormaComposicaoList(producaoForma.getProducaoFormaComposicaoList())); // FORMA_COMPOSICAO_LIST
		result.add(producaoForma.getItemAValor()); // FORMA_VALOR_ITEM_A
		result.add(producaoForma.getItemBValor()); // FORMA_VALOR_ITEM_B
		result.add(producaoForma.getItemCValor()); // FORMA_VALOR_ITEM_C
		result.add(producaoForma.getVolume()); // FORMA_VOLUME
		result.add(producaoForma.getValorUnitario()); // FORMA_VLR_UNIT
		result.add(producaoForma.getValorTotal()); // FORMA_VLR_TOTAL
		result.add(producaoForma.getQuantidadeProdutores()); // FORMA_QTD_PRODUTORES
		result.add(producaoForma.getDataConfirmacao()); // FORMA_DATA_CONFIRMACAO
		result.add(producaoForma.getInclusaoUsuario() == null ? null : producaoForma.getInclusaoUsuario().getPessoa().getNome()); // FORMA_INCLUSAO_NOME
		result.add(producaoForma.getInclusaoData()); // FORMA_INCLUSAO_DATA
		result.add(producaoForma.getAlteracaoUsuario() == null ? null : producaoForma.getAlteracaoUsuario().getPessoa().getNome()); // FORMA_ALTERACAO_NOME
		result.add(producaoForma.getAlteracaoData()); // FORMA_ALTERACAO_DATA

		return result;
	}

	private List<Object> fetchProducaoFormaComposicaoList(List<ProducaoFormaComposicao> producaoFormaComposicaoList) {
		List<Object> result = null;
		if (producaoFormaComposicaoList != null) {
			for (ProducaoFormaComposicao producaoFormaComposicao : producaoFormaComposicaoList) {
				List<Object> linha = new ArrayList<Object>();
				linha.add(producaoFormaComposicao.getFormaProducaoValor().getId()); // COMPOSICAO_FORMA_PRODUCAO_VALOR_ID
				linha.add(producaoFormaComposicao.getFormaProducaoValor().getFormaProducaoItem().getNome()); // COMPOSICAO_FORMA_PRODUCAO_VALOR_ITEM_NOME
				linha.add(producaoFormaComposicao.getFormaProducaoValor().getNome()); // COMPOSICAO_FORMA_PRODUCAO_VALOR_NOME
				linha.add(producaoFormaComposicao.getOrdem()); // COMPOSICAO_ORDEM
				if (result == null) {
					result = new ArrayList<Object>();
				}
				result.add(linha);
			}
		}
		return result;
	}

}
// @Autowired
// private ProducaoCalculo calculo;
//
// @Autowired
// private UtilDao utilDao;
//
//
// @SuppressWarnings("unused")
// private List<Object> fetch(List<Producao> producaoList) {
// List<Object> result = null;
// if (producaoList != null) {
// for (Producao producao : producaoList) {
// // calcular somatorios
// calculo.abreProducao(producao.getId());
// calculo.calcular(true);
//
// List<Object> linha = new ArrayList<Object>();
//
// linha.add(calculo.getProducao().getId()); // PRODUCAO_ID
// linha.add(calculo.getProducao().getAno()); // PRODUCAO_ANO
// linha.add(calculo.getProducao().getComunidade().getUnidadeOrganizacionalComunidadeList().get(0).getUnidadeOrganizacional().getNome());
// // PRODUCAO_UNIDADE_LOCAL
// Map<String, Object> bemClassificacaoList =
// utilDao.ipaBemClassificacaoDetalhes(calculo.getProducao().getBem().getBemClassificacao());
// linha.add(bemClassificacaoList.get("bemClassificacao")); // CLASSIFICACAO_BEM
// linha.add(calculo.getProducao().getBem().getNome()); // BEM_NOME
// linha.add(((UnidadeMedida)
// bemClassificacaoList.get("unidadeMedida")).getNome()); // UNIDADE_MEDIDA
// linha.add(fetchProducaoFormaList(calculo.getProducao().getProducaoFormaList()));
// // PRODUCAO_FORMA_LIST
// linha.add(fetchProdutores(calculo.getProducao())); // PRODUTORES_LIST
// linha.add(bemClassificacaoList.get("formulaProduto").toString()); // FORMULA
// linha.add(bemClassificacaoList.get("itemA") == null ? null : ((ItemNome)
// bemClassificacaoList.get("itemA")).getNome()); // ITEM_NOME_A
// linha.add(bemClassificacaoList.get("itemB") == null ? null : ((ItemNome)
// bemClassificacaoList.get("itemB")).getNome()); // ITEM_NOME_B
// linha.add(bemClassificacaoList.get("itemC") == null ? null : ((ItemNome)
// bemClassificacaoList.get("itemC")).getNome()); // ITEM_NOME_C
// linha.add(calculo.getProducao().getComunidade().getId()); // COMUNIDADE_ID
// linha.add(calculo.getProducao().getBem().getId()); // BEM_ID
// linha.add(calculo.getProducao().getComunidade().getNome()); //
// PRODUCAO_COMUNIDADE
//
// if (result == null) {
// result = new ArrayList<Object>();
// }
// result.add(linha.toArray());
// }
// }
// return result;
// }
//
// private List<Object[]> fetchProducaoFormaList(List<ProducaoForma>
// producaoFormaList) {
// List<Object[]> result = null;
// if (producaoFormaList != null) {
// for (ProducaoForma producaoForma : producaoFormaList) {
// List<Object> linha = new ArrayList<Object>();
// linha.add(fetchProducaoFormaComposicaoList(producaoForma.getProducaoFormaComposicaoList()));
// // PRODUCAO_FORMA_COMPOSICAO_LIST
// linha.add(producaoForma.getVolume()); // PROD_FORMA_VOLUME
// linha.add(0); // PROD_FORMA_VOLUME_ESPERADO
// linha.add(producaoForma.getQuantidadeProdutores()); //
// PROD_FORMA_QTD_PRODUTORES
// linha.add(0); // PROD_FORMA_VOLUME_CONFIRMADO
// linha.add(producaoForma.getItemAValor()); // PROD_FORMA_ITEM_A_VALOR
// linha.add(producaoForma.getItemBValor()); // PROD_FORMA_ITEM_B_VALOR
// linha.add(producaoForma.getItemCValor()); // PROD_FORMA_ITEM_C_VALOR
// linha.add(producaoForma.getValorUnitario()); // PROD_FORMA_VLR_UNITARIO
// linha.add(producaoForma.getValorTotal()); // PROD_FORMA_VLR_TOTAL
// linha.add(producaoForma.getDataConfirmacao()); // PROD_FORMA_DT_CONFIRM
//
// Object[] mapa =
// calculo.getMapaDeProducaoForma().get(calculo.getComposicao(producaoForma));
//
// if (mapa != null) {
// ProducaoForma confirmado = (ProducaoForma) mapa[2];
// linha.add(confirmado.getItemAValor()); // PROD_FORMA_ITEM_A_VALOR_CONFIRM
// linha.add(confirmado.getItemBValor()); // PROD_FORMA_ITEM_B_VALOR_CONFIRM
// linha.add(confirmado.getItemCValor()); // PROD_FORMA_ITEM_C_VALOR_CONFIRM
// linha.add(confirmado.getVolume()); // PROD_FORMA_VOLUME_CONFIRM
// linha.add(confirmado.getValorUnitario()); // PROD_FORMA_VLR_UNITARIO_CONFIRM
// linha.add(confirmado.getValorTotal()); // PROD_FORMA_VLR_TOTAL_CONFIRM
// linha.add(confirmado.getQuantidadeProdutores()); //
// PROD_FORMA_QTD_PRODUTORES_CONFIRM
// }
//
// if (result == null) {
// result = new ArrayList<Object[]>();
// }
// result.add(linha.toArray());
// }
// }
// return result;
// }
//
// private List<Object[]>
// fetchProducaoFormaComposicaoList(List<ProducaoFormaComposicao>
// producaoFormaComposicaoList) {
// List<Object[]> result = null;
// if (producaoFormaComposicaoList != null) {
// for (ProducaoFormaComposicao producaoFormaComposicao :
// producaoFormaComposicaoList) {
// List<Object> linha = new ArrayList<Object>();
// linha.add(producaoFormaComposicao.getFormaProducaoValor().getFormaProducaoItem().getNome());
// // COMPOSICAO_FORMA_PRODUCAO_VALOR_ITEM_NOME
// linha.add(producaoFormaComposicao.getFormaProducaoValor().getNome()); //
// COMPOSICAO_FORMA_PRODUCAO_VALOR_NOME
// linha.add(producaoFormaComposicao.getOrdem()); // COMPOSICAO_ORDEM
// linha.add(producaoFormaComposicao.getFormaProducaoValor().getId()); //
// COMPOSICAO_FORMA_PRODUCAO_VALOR_ID
// if (result == null) {
// result = new ArrayList<Object[]>();
// }
// result.add(linha.toArray());
// }
// }
// return result;
// }
//
// private List<Object> fetchProdutor(Producao producao) {
// List<Object> result = null;
// if (producao != null) {
// result = new ArrayList<Object>();
// result.add(producao.getPropriedadeRural() != null ?
// producao.getPropriedadeRural().getNome() : null);// PROPRIEDADE_RURAL_NOME
// result.add(producao.getPublicoAlvo() != null ?
// producao.getPublicoAlvo().getPessoa().getNome() : null);// PUBLICO_ALVO_NOME
// result.add(fetchProducaoFormaList(producao.getProducaoFormaList()));//
// PRODUTOR_PRODUCAO_FORMA_LIST
// result.add(producao.getId());// PRODUTOR_PRODUCAO_ID
// }
// return result;
// }
//
// private List<Object[]> fetchProdutores(Producao producao) {
// List<Object[]> result = null;
// if (producao != null) {
// List<Producao> producaoList =
// dao.findByAnoAndBemAndPropriedadeRuralComunidade(producao.getAno(),
// producao.getBem(), producao.getComunidade());
// if (producaoList != null) {
// for (Producao p : producaoList) {
// if (result == null) {
// result = new ArrayList<Object[]>();
// }
// result.add(fetchProdutor(p).toArray());
// }
// }
// }
// return result;
// }
