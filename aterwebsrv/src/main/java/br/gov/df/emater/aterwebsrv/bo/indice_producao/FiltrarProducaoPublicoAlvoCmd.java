package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ferramenta.UtilDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.dto.IndiceProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoValor;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ItemNome;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoFormaComposicao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.UnidadeMedida;

@Service("IndiceProducaoFiltrarProducaoPublicoAlvoCmd")
public class FiltrarProducaoPublicoAlvoCmd extends _Comando {

	@Autowired
	private ProducaoDao dao;

	@Autowired
	private UtilDao utilDao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		IndiceProducaoCadFiltroDto filtro = (IndiceProducaoCadFiltroDto) contexto.getRequisicao();
		List<Object> result = null;
		List<Producao> producaoList = null;
		producaoList = (List<Producao>) dao.filtrar(filtro);

		if (producaoList != null) {
			// contabilizar a producao
			result = processaProducao(filtro, producaoList);
		}

		contexto.setResposta(result);
		return false;
	}

	private List<Object> fetch(IndiceProducaoCadFiltroDto filtro, Producao producao, ProducaoCalculo calculoProducaoEsperada, ProducaoCalculo calculoProducaoConfirmada) {
		return fetch(filtro, producao, calculoProducaoEsperada, calculoProducaoConfirmada, null, null);
	}

	private List<Object> fetch(IndiceProducaoCadFiltroDto filtro, Producao producao, ProducaoCalculo esperada, ProducaoCalculo confirmada, ProducaoCalculo total, List<Object> resultProdutor) {
		List<Object> result = new ArrayList<Object>();

		result.add(producao.getId()); // PRODUCAO_ID
		result.add(producao.getAno()); // PRODUCAO_ANO
		result.add(producao.getBem().getId()); // PRODUCAO_BEM_ID
		result.add(producao.getBem().getNome()); // PRODUCAO_BEM_NOME

		result.add(esperada.getBemClassificacaoList() == null ? null : esperada.getBemClassificacaoList().get("bemClassificacao")); // PRODUCAO_BEM_CLASSIFICACAO
		result.add(esperada.getBemClassificacaoList() == null ? null : esperada.getBemClassificacaoList().get("unidadeMedida") == null ? null : ((UnidadeMedida) esperada.getBemClassificacaoList().get("unidadeMedida")).getNome()); // PRODUCAO_UNIDADE_MEDIDA
		result.add(esperada.getBemClassificacaoList() == null ? null : esperada.getBemClassificacaoList().get("formulaProduto") == null ? null : esperada.getBemClassificacaoList().get("formulaProduto").toString()); // PRODUCAO_FORMULA
		result.add(esperada.getBemClassificacaoList() == null ? null : esperada.getBemClassificacaoList().get("itemA") == null ? null : ((ItemNome) esperada.getBemClassificacaoList().get("itemA")).getNome()); // PRODUCAO_NOME_ITEM_A
		result.add(esperada.getBemClassificacaoList() == null ? null : esperada.getBemClassificacaoList().get("itemB") == null ? null : ((ItemNome) esperada.getBemClassificacaoList().get("itemB")).getNome()); // PRODUCAO_NOME_ITEM_B
		result.add(esperada.getBemClassificacaoList() == null ? null : esperada.getBemClassificacaoList().get("itemC") == null ? null : ((ItemNome) esperada.getBemClassificacaoList().get("itemC")).getNome()); // PRODUCAO_NOME_ITEM_C

		result.add(producao.getUnidadeOrganizacional() == null ? null : producao.getUnidadeOrganizacional().getId()); // PRODUCAO_UNID_ORG_ID
		result.add(producao.getUnidadeOrganizacional() == null ? null : producao.getUnidadeOrganizacional().getNome()); // PRODUCAO_UNID_ORG_NOME
		result.add(producao.getUnidadeOrganizacional() == null ? null : producao.getUnidadeOrganizacional().getSigla()); // PRODUCAO_UNID_ORG_SIGLA

		result.add(producao.getPublicoAlvo() == null ? null : producao.getPublicoAlvo().getId()); // PRODUCAO_PUBLICO_ALVO_ID
		result.add(producao.getPublicoAlvo() == null ? null : producao.getPublicoAlvo().getPessoa().getId()); // PRODUCAO_PUBLICO_ALVO_PESSOA_ID
		result.add(producao.getPublicoAlvo() == null ? null : producao.getPublicoAlvo().getPessoa().getNome()); // PRODUCAO_PUBLICO_ALVO_NOME

		result.add(producao.getPropriedadeRural() == null ? null : producao.getPropriedadeRural().getId()); // PRODUCAO_PROPRIEDADE_RURAL_ID
		result.add(producao.getPropriedadeRural() == null ? null : producao.getPropriedadeRural().getNome()); // PRODUCAO_PROPRIEDADE_RURAL_NOME

		result.add(producao.getPropriedadeRural() == null ? null : producao.getPropriedadeRural().getComunidade().getId()); // PRODUCAO_COMUNIDADE_ID
		result.add(producao.getPropriedadeRural() == null ? null : producao.getPropriedadeRural().getComunidade().getNome()); // PRODUCAO_COMUNIDADE_NOME

		result.add(fetchProducaoFormaList(producao.getProducaoFormaList(), total, esperada, confirmada, filtro)); // PRODUCAO_FORMA_LIST

		result.add(producao.getInclusaoUsuario() == null ? null : producao.getInclusaoUsuario().getPessoa().getNome()); // PRODUCAO_INCLUSAO_NOME
		result.add(producao.getInclusaoUsuario() == null ? null : producao.getInclusaoData()); // PRODUCAO_INCLUSAO_DATA
		result.add(producao.getAlteracaoUsuario() == null ? null : producao.getAlteracaoUsuario().getPessoa().getNome()); // PRODUCAO_ALTERACAO_NOME
		result.add(producao.getAlteracaoUsuario() == null ? null : producao.getAlteracaoData()); // PRODUCAO_ALTERACAO_DATA

		result.add(resultProdutor); // PRODUCAO_PRODUTOR_LIST

		return result;
	}

	private List<Object> fetchProducaoForma(ProducaoForma producaoForma) {
		return fetchProducaoForma(producaoForma, null);
	}

	private List<Object> fetchProducaoForma(ProducaoForma producaoForma, String nomeCalculo) {
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
		result.add(nomeCalculo); // FORMA_NOME_CALCULO

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

	private List<Object> fetchProducaoFormaList(List<ProducaoForma> producaoFormaList, ProducaoCalculo total, ProducaoCalculo esperada, ProducaoCalculo confirmada, IndiceProducaoCadFiltroDto filtro) {
		List<Object> result = new ArrayList<Object>();

		// inserir todas as formas de producao
		if (producaoFormaList != null) {
			List<Object> registro = null;
			for (ProducaoForma producaoForma : producaoFormaList) {
				registro = fetchProducaoForma(producaoForma);
				String composicao = ProducaoCalculo.getComposicao(producaoForma);

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
					calculoItem.getProducaoFormaTotal().setQuantidadeProdutores(calculoItem.getPublicoAlvoList().size());
					esperada.atualizarMedias(calculoItem);
					registro.addAll(fetchProducaoForma(calculoItem.getProducaoFormaTotal()));
				}

				// inserir o totalizador das formas confirmadas
				if (confirmada != null && confirmada.getMatriz().containsKey(composicao)) {
					CalculoItem calculoItem = confirmada.getMatriz().get(composicao);
					calculoItem.getProducaoFormaTotal().setQuantidadeProdutores(calculoItem.getPublicoAlvoList().size());
					confirmada.atualizarMedias(calculoItem);
					registro.addAll(fetchProducaoForma(calculoItem.getProducaoFormaTotal()));
				}
				result.add(registro);
			}
		}

		// inserir o totalizador total das formas esperadas
		if (total != null && total.getMatriz().containsKey("")) {
			CalculoItem calculoItem = total.getMatriz().get("");
			// calculoItem.getProducaoFormaTotal().setQuantidadeProdutores(calculoItem.getPublicoAlvoList().size());
			total.atualizarMedias(calculoItem);
			result.add(fetchProducaoForma(calculoItem.getProducaoFormaTotal(), total.getNomeCalculo()));
		}

		// inserir o totalizador das formas esperadas
		if (esperada != null && esperada.getMatriz().containsKey("")) {
			CalculoItem calculoItem = esperada.getMatriz().get("");
			calculoItem.getProducaoFormaTotal().setQuantidadeProdutores(calculoItem.getPublicoAlvoList().size());
			esperada.atualizarMedias(calculoItem);
			result.add(fetchProducaoForma(calculoItem.getProducaoFormaTotal(), esperada.getNomeCalculo()));
		}

		// inserir o totalizador das formas confirmadas
		if (confirmada != null && confirmada.getMatriz().containsKey("")) {
			CalculoItem calculoItem = confirmada.getMatriz().get("");
			calculoItem.getProducaoFormaTotal().setQuantidadeProdutores(calculoItem.getPublicoAlvoList().size());
			confirmada.atualizarMedias(calculoItem);
			result.add(fetchProducaoForma(calculoItem.getProducaoFormaTotal(), confirmada.getNomeCalculo()));
		}

		return result;
	}

	private List<Object> processaProducao(IndiceProducaoCadFiltroDto filtro, List<Producao> lista) {
		List<Object> result = null;

		Map<String, Object> bemClassificacaoList = null;
		ProducaoCalculo calculoProducaoProdutoresEsperada = null;
		ProducaoCalculo calculoProducaoProdutoresConfirmada = null;

		Integer bemId = null;

		for (Producao produtorProducao : lista) {
			if (bemId != produtorProducao.getBem().getId()) {
				bemClassificacaoList = utilDao.ipaBemClassificacaoDetalhes(produtorProducao.getBem().getBemClassificacao());
				calculoProducaoProdutoresEsperada = new ProducaoCalculo(bemClassificacaoList, "Calculada");
				calculoProducaoProdutoresConfirmada = new ProducaoCalculo(bemClassificacaoList, "Confirmada");
				bemId = produtorProducao.getBem().getId();
			}
			if (!CollectionUtils.isEmpty(filtro.getComunidadeList())) {
				boolean continuar = false;
				for (Comunidade comunidade : filtro.getComunidadeList()) {
					if (comunidade.getId().equals(produtorProducao.getPropriedadeRural().getComunidade().getId())) {
						continuar = true;
						break;
					}
				}
				if (!continuar) {
					continue;
				}
			}

			if (produtorProducao.getProducaoFormaList() != null) {
				ProducaoCalculo calculoProdutorEsperada = new ProducaoCalculo(bemClassificacaoList, "Estimada");
				ProducaoCalculo calculoProdutorConfirmada = new ProducaoCalculo(bemClassificacaoList, "Confirmada");
				for (ProducaoForma producaoForma : produtorProducao.getProducaoFormaList()) {
					String composicao = ProducaoCalculo.getComposicao(producaoForma);
					Integer publicoAlvoId = produtorProducao.getPublicoAlvo().getId();

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
					calculoProducaoProdutoresEsperada.acumulaItem(composicao, producaoForma, publicoAlvoId, false);
					calculoProducaoProdutoresConfirmada.acumulaItem(composicao, producaoForma, publicoAlvoId, true);

					// acumular os totais da producao
					calculoProdutorEsperada.acumulaItem("", producaoForma, publicoAlvoId, false);
					calculoProdutorConfirmada.acumulaItem("", producaoForma, publicoAlvoId, true);
				}
				if (result == null) {
					result = new ArrayList<Object>();
				}
				result.add(fetch(filtro, produtorProducao, calculoProdutorEsperada, calculoProdutorConfirmada));
			}
		}
		return result;
	}

}
