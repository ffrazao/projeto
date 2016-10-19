package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.BemClassificacaoCadDto;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ItemNomeResultado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;

@Service("IndiceProducaoFiltrarTotalizadorCmd")
public class FiltrarTotalizadorCmd extends _Comando {

	@Autowired
	private BemClassificacaoUtilSrv utilSrv;

	private BemClassificacaoCadDto matriz;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		List<ProducaoProprietario> resposta = (List<ProducaoProprietario>) contexto.getResposta();

		// captar a matriz da classificação dos bens de produção
		if (matriz == null) {
			matriz = utilSrv.geraMatriz();
		}

		if (!CollectionUtils.isEmpty(resposta)) {
			Map<String, Producao> total = null;
			for (ProducaoProprietario producaoProprietario : resposta) {
				total = totaliza(producaoProprietario);
				producaoProprietario.getProducaoList().add(total.get("Esperado"));
				producaoProprietario.getProducaoList().add(total.get("Acumulado"));
				producaoProprietario.getProducaoList().add(total.get("Confirmado"));
			}
		}
		return false;
	}

	private Map<String, Producao> totaliza(ProducaoProprietario producaoProprietario) {
		Map<String, Producao> result = new HashMap<>();

		result.put("Esperado", new Producao());
		result.put("Acumulado", new Producao());
		result.put("Confirmado", new Producao());

		result.get("Esperado").setSituacao("Total Esperado");
		result.get("Acumulado").setSituacao("Total Acumulado");
		result.get("Confirmado").setSituacao("Total Confirmado");

		// captar tipo do item a ser somado
		BemClassificacao bemClassificacao = matriz.getBemClassificacaoMatrizList().stream().filter(m -> m.getId().equals(producaoProprietario.getBemClassificado().getBemClassificacao().getId())).findFirst().get();

		// verificar se tem produção vinculada
		if (!CollectionUtils.isEmpty(producaoProprietario.getProducaoProprietarioList())) {
			Map<String, Producao> totalAcumulado = new HashMap<>();
			for (ProducaoProprietario producaoProprietarioAcumulado : producaoProprietario.getProducaoProprietarioList()) {
				totalAcumulado = totaliza(producaoProprietarioAcumulado);
				
				producaoProprietarioAcumulado.getProducaoList().add(totalAcumulado.get("Esperado"));
				//producaoProprietarioAcumulado.getProducaoList().add(totalAcumulado.get("Acumulado"));
				producaoProprietarioAcumulado.getProducaoList().add(totalAcumulado.get("Confirmado"));
				
				calculaSoma(bemClassificacao, result.get("Acumulado"), totalAcumulado.get("Esperado"));
				calculaSoma(bemClassificacao, result.get("Confirmado"), totalAcumulado.get("Confirmado"));
			}
		}

		Integer esperadoTot = 0;
		Integer confirmadoTot = 0;

		for (Producao producao : producaoProprietario.getProducaoList()) {
			Producao esperado = result.get("Esperado");
			Producao confirmado = result.get("Confirmado");

			// contabilizar o item
			esperadoTot++;
			calculaSoma(bemClassificacao, esperado, producao);
			// contabilizar o item caso esteja confirmado
			if (producao.getDataConfirmacao() != null) {
				confirmadoTot++;
				calculaSoma(bemClassificacao, confirmado, producao);
			}
		}

		calculaMedia(bemClassificacao, result.get("Esperado"), esperadoTot);
		calculaMedia(bemClassificacao, result.get("Confirmado"), confirmadoTot);

		return result;
	}

	private void calculaMedia(BemClassificacao bemClassificacao, Producao producao, Integer total) {
		if (total == null || total <= 0) {
			return;
		}
		if (producao.getItemAValor() != null && bemClassificacao.getItemANome() != null && ItemNomeResultado.M.equals(bemClassificacao.getItemANome().getResultado())) {
			producao.setItemAValor(producao.getItemAValor().divide(new BigDecimal(total), RoundingMode.HALF_UP));
		}
		if (producao.getItemBValor() != null && bemClassificacao.getItemBNome() != null && ItemNomeResultado.M.equals(bemClassificacao.getItemBNome().getResultado())) {
			producao.setItemBValor(producao.getItemBValor().divide(new BigDecimal(total), RoundingMode.HALF_UP));
		}
		if (producao.getItemCValor() != null && bemClassificacao.getItemCNome() != null && ItemNomeResultado.M.equals(bemClassificacao.getItemCNome().getResultado())) {
			producao.setItemCValor(producao.getItemCValor().divide(new BigDecimal(total), RoundingMode.HALF_UP));
		}
	}

	private void calculaSoma(BemClassificacao bemClassificacao, Producao acumulado, Producao valor) {
		// preparar item acumulado
		if (acumulado.getItemAValor() == null) {
			acumulado.setItemAValor(new BigDecimal("0"));
		}
		if (acumulado.getItemBValor() == null) {
			acumulado.setItemBValor(new BigDecimal("0"));
		}
		if (acumulado.getItemCValor() == null) {
			acumulado.setItemCValor(new BigDecimal("0"));
		}
		if (acumulado.getVolume() == null) {
			acumulado.setVolume(new BigDecimal("0"));
		}
		if (acumulado.getValorUnitario() == null) {
			acumulado.setValorUnitario(new BigDecimal("0"));
		}
		if (acumulado.getValorTotal() == null) {
			acumulado.setValorTotal(new BigDecimal("0"));
		}

		// acumular valores
		if (valor.getItemAValor() != null && bemClassificacao.getItemANome() != null) {
			if (!ItemNomeResultado.N.equals(bemClassificacao.getItemANome().getResultado())) {
				acumulado.setItemAValor(acumulado.getItemAValor().add(valor.getItemAValor()));
			}
		}
		if (valor.getItemBValor() != null && bemClassificacao.getItemBNome() != null) {
			if (!ItemNomeResultado.N.equals(bemClassificacao.getItemBNome().getResultado())) {
				acumulado.setItemBValor(acumulado.getItemBValor().add(valor.getItemBValor()));
			}
		}
		if (valor.getItemCValor() != null && bemClassificacao.getItemCNome() != null) {
			if (!ItemNomeResultado.N.equals(bemClassificacao.getItemCNome().getResultado())) {
				acumulado.setItemCValor(acumulado.getItemCValor().add(valor.getItemCValor()));
			}
		}

	}

}