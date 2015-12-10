package br.gov.df.emater.aterwebsrv.bo.bem_classificacao;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.modelo.dto.BemClassificacaoCadFiltroDto;

@Service("BemClassificacaoFiltroNovoCmd")
public class FiltroNovoCmd implements Command {

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		BemClassificacaoCadFiltroDto filtro = new BemClassificacaoCadFiltroDto();
		context.put("resultado", filtro);
		return false;
	}

}
