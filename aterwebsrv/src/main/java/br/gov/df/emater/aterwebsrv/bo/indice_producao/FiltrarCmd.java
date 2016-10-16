package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ferramenta.UtilDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoProprietarioDao;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.IndiceProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoValor;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ItemNome;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoComposicao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.UnidadeMedida;

@Service("IndiceProducaoFiltrarCmd")
public class FiltrarCmd extends _Comando {

	@Autowired
	private ProducaoProprietarioDao dao;

	@Autowired
	private UtilDao utilDao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		IndiceProducaoCadFiltroDto filtro = (IndiceProducaoCadFiltroDto) contexto.getRequisicao();
		List<ProducaoProprietario> result = dao.filtrarNovo(filtro);
/*
		if (producaoProprietarioList != null) {
			// fetch no resultado
			if (filtro.getId() != null) {
				if (producaoProprietarioList != null && producaoProprietarioList.size() == 1) {
					// producaoProprietarioList = new ArrayList<ProducaoProprietario>();
					// result.add(fetchProdutor(result.get(0)));
				}
			} else {

				// contabilizar a producao
				for (ProducaoProprietario producaoProprietario : producaoProprietarioList) {
					List<Object> resultProdutor = null;

					Map<String, Object> bemClassificacaoList = utilDao.ipaBemClassificacaoDetalhes(producaoProprietario.getBemClassificado().getBemClassificacao());

					ProducaoCalculo calculoProducaoTotal = new ProducaoCalculo(bemClassificacaoList, "Estimada");
					ProducaoCalculo calculoProducaoProdutoresEsperada = new ProducaoCalculo(bemClassificacaoList, "Calculada");
					ProducaoCalculo calculoProducaoProdutoresConfirmada = new ProducaoCalculo(bemClassificacaoList, "Confirmada");

					// contabilizar os produtores da producao
					List<ProducaoProprietario> produtorProducaoProprietarioList = dao.findByAnoAndBemClassificadoAndPropriedadeRuralComunidadeUnidadeOrganizacional(producaoProprietario.getAno(), producaoProprietario.getBemClassificado(), producaoProprietario.getUnidadeOrganizacional());
					// TODO utilizar o filtro na producao dos produtores

					if (produtorProducaoProprietarioList != null) {

						// looping pelos produtores cadastrados
						for (ProducaoProprietario produtorProducaoProprietario : produtorProducaoProprietarioList) {
							if (!CollectionUtils.isEmpty(filtro.getComunidadeList())) {
								boolean continuar = false;
								for (Comunidade comunidade : filtro.getComunidadeList()) {
									if (comunidade.getId().equals(produtorProducaoProprietario.getPropriedadeRural().getComunidade().getId())) {
										continuar = true;
										break;
									}
								}
								if (!continuar) {
									continue;
								}
							}

							if (produtorProducaoProprietario.getProducaoList() != null) {
								ProducaoCalculo calculoProdutorEsperada = new ProducaoCalculo(bemClassificacaoList, "Estimada");
								ProducaoCalculo calculoProdutorConfirmada = new ProducaoCalculo(bemClassificacaoList, "Confirmada");
								for (Producao producao : produtorProducaoProprietario.getProducaoList()) {
									String composicao = ProducaoCalculo.getComposicao(producao);
									Integer publicoAlvoId = produtorProducaoProprietario.getPublicoAlvo() == null ? null : produtorProducaoProprietario.getPublicoAlvo().getId();

									// verificar se há filtro pela forma de
									// producao
									if (!CollectionUtils.isEmpty(filtro.getFormaProducaoValorList())) {
										boolean continuar = false;
										fora: for (FormaProducaoValor formaProducaoValor : filtro.getFormaProducaoValorList()) {
											for (String comp : composicao.split(",")) {
												if (Integer.valueOf(comp.trim()).equals(formaProducaoValor.getId())) {
													continuar = true;
													break fora;
												}
											}
										}
										if (!continuar) {
											continue;
										}
									}

									// acumular os totais do produtor
									calculoProducaoProdutoresEsperada.acumulaItem(composicao, producao, publicoAlvoId, false);
									calculoProducaoProdutoresConfirmada.acumulaItem(composicao, producao, publicoAlvoId, true);

									// acumular os totais da producao
									calculoProdutorEsperada.acumulaItem("", producao, publicoAlvoId, false);
									calculoProdutorConfirmada.acumulaItem("", producao, publicoAlvoId, true);
								}
								if (resultProdutor == null) {
									resultProdutor = new ArrayList<Object>();
								}
								resultProdutor.add(fetch(filtro, produtorProducaoProprietario, calculoProdutorEsperada, calculoProdutorConfirmada));
							}
						}
					}

					// calcular a produção estimada informada
					for (Producao producao : producaoProprietario.getProducaoList()) {
						String composicao = ProducaoCalculo.getComposicao(producao);
						// verificar se há filtro pela forma de producao
						if (!CollectionUtils.isEmpty(filtro.getFormaProducaoValorList())) {
							boolean continuar = false;
							fora: for (FormaProducaoValor formaProducaoValor : filtro.getFormaProducaoValorList()) {
								for (String comp : composicao.split(",")) {
									if (Integer.valueOf(comp.trim()).equals(formaProducaoValor.getId())) {
										continuar = true;
										break fora;
									}
								}
							}
							if (!continuar) {
								continue;
							}
						}
						calculoProducaoTotal.acumulaItem("", producao, null, false);
					}

					calculoProducaoProdutoresEsperada.totalizar();

					calculoProducaoProdutoresConfirmada.totalizar();

					if (result == null) {
						result = new ArrayList<Object>();
					}
					result.add(fetch(filtro, producaoProprietario, calculoProducaoProdutoresEsperada, calculoProducaoProdutoresConfirmada, calculoProducaoTotal, resultProdutor));
				}
			}
		}
*/
		contexto.setResposta(result);
		return false;
	}

	private List<Object> fetch(IndiceProducaoCadFiltroDto filtro, ProducaoProprietario producaoProprietario, ProducaoCalculo calculoProducaoEsperada, ProducaoCalculo calculoProducaoConfirmada) {
		return fetch(filtro, producaoProprietario, calculoProducaoEsperada, calculoProducaoConfirmada, null, null);
	}

	private List<Object> fetch(IndiceProducaoCadFiltroDto filtro, ProducaoProprietario producaoProprietario, ProducaoCalculo esperada, ProducaoCalculo confirmada, ProducaoCalculo total, List<Object> resultProdutor) {
		List<Object> result = new ArrayList<Object>();

		result.add(producaoProprietario.getId()); // PRODUCAO_ID
		result.add(producaoProprietario.getAno()); // PRODUCAO_ANO
		result.add(producaoProprietario.getBemClassificado().getId()); // PRODUCAO_BEM_ID
		result.add(producaoProprietario.getBemClassificado().getNome()); // PRODUCAO_BEM_NOME

		result.add(esperada.getBemClassificacaoList() == null ? null : esperada.getBemClassificacaoList().get("bemClassificacao")); // PRODUCAO_BEM_CLASSIFICACAO
		result.add(esperada.getBemClassificacaoList() == null ? null : esperada.getBemClassificacaoList().get("unidadeMedida") == null ? null : ((UnidadeMedida) esperada.getBemClassificacaoList().get("unidadeMedida")).getNome()); // PRODUCAO_UNIDADE_MEDIDA
		result.add(esperada.getBemClassificacaoList() == null ? null : esperada.getBemClassificacaoList().get("formulaProduto") == null ? null : esperada.getBemClassificacaoList().get("formulaProduto").toString()); // PRODUCAO_FORMULA
		result.add(esperada.getBemClassificacaoList() == null ? null : esperada.getBemClassificacaoList().get("itemA") == null ? null : ((ItemNome) esperada.getBemClassificacaoList().get("itemA")).getNome()); // PRODUCAO_NOME_ITEM_A
		result.add(esperada.getBemClassificacaoList() == null ? null : esperada.getBemClassificacaoList().get("itemB") == null ? null : ((ItemNome) esperada.getBemClassificacaoList().get("itemB")).getNome()); // PRODUCAO_NOME_ITEM_B
		result.add(esperada.getBemClassificacaoList() == null ? null : esperada.getBemClassificacaoList().get("itemC") == null ? null : ((ItemNome) esperada.getBemClassificacaoList().get("itemC")).getNome()); // PRODUCAO_NOME_ITEM_C

		result.add(producaoProprietario.getUnidadeOrganizacional() == null ? null : producaoProprietario.getUnidadeOrganizacional().getId()); // PRODUCAO_UNID_ORG_ID
		result.add(producaoProprietario.getUnidadeOrganizacional() == null ? null : producaoProprietario.getUnidadeOrganizacional().getNome()); // PRODUCAO_UNID_ORG_NOME
		result.add(producaoProprietario.getUnidadeOrganizacional() == null ? null : producaoProprietario.getUnidadeOrganizacional().getSigla()); // PRODUCAO_UNID_ORG_SIGLA

		result.add(producaoProprietario.getPublicoAlvo() == null ? null : producaoProprietario.getPublicoAlvo().getId()); // PRODUCAO_PUBLICO_ALVO_ID
		result.add(producaoProprietario.getPublicoAlvo() == null ? null : producaoProprietario.getPublicoAlvo().getPessoa().getId()); // PRODUCAO_PUBLICO_ALVO_PESSOA_ID
		result.add(producaoProprietario.getPublicoAlvo() == null ? null : producaoProprietario.getPublicoAlvo().getPessoa().getNome()); // PRODUCAO_PUBLICO_ALVO_NOME

		result.add(producaoProprietario.getPropriedadeRural() == null ? null : producaoProprietario.getPropriedadeRural().getId()); // PRODUCAO_PROPRIEDADE_RURAL_ID
		result.add(producaoProprietario.getPropriedadeRural() == null ? null : producaoProprietario.getPropriedadeRural().getNome()); // PRODUCAO_PROPRIEDADE_RURAL_NOME

		result.add(producaoProprietario.getPropriedadeRural() == null ? null : producaoProprietario.getPropriedadeRural().getComunidade().getId()); // PRODUCAO_COMUNIDADE_ID
		result.add(producaoProprietario.getPropriedadeRural() == null ? null : producaoProprietario.getPropriedadeRural().getComunidade().getNome()); // PRODUCAO_COMUNIDADE_NOME

		result.add(fetchProducaoList(producaoProprietario.getProducaoList(), total, esperada, confirmada, filtro)); // PRODUCAO_FORMA_LIST

		result.add(producaoProprietario.getInclusaoUsuario() == null ? null : producaoProprietario.getInclusaoUsuario().getPessoa().getNome()); // PRODUCAO_INCLUSAO_NOME
		result.add(producaoProprietario.getInclusaoUsuario() == null ? null : producaoProprietario.getInclusaoData()); // PRODUCAO_INCLUSAO_DATA
		result.add(producaoProprietario.getAlteracaoUsuario() == null ? null : producaoProprietario.getAlteracaoUsuario().getPessoa().getNome()); // PRODUCAO_ALTERACAO_NOME
		result.add(producaoProprietario.getAlteracaoUsuario() == null ? null : producaoProprietario.getAlteracaoData()); // PRODUCAO_ALTERACAO_DATA

		result.add(resultProdutor); // PRODUCAO_PRODUTOR_LIST

		return result;
	}

	private List<Object> fetchProducao(Producao producaoForma) {
		return fetchProducao(producaoForma, null);
	}

	private List<Object> fetchProducao(Producao producao, String nomeCalculo) {
		List<Object> result = new ArrayList<Object>();

		result.add(fetchProducaoComposicaoList(producao.getProducaoComposicaoList())); // FORMA_COMPOSICAO_LIST
		result.add(producao.getItemAValor()); // FORMA_VALOR_ITEM_A
		result.add(producao.getItemBValor()); // FORMA_VALOR_ITEM_B
		result.add(producao.getItemCValor()); // FORMA_VALOR_ITEM_C
		result.add(producao.getVolume()); // FORMA_VOLUME
		result.add(producao.getValorUnitario()); // FORMA_VLR_UNIT
		result.add(producao.getValorTotal()); // FORMA_VLR_TOTAL
		result.add(producao.getQuantidadeProdutores()); // FORMA_QTD_PRODUTORES
		result.add(producao.getDataConfirmacao()); // FORMA_DATA_CONFIRMACAO
		result.add(producao.getInclusaoUsuario() == null ? null : producao.getInclusaoUsuario().getPessoa().getNome()); // FORMA_INCLUSAO_NOME
		result.add(producao.getInclusaoData()); // FORMA_INCLUSAO_DATA
		result.add(producao.getAlteracaoUsuario() == null ? null : producao.getAlteracaoUsuario().getPessoa().getNome()); // FORMA_ALTERACAO_NOME
		result.add(producao.getAlteracaoData()); // FORMA_ALTERACAO_DATA
		result.add(nomeCalculo); // FORMA_NOME_CALCULO

		return result;
	}

	private List<Object> fetchProducaoComposicaoList(List<ProducaoComposicao> producaoComposicaoList) {
		List<Object> result = null;
		if (producaoComposicaoList != null) {
			for (ProducaoComposicao producaoComposicao : producaoComposicaoList) {
				List<Object> linha = new ArrayList<Object>();
				linha.add(producaoComposicao.getFormaProducaoValor().getId()); // COMPOSICAO_FORMA_PRODUCAO_VALOR_ID
				linha.add(producaoComposicao.getFormaProducaoValor().getFormaProducaoItem().getNome()); // COMPOSICAO_FORMA_PRODUCAO_VALOR_ITEM_NOME
				linha.add(producaoComposicao.getFormaProducaoValor().getNome()); // COMPOSICAO_FORMA_PRODUCAO_VALOR_NOME
				linha.add(producaoComposicao.getOrdem()); // COMPOSICAO_ORDEM
				if (result == null) {
					result = new ArrayList<Object>();
				}
				result.add(linha);
			}
		}
		return result;
	}

	private List<Object> fetchProducaoList(List<Producao> producaoList, ProducaoCalculo total, ProducaoCalculo esperada, ProducaoCalculo confirmada, IndiceProducaoCadFiltroDto filtro) {
		List<Object> result = new ArrayList<Object>();

		// inserir todas as formas de producao
		if (producaoList != null) {
			List<Object> registro = null;
			for (Producao producao : producaoList) {
				registro = fetchProducao(producao);
				String composicao = ProducaoCalculo.getComposicao(producao);

				// verificar se há filtro pela forma de producao
				if (!CollectionUtils.isEmpty(filtro.getFormaProducaoValorList())) {
					boolean continuar = false;
					fora: for (FormaProducaoValor formaProducaoValor : filtro.getFormaProducaoValorList()) {
						for (String comp : composicao.split(",")) {
							if (Integer.valueOf(comp.trim()).equals(formaProducaoValor.getId())) {
								continuar = true;
								break fora;
							}
						}
					}
					if (!continuar) {
						continue;
					}
				}

				// inserir o totalizador das formas esperadas
				if (esperada != null && esperada.getMatriz().containsKey(composicao)) {
					CalculoItem calculoItem = esperada.getMatriz().get(composicao);
					calculoItem.getProducaoTotal().setQuantidadeProdutores(calculoItem.getPublicoAlvoList().size());
					esperada.atualizarMedias(calculoItem);
					registro.addAll(fetchProducao(calculoItem.getProducaoTotal()));
				}

				// inserir o totalizador das formas confirmadas
				if (confirmada != null && confirmada.getMatriz().containsKey(composicao)) {
					CalculoItem calculoItem = confirmada.getMatriz().get(composicao);
					calculoItem.getProducaoTotal().setQuantidadeProdutores(calculoItem.getPublicoAlvoList().size());
					confirmada.atualizarMedias(calculoItem);
					registro.addAll(fetchProducao(calculoItem.getProducaoTotal()));
				}
				result.add(registro);
			}
		}

		// inserir o totalizador total das formas esperadas
		if (total != null && total.getMatriz().containsKey("")) {
			CalculoItem calculoItem = total.getMatriz().get("");
			// calculoItem.getProducaoTotal().setQuantidadeProdutores(calculoItem.getPublicoAlvoList().size());
			total.atualizarMedias(calculoItem);
			result.add(fetchProducao(calculoItem.getProducaoTotal(), total.getNomeCalculo()));
		}

		// inserir o totalizador das formas esperadas
		if (esperada != null && esperada.getMatriz().containsKey("")) {
			CalculoItem calculoItem = esperada.getMatriz().get("");
			calculoItem.getProducaoTotal().setQuantidadeProdutores(calculoItem.getPublicoAlvoList().size());
			esperada.atualizarMedias(calculoItem);
			result.add(fetchProducao(calculoItem.getProducaoTotal(), esperada.getNomeCalculo()));
		}

		// inserir o totalizador das formas confirmadas
		if (confirmada != null && confirmada.getMatriz().containsKey("")) {
			CalculoItem calculoItem = confirmada.getMatriz().get("");
			calculoItem.getProducaoTotal().setQuantidadeProdutores(calculoItem.getPublicoAlvoList().size());
			confirmada.atualizarMedias(calculoItem);
			result.add(fetchProducao(calculoItem.getProducaoTotal(), confirmada.getNomeCalculo()));
		}

		return result;
	}

}