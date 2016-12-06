package br.gov.df.emater.aterwebsrv.bo_planejamento.planejamento;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.dto.ater.PropriedadeRuralCadFiltroDto;

@Service("PropriedadeRuralFiltroNovoCmd")
public class FiltroNovoCmd implements Command {

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		PropriedadeRuralCadFiltroDto filtro = new PropriedadeRuralCadFiltroDto();
		context.put("resultado", filtro);
		return false;
	}

}
