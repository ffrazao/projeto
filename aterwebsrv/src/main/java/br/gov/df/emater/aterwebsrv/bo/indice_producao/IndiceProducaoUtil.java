package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;

public final class IndiceProducaoUtil {

	public static String getComposicaoValorId(Producao producao) {
		List<Integer> lista = new ArrayList<Integer>();
		if (!CollectionUtils.isEmpty(producao.getProducaoComposicaoList())) {
			producao.getProducaoComposicaoList().forEach((composicao)-> lista.add(composicao.getFormaProducaoValor().getId()));
		}
		Collections.sort(lista);
		return CollectionUtils.isEmpty(lista) ? "" : UtilitarioString.collectionToString(lista);
	}

}
