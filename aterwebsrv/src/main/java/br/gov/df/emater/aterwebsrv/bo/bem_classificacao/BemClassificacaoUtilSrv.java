package br.gov.df.emater.aterwebsrv.bo.bem_classificacao;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaList;
import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.dao.indice_producao.BemClassificacaoDao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;

@Service
public class BemClassificacaoUtilSrv {

	@Autowired
	private BemClassificacaoDao dao;

	private void acumulaBemClassificacao(BemClassificacao origem, BemClassificacao destino) {
		destino.setNome(destino.getNome() != null ? destino.getNome().concat("/") : "");
		destino.setNome(destino.getNome().concat(origem.getNome()));
		destino.setFormula(origem.getFormula() != null ? origem.getFormula() : destino.getFormula());
		destino.setUnidadeMedida(origem.getUnidadeMedida() != null ? origem.getUnidadeMedida() : destino.getUnidadeMedida());
		destino.setItemANome(origem.getItemANome() != null ? origem.getItemANome() : destino.getItemANome());
		destino.setItemBNome(origem.getItemBNome() != null ? origem.getItemBNome() : destino.getItemBNome());
		destino.setItemCNome(origem.getItemCNome() != null ? origem.getItemCNome() : destino.getItemCNome());
		// acumular itens
		if (origem.getBemClassificacaoFormaProducaoItemList() != null) {
			if (destino.getBemClassificacaoFormaProducaoItemList() == null) {
				destino.setBemClassificacaoFormaProducaoItemList(new ArrayList<>());
			}
			destino.getBemClassificacaoFormaProducaoItemList().addAll(origem.getBemClassificacaoFormaProducaoItemList());
		}
		// acumular itens
		if (origem.getBemClassificacaoFormaProducaoValorList() != null) {
			if (destino.getBemClassificacaoFormaProducaoValorList() == null) {
				destino.setBemClassificacaoFormaProducaoValorList(new ArrayList<>());
			}
			destino.getBemClassificacaoFormaProducaoValorList().addAll(origem.getBemClassificacaoFormaProducaoValorList());
		}
	}

	@Cacheable("BemClassificacaoMatriz")
	public List<BemClassificacao> geraMatriz() {
		List<BemClassificacao> result = null;
		List<BemClassificacao> bancoDados = dao.findAll();

		if (bancoDados != null) {
			for (BemClassificacao bemClassificacao : bancoDados) {
				BemClassificacao item = new BemClassificacao(bemClassificacao.getId());

				List<BemClassificacao> paiList = null;
				paiList = getBemClassificacaoPai(bemClassificacao, paiList);

				if (paiList != null) {
					Collections.reverse(paiList);
					// captar os dados dos pais
					for (BemClassificacao bemClassificacaoPai : paiList) {
						acumulaBemClassificacao(bemClassificacaoPai, item);
					}
				}
				// captar o dado principal
				acumulaBemClassificacao(bemClassificacao, item);

				if (result == null) {
					result = new ArrayList<>();
				}

				item.setUnidadeMedida(infoBasicaReg(item.getUnidadeMedida()));
				item.setItemANome(infoBasicaReg(item.getItemANome()));
				item.setItemBNome(infoBasicaReg(item.getItemBNome()));
				item.setItemCNome(infoBasicaReg(item.getItemCNome()));
				item.setBemClassificacaoFormaProducaoItemList(infoBasicaList(item.getBemClassificacaoFormaProducaoItemList()));
				item.setBemClassificacaoFormaProducaoValorList(infoBasicaList(item.getBemClassificacaoFormaProducaoValorList()));

				result.add(item);
			}
		}
		return result;
	}

	private List<BemClassificacao> getBemClassificacaoPai(BemClassificacao bemClassificacao, List<BemClassificacao> paiList) {
		if (bemClassificacao != null && bemClassificacao.getBemClassificacao() != null) {
			if (paiList == null) {
				paiList = new ArrayList<>();
			}
			paiList.add(bemClassificacao.getBemClassificacao());
			paiList = getBemClassificacaoPai(bemClassificacao.getBemClassificacao(), paiList);
		}
		return paiList;
	}

}
