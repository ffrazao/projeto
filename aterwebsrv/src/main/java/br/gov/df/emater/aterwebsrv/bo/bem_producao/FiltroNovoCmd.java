package br.gov.df.emater.aterwebsrv.bo.bem_producao;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.dto.BemProducaoCadFiltroDto;

@Service("BemProducaoFiltroNovoCmd")
public class FiltroNovoCmd implements Command {

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		BemProducaoCadFiltroDto filtro = new BemProducaoCadFiltroDto();
		context.put("resultado", filtro);
		return false;
	}

}
