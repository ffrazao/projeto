package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaList;
import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.dao.indice_producao.BemClassificacaoDao;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.BemClassificacaoCadDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacaoFormaProducaoItem;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacaoFormaProducaoValor;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoItem;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoValor;

@Service("IndiceProducaoBemClassificacaoUtilSrv")
public class BemClassificacaoUtilSrv {

	@Autowired
	private BemClassificacaoDao dao;

	private void acumulaBemClassificacao(BemClassificacao origem, BemClassificacao destino, boolean acumulaNome) {
		if (acumulaNome) {
			destino.setNome(destino.getNome() != null ? destino.getNome().concat("/") : "");
			destino.setNome(destino.getNome().concat(origem.getNome()));
		}
		// acumular itens
		if (origem.getBemClassificacaoFormaProducaoItemList() != null) {
			if (destino.getBemClassificacaoFormaProducaoItemList() == null) {
				destino.setBemClassificacaoFormaProducaoItemList(new ArrayList<>());
			}
			for (BemClassificacaoFormaProducaoItem reg : origem.getBemClassificacaoFormaProducaoItemList()) {
				boolean encontrou = false;
				for (BemClassificacaoFormaProducaoItem cadastrado : destino.getBemClassificacaoFormaProducaoItemList()) {
					if (cadastrado.getId().equals(reg.getId())) {
						encontrou = true;
						break;
					}
				}
				if (!encontrou) {
					destino.getBemClassificacaoFormaProducaoItemList().add(infoBasicaReg(reg));
				}
			}
		}
		
		// acumular valor
		if (origem.getBemClassificacaoFormaProducaoValorList() != null && origem.getBemClassificacaoFormaProducaoValorList().size() > 0) {
			// adicionar itens com valores especificos
			if (destino.getBemClassificacaoFormaProducaoItemList() == null) {
				destino.setBemClassificacaoFormaProducaoItemList(new ArrayList<>());
			}

			List<FormaProducaoItem> itemList = new ArrayList<>();

			for (BemClassificacaoFormaProducaoValor valor : origem.getBemClassificacaoFormaProducaoValorList()) {
				FormaProducaoItem formaProducaoItemEncontrou = null;
				for (FormaProducaoItem item : itemList) {
					if (valor.getFormaProducaoValor().getFormaProducaoItem().getId().equals(item.getId())) {
						formaProducaoItemEncontrou = item;
						break;
					}
				}
				if (formaProducaoItemEncontrou == null) {
					formaProducaoItemEncontrou = new FormaProducaoItem(valor.getFormaProducaoValor().getFormaProducaoItem().getId(), valor.getFormaProducaoValor().getFormaProducaoItem().getNome());
					formaProducaoItemEncontrou.setFormaProducaoValorList(new ArrayList<>());
					itemList.add(formaProducaoItemEncontrou);
				}
				
				boolean e = false;
				for (FormaProducaoValor f: formaProducaoItemEncontrou.getFormaProducaoValorList()) {
					if (valor.getFormaProducaoValor().getId().equals(f.getId())) {
						e = true;
						break;
					}
				}
				if (!e) {
					formaProducaoItemEncontrou.getFormaProducaoValorList().add(new FormaProducaoValor(valor.getFormaProducaoValor().getId(), valor.getFormaProducaoValor().getNome()));
				}				
			}
			for (FormaProducaoItem item: itemList) {
				boolean e = false;
				for (BemClassificacaoFormaProducaoItem ii: destino.getBemClassificacaoFormaProducaoItemList()) {
					if (ii.getFormaProducaoItem().getId().equals(item.getId())) {
						ii.setFormaProducaoItem(item);
						e = true;
						break;
					}
				}
				if (!e) {
					BemClassificacaoFormaProducaoItem bcfpi = new BemClassificacaoFormaProducaoItem();
					bcfpi.setFormaProducaoItem(item);
					destino.getBemClassificacaoFormaProducaoItemList().add(bcfpi);
				}
			}
		}
		
		destino.setFormula(origem.getFormula() != null ? origem.getFormula() : destino.getFormula());
		destino.setItemANome(origem.getItemANome() != null ? infoBasicaReg(origem.getItemANome()) : destino.getItemANome());
		destino.setItemBNome(origem.getItemBNome() != null ? infoBasicaReg(origem.getItemBNome()) : destino.getItemBNome());
		destino.setItemCNome(origem.getItemCNome() != null ? infoBasicaReg(origem.getItemCNome()) : destino.getItemCNome());
		destino.setUnidadeMedida(origem.getUnidadeMedida() != null ? infoBasicaReg(origem.getUnidadeMedida()) : destino.getUnidadeMedida());
	}

	private void ajustaLista(List<BemClassificacao> lista, List<BemClassificacao> matriz) {
		if (lista == null) {
			return;
		}
		for (BemClassificacao bemClassificacao : lista) {
			for (BemClassificacao bemClassificacaoMatriz : matriz) {
				if (bemClassificacao.getId().equals(bemClassificacaoMatriz.getId())) {
					acumulaBemClassificacao(bemClassificacaoMatriz, bemClassificacao, false);
					ajustaLista(bemClassificacao.getBemClassificacaoList(), matriz);
				}
			}
		}
	}

	@Cacheable("BemClassificacaoMatriz")
	public BemClassificacaoCadDto geraMatriz() {
		BemClassificacaoCadDto result = new BemClassificacaoCadDto();

		List<BemClassificacao> bemClassificacaoMatrizList = null;
		List<BemClassificacao> bemClassificacaoList = null;

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
						acumulaBemClassificacao(bemClassificacaoPai, item, true);
					}
				}
				// captar o dado principal
				acumulaBemClassificacao(bemClassificacao, item, true);

				if (bemClassificacaoMatrizList == null) {
					bemClassificacaoMatrizList = new ArrayList<>();
				}

				item.setUnidadeMedida(infoBasicaReg(item.getUnidadeMedida()));
				item.setItemANome(infoBasicaReg(item.getItemANome()));
				item.setItemBNome(infoBasicaReg(item.getItemBNome()));
				item.setItemCNome(infoBasicaReg(item.getItemCNome()));
				item.setBemClassificacaoFormaProducaoItemList(infoBasicaList(item.getBemClassificacaoFormaProducaoItemList()));
				item.setBemClassificacaoFormaProducaoValorList(infoBasicaList(item.getBemClassificacaoFormaProducaoValorList()));
				
				// teste
				item.setBemClassificadoList(infoBasicaList(bemClassificacao.getBemClassificadoList()));

				bemClassificacaoMatrizList.add(item);

				if (bemClassificacao.getBemClassificacao() == null) {
					if (bemClassificacaoList == null) {
						bemClassificacaoList = new ArrayList<>();
					}
					bemClassificacaoList.add(bemClassificacao.infoBasica());
				}
			}
		}

		result.setBemClassificacaoMatrizList(bemClassificacaoMatrizList);
		ajustaLista(bemClassificacaoList, bemClassificacaoMatrizList);
		result.setBemClassificacaoList(bemClassificacaoList);

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
