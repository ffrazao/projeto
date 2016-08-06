package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.dto.indice_producao.IndiceProducaoCadFiltroDto;

@Service("IndiceProducaoFiltroNovoCmd")
public class FiltroNovoCmd implements Command {

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		IndiceProducaoCadFiltroDto filtro = new IndiceProducaoCadFiltroDto();
		context.put("resultado", filtro);
		return false;
	}

}
