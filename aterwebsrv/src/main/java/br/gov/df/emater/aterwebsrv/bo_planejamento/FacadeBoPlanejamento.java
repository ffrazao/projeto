package br.gov.df.emater.aterwebsrv.bo_planejamento;

import java.security.Principal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dto_planejamento.PlanejamentoCadFiltroDto;

@Service("FacadeBoPlanejamento")
public class FacadeBoPlanejamento extends FacadeBo {

	@Transactional
	public _Contexto planejamentoRetoraMetaTatica(Principal usuario, PlanejamentoCadFiltroDto filtro) throws Exception {
		return this._executar(usuario, "PlanejamentoRetornaMetaTaticaCmd", filtro);
	}

}