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

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.dao.ferramenta.UtilDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioNumero;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FormulaProduto;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ItemNomeResultado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ItemNome;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoFormaComposicao;

@Service("ProducaoCalculo")
public class ProducaoCalculo {

	private BemClassificacao bemClassificacao;

	@Autowired
	private ProducaoDao dao;

	private Map<String, Object[]> mapaDeProducaoForma = new HashMap<String, Object[]>();

	private Producao producao;

	private List<Producao> producaoProdutorList;

	@Autowired
	private UtilDao utilDao;

	public void abreProducao(Integer producaoId) {
		// recuperar a produção que foi salva
//		Producao producao = dao.findOne(producaoId);
//
//		// verificar o tipo de producao
//		if (producao.getComunidade() == null) {
//			// captar o registro da producao principal
//			Producao producaoPrincipal = dao.findOneByAnoAndBemAndComunidadeAndPublicoAlvoIsNullAndPropriedadeRuralIsNull(producao.getAno(), producao.getBem(), producao.getPropriedadeRural().getComunidade());
//			if (producaoPrincipal == null) {
//				throw new BoException(String.format("Producao principal inexistente [%s, %s, %s]", producao.getAno(), producao.getBem().getNome(), producao.getPropriedadeRural().getComunidade().getNome()));
//			}
//			producao = producaoPrincipal;
//		}
//
//		// encontrar os produtores do bem
//		List<Producao> producaoProdutorList = dao.findByAnoAndBemAndPropriedadeRuralComunidade(producao.getAno(), producao.getBem(), producao.getComunidade());
//
//		this.producao = producao;
//		this.producaoProdutorList = producaoProdutorList;
//		Map<String, Object> bemClassificacaoDetalhes = utilDao.ipaBemClassificacaoDetalhes(producao.getBem().getBemClassificacao());
//		this.bemClassificacao = new BemClassificacao();
//		this.bemClassificacao.setFormula((FormulaProduto) bemClassificacaoDetalhes.get("formulaProduto"));
//		this.bemClassificacao.setItemANome((ItemNome) bemClassificacaoDetalhes.get("itemA"));
//		this.bemClassificacao.setItemBNome((ItemNome) bemClassificacaoDetalhes.get("itemB"));
//		this.bemClassificacao.setItemCNome((ItemNome) bemClassificacaoDetalhes.get("itemC"));
	}

	@SuppressWarnings("unchecked")
	public void calcular(boolean calcularConfirmadas) {
		if (getProducaoProdutorList() != null) {
			// alualizar as produções
			for (Producao producao : getProducaoProdutorList()) {

				for (ProducaoForma producaoForma : producao.getProducaoFormaList()) {
					try {
						producaoForma.setVolume(getBemClassificacao().getFormula().volume(producaoForma));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					producaoForma.setValorTotal(producaoForma.getVolume().multiply(producaoForma.getValorUnitario(), UtilitarioNumero.BIG_DECIMAL_PRECISAO));
					producaoForma.setQuantidadeProdutores(null);
					producaoFormaAcumula(producaoForma, calcularConfirmadas);
				}

			}
		}

		// atualizar as previsôes de produção
		for (ProducaoForma producaoForma : getProducao().getProducaoFormaList()) {

			String composicao = getComposicao(producaoForma);

			if (!getMapaDeProducaoForma().containsKey(composicao)) {
				continue;
			}

			Object[] mapa = getMapaDeProducaoForma().get(composicao);

			ProducaoForma producaoFormaCalculada = (ProducaoForma) mapa[0];
			ProducaoForma producaoFormaCalculadaConfirmada = (ProducaoForma) mapa[2];

			atualizaProducaoFormaCalculada(producaoFormaCalculada);
			
			atualizaProducaoFormaCalculada(producaoFormaCalculadaConfirmada);

			// atualizar dados
			if (producaoForma.getItemAValor() != null) {
				producaoForma.setItemAValor(producaoForma.getItemAValor().max(producaoFormaCalculada.getItemAValor()));
			}
			if (producaoForma.getItemBValor() != null) {
				producaoForma.setItemBValor(producaoForma.getItemBValor().max(producaoFormaCalculada.getItemBValor()));
			}
			if (producaoForma.getItemCValor() != null) {
				producaoForma.setItemCValor(producaoForma.getItemCValor().max(producaoFormaCalculada.getItemCValor()));
			}
			if (producaoForma.getValorUnitario() != null) {
				producaoForma.setValorUnitario(producaoForma.getValorUnitario().max(producaoFormaCalculada.getValorUnitario()));
			}

			try {
				producaoForma.setVolume(getBemClassificacao().getFormula().volume(producaoForma));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			// calcular o preço médio ponderado
			producaoForma.setValorTotal(producaoForma.getVolume().multiply(producaoForma.getValorUnitario(), UtilitarioNumero.BIG_DECIMAL_PRECISAO));

			Set<Integer> produtoresList = (Set<Integer>) mapa[1];
			producaoForma.setQuantidadeProdutores(produtoresList.size());
			producaoForma.setDataConfirmacao(null);
		}
	}

	private void atualizaProducaoFormaCalculada(ProducaoForma producaoFormaCalculada) {
		if (ItemNomeResultado.M.equals(getBemClassificacao().getItemANome().getResultado())) {
			// calcular a média ponderada
			if (producaoFormaCalculada.getVolume().compareTo( BigDecimal.ZERO) == 0) {
				producaoFormaCalculada.setItemBValor(BigDecimal.ZERO);
			} else {				
				producaoFormaCalculada.setItemBValor(producaoFormaCalculada.getItemAValor().divide(producaoFormaCalculada.getVolume(), UtilitarioNumero.BIG_DECIMAL_PRECISAO));
			}
		}
		if (ItemNomeResultado.M.equals(getBemClassificacao().getItemBNome().getResultado())) {
			// calcular a média ponderada
			if (producaoFormaCalculada.getVolume().compareTo( BigDecimal.ZERO) == 0) {
				producaoFormaCalculada.setItemAValor(BigDecimal.ZERO);
			} else {				
				producaoFormaCalculada.setItemAValor(producaoFormaCalculada.getItemBValor().divide(producaoFormaCalculada.getVolume(), UtilitarioNumero.BIG_DECIMAL_PRECISAO));
			}
		}

		try {
			producaoFormaCalculada.setVolume(getBemClassificacao().getFormula().volume(producaoFormaCalculada));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// calcular o preço médio ponderado
		if (producaoFormaCalculada.getValorTotal().compareTo(BigDecimal.ZERO) == 0) {
			producaoFormaCalculada.setValorUnitario(BigDecimal.ZERO);
		} else {			
			producaoFormaCalculada.setValorUnitario(producaoFormaCalculada.getVolume().divide(producaoFormaCalculada.getValorTotal(), UtilitarioNumero.BIG_DECIMAL_PRECISAO));
		}
	}

	public BemClassificacao getBemClassificacao() {
		return bemClassificacao;
	}

	public String getComposicao(ProducaoForma producaoForma) {
		List<Integer> idList = new ArrayList<Integer>();
		for (ProducaoFormaComposicao composicao : producaoForma.getProducaoFormaComposicaoList()) {
			idList.add(composicao.getFormaProducaoValor().getId());
		}
		Collections.sort(idList);
		return UtilitarioString.collectionToString(idList);
	}

	public Map<String, Object[]> getMapaDeProducaoForma() {
		return mapaDeProducaoForma;
	}

	public Producao getProducao() {
		return producao;
	}

	public List<Producao> getProducaoProdutorList() {
		return producaoProdutorList;
	}

	@SuppressWarnings("unchecked")
	public void producaoFormaAcumula(ProducaoForma producaoForma, boolean calcularConfirmadas) {
		String composicao = getComposicao(producaoForma);

		ProducaoForma pf = null;
		Set<Integer> publicoAlvoList = null;
		ProducaoForma pfConfirmadas = null;
		Set<Integer> publicoAlvoConfirmadasList = null;

		if (!mapaDeProducaoForma.containsKey(composicao)) {
			pf = producaoFormaCriar(producaoForma, false);
			pfConfirmadas = producaoFormaCriar(producaoForma, calcularConfirmadas);

			publicoAlvoList = new HashSet<Integer>();
			publicoAlvoConfirmadasList = new HashSet<Integer>();
		} else {
			pf = producaoFormaMesclar((ProducaoForma) mapaDeProducaoForma.get(composicao)[0], producaoForma, false);
			pfConfirmadas = producaoFormaMesclar((ProducaoForma) mapaDeProducaoForma.get(composicao)[2], producaoForma, calcularConfirmadas);

			publicoAlvoList = (Set<Integer>) mapaDeProducaoForma.get(composicao)[1];
			publicoAlvoConfirmadasList = (Set<Integer>) mapaDeProducaoForma.get(composicao)[3];
		}
		
		// acumular o publico alvo
		publicoAlvoList.add(producaoForma.getProducao().getPublicoAlvo().getId());
		if (calcularConfirmadas) {
			if (producaoForma.getDataConfirmacao() == null) {
				publicoAlvoConfirmadasList.add(producaoForma.getProducao().getPublicoAlvo().getId());
			}
		}
		mapaDeProducaoForma.put(composicao, new Object[] { pf, publicoAlvoList, pfConfirmadas, publicoAlvoConfirmadasList });
	}

	private ProducaoForma producaoFormaCriar(ProducaoForma producaoForma, boolean calcularConfirmadas) {
		ProducaoForma result = new ProducaoForma();
		if (calcularConfirmadas) {
			result.setItemAValor(producaoForma.getItemAValor() == null ? null : new BigDecimal("0"));
			result.setItemBValor(producaoForma.getItemBValor() == null ? null : new BigDecimal("0"));
			result.setItemCValor(producaoForma.getItemCValor() == null ? null : new BigDecimal("0"));
			result.setVolume(new BigDecimal("0"));
			result.setValorUnitario(new BigDecimal("0"));
			result.setValorTotal(new BigDecimal("0"));
			if (producaoForma.getDataConfirmacao() == null) {
				return result;
			}
		}
		result.setItemAValor(producaoForma.getItemAValor());
		result.setItemBValor(producaoForma.getItemBValor());
		result.setItemCValor(producaoForma.getItemCValor());
		result.setVolume(producaoForma.getVolume());
		result.setValorUnitario(producaoForma.getValorUnitario());
		result.setValorTotal(producaoForma.getValorTotal());
		return result;
	}

	private ProducaoForma producaoFormaMesclar(ProducaoForma result, ProducaoForma producaoForma, boolean calcularConfirmadas) {
		if (calcularConfirmadas) {
			if (producaoForma.getDataConfirmacao() == null) {
				return result;
			}
		}
		if (producaoForma.getItemAValor() != null) {
			result.setItemAValor(result.getItemAValor().add(producaoForma.getItemAValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO));
		}
		if (producaoForma.getItemBValor() != null) {
			result.setItemBValor(result.getItemBValor().add(producaoForma.getItemBValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO));
		}
		if (producaoForma.getItemCValor() != null) {
			result.setItemCValor(result.getItemCValor().add(producaoForma.getItemCValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO));
		}
		result.setVolume(result.getVolume().add(producaoForma.getVolume(), UtilitarioNumero.BIG_DECIMAL_PRECISAO));
		result.setValorUnitario(result.getValorUnitario().add(producaoForma.getValorUnitario(), UtilitarioNumero.BIG_DECIMAL_PRECISAO));
		result.setValorTotal(result.getValorTotal().add(producaoForma.getValorTotal(), UtilitarioNumero.BIG_DECIMAL_PRECISAO));
		return result;
	}
}