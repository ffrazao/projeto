package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.modelo.dto.IndiceProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;

@Service("IndiceProducaoFiltrarCmd")
public class FiltrarCmd extends _Comando {
	@Autowired
	private ProducaoDao dao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		IndiceProducaoCadFiltroDto filtro = (IndiceProducaoCadFiltroDto) contexto.getRequisicao();
		List<Producao> result = null;
		result = (List<Producao>) dao.filtrar(filtro);

		// fetch no resultado
		if (filtro.getId() != null) {
			if (result != null && result.size() == 1) {
				result = new ArrayList<Producao>();
				// result.add(fetchProdutor(result.get(0)).toArray());
			}
		} else {
			// result = fetch(result);
		}

		contexto.setResposta(result);
		return false;
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
