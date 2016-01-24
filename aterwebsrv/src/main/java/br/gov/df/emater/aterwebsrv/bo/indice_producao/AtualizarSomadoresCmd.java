package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoFormaDao;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ItemNomeResultado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Bem;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoFormaComposicao;

@Service("AtualizarSomadoresCmd")
public class AtualizarSomadoresCmd extends _Comando {

	@Autowired
	private ProducaoDao dao;

	@Autowired
	private ProducaoFormaDao producaoFormaDao;

	private BigDecimal ajustaItem(String item, BemClassificacao bemClassificacao, BigDecimal novo, BigDecimal atual, Integer divisor) {
		if (novo == null || novo.compareTo(BigDecimal.ZERO) == 0) {
			return atual;
		}
		if (atual == null) {
			atual = new BigDecimal(0);
		}
		ItemNomeResultado inr = getResultado(item, bemClassificacao);
		if (ItemNomeResultado.N.equals(inr)) {
			novo = atual;
		} else if (ItemNomeResultado.M.equals(inr)) {
			novo = novo.divide(new BigDecimal(divisor));
		}
		BigDecimal result = novo.compareTo(atual) > 0 ? novo : atual;
		return result.compareTo(BigDecimal.ZERO) == 0 ? null : result;
	}

	@SuppressWarnings("unchecked")
	private void calculaProducao(ProducaoForma producaoForma, List<Producao> producaoProdutorList, Map<ProducaoForma, Map<String, Object>> result) {

		for (Producao producao : producaoProdutorList) {
			for (ProducaoForma pfp : producao.getProducaoFormaList()) {
				if (isProducaoFormaIguais(producaoForma, pfp)) {
					Map<String, Object> items = result.get(producaoForma);
					if (items == null) {
						items = new HashMap<String, Object>();
						items.put("formaProducalTotais", 0);
						items.put("produtorTotais", new HashSet<Integer>());
						items.put("volumeTotais", new BigDecimal(0));
						items.put("valorTotais", new BigDecimal(0));
						items.put("itemATotais", new BigDecimal(0));
						items.put("itemBTotais", new BigDecimal(0));
						items.put("itemCTotais", new BigDecimal(0));
					}
					Integer f = (Integer) items.get("formaProducalTotais");
					f++;
					items.put("formaProducalTotais", f);

					((Set<Integer>) items.get("produtorTotais")).add(producao.getPublicoAlvo().getId());
					BigDecimal vlr = (BigDecimal) items.get("volumeTotais");
					vlr = vlr.add(pfp.getVolume() == null ? new BigDecimal(0) : pfp.getVolume());
					items.put("volumeTotais", vlr);

					vlr = (BigDecimal) items.get("valorTotais");
					vlr = vlr.add(pfp.getValorTotal() == null ? new BigDecimal(0) : pfp.getValorTotal());
					items.put("valorTotais", vlr);

					vlr = (BigDecimal) items.get("itemATotais");
					vlr = vlr.add(pfp.getItemAValor() == null ? new BigDecimal(0) : pfp.getItemAValor());
					items.put("itemATotais", vlr);

					vlr = (BigDecimal) items.get("itemBTotais");
					vlr = vlr.add(pfp.getItemBValor() == null ? new BigDecimal(0) : pfp.getItemBValor());
					items.put("itemBTotais", vlr);

					vlr = (BigDecimal) items.get("itemCTotais");
					vlr = vlr.add(pfp.getItemCValor() == null ? new BigDecimal(0) : pfp.getItemCValor());
					items.put("itemCTotais", vlr);

					result.put(producaoForma, items);
				}
			}
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer producaoId = (Integer) contexto.getResposta();

		Producao producao = dao.findOne(producaoId);

		if (producao != null) {
			Integer ano = producao.getAno();
			Bem bem = producao.getBem();
			Comunidade comunidade = null;

			// verificar o tipo de producao
			if (producao.getComunidade() != null) {
				// por comunidade
				comunidade = producao.getComunidade();
			} else {
				// por propriedade
				comunidade = producao.getPropriedadeRural().getComunidade();

				// captar o registro da producao principal
				producao = dao.findOneByAnoAndBemAndComunidadeAndPublicoAlvoIsNullAndPropriedadeRuralIsNull(ano, bem, comunidade);
				if (producao == null) {
					throw new BoException(String.format("Producao principal inexistente [%s, %s, %s]", ano, bem.getNome(), comunidade.getNome()));
				}
			}

			// encontrar os produtores do bem
			List<Producao> producaoProdutorList = dao.findByAnoAndBemAndPropriedadeRuralComunidade(ano, bem, comunidade);

			if (producaoProdutorList != null) {
				Map<ProducaoForma, Map<String, Object>> result = new HashMap<ProducaoForma, Map<String, Object>>();
				for (ProducaoForma producaoForma : producao.getProducaoFormaList()) {
					calculaProducao(producaoForma, producaoProdutorList, result);
				}

				// atualizar os totais
				for (ProducaoForma producaoForma : producao.getProducaoFormaList()) {
					Map<String, Object> items = result.get(producaoForma);
					if (items != null) {
						producaoForma.setQuantidadeProdutores(Math.max(((Set<Integer>) items.get("produtorTotais")).size(), producaoForma.getQuantidadeProdutores()));
						producaoForma.setVolume(new BigDecimal(Math.max(((BigDecimal) items.get("volumeTotais")).doubleValue(), producaoForma.getVolume() == null ? 0d : producaoForma.getVolume().doubleValue())));
						producaoForma.setValorTotal(new BigDecimal(Math.max(((BigDecimal) items.get("valorTotais")).doubleValue(), producaoForma.getValorTotal() == null ? 0d : producaoForma.getValorTotal().doubleValue())));
						producaoForma.setItemAValor(ajustaItem("A", bem.getBemClassificacao(), (BigDecimal) items.get("itemATotais"), producaoForma.getItemAValor(), (Integer) items.get("formaProducalTotais")));
						producaoForma.setItemBValor(ajustaItem("B", bem.getBemClassificacao(), (BigDecimal) items.get("itemBTotais"), producaoForma.getItemBValor(), (Integer) items.get("formaProducalTotais")));
						producaoForma.setItemCValor(ajustaItem("C", bem.getBemClassificacao(), (BigDecimal) items.get("itemCTotais"), producaoForma.getItemCValor(), (Integer) items.get("formaProducalTotais")));
						producaoFormaDao.save(producaoForma);
					}
				}
			}
		}

		return false;
	}

	private ItemNomeResultado getResultado(String item, BemClassificacao bemClassificacao) {
		if (bemClassificacao == null) {
			return null;
		}
		switch (item) {
		case "A":
			if (bemClassificacao.getItemANome() != null) {
				return bemClassificacao.getItemANome().getResultado();
			}
			break;
		case "B":
			if (bemClassificacao.getItemBNome() != null) {
				return bemClassificacao.getItemBNome().getResultado();
			}
			break;
		case "C":
			if (bemClassificacao.getItemCNome() != null) {
				return bemClassificacao.getItemCNome().getResultado();
			}
			break;
		}

		return getResultado(item, bemClassificacao.getBemClassificacao());
	}

	private boolean isProducaoFormaIguais(ProducaoForma a, ProducaoForma b) {
		if (a.getProducaoFormaComposicaoList().size() != b.getProducaoFormaComposicaoList().size()) {
			return false;
		}
		for (ProducaoFormaComposicao pfcA : a.getProducaoFormaComposicaoList()) {
			boolean achei = false;
			for (ProducaoFormaComposicao pfcB : b.getProducaoFormaComposicaoList()) {
				if (pfcA.getFormaProducaoValor().getId().equals(pfcB.getFormaProducaoValor().getId())) {
					achei = true;
					break;
				}
			}
			if (!achei) {
				return false;
			}
		}
		return true;
	}

}