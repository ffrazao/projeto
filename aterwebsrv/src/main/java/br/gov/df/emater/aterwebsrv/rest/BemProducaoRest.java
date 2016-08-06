package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.dto.BemProducaoCadFiltroDto;

@RestController
@RequestMapping("/bem-producao")
public class BemProducaoRest {

	@Autowired
	private FacadeBo facadeBo;

	public BemProducaoRest() {
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	public Resposta filtroExecutar(@RequestBody BemProducaoCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.bemProducaoFiltroExecutar(usuario, filtro).getResposta());
	}

}