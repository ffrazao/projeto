package br.gov.df.emater.aterwebsrv.rest_planejamento;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo_planejamento.FacadeBoPlanejamento;
import br.gov.df.emater.aterwebsrv.dto_planejamento.PlanejamentoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.rest.Resposta;

@RestController
@RequestMapping("/planejamento")
public class PlanejamentoRest {

	@Autowired
	private FacadeBoPlanejamento facadeBo;

	public PlanejamentoRest() {
	}

	@RequestMapping(value = "/retorna-meta-tatica", method = RequestMethod.POST)
	public Resposta retoraMetaTatica(@RequestBody PlanejamentoCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.planejamentoRetoraMetaTatica(usuario, filtro).getResposta());
	}

}