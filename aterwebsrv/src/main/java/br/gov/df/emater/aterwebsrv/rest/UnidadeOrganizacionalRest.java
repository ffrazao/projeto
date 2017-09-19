package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.dto.funcional.UnidadeOrganizacionalCadFiltroDto;

@RestController
@RequestMapping("/unidade-organizacional")
public class UnidadeOrganizacionalRest {

	@Autowired
	private FacadeBo facadeBo;

	public UnidadeOrganizacionalRest() {
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	public Resposta filtroExecutar(@RequestBody UnidadeOrganizacionalCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.unidadeOrganizacionalFiltroExecutar(usuario, filtro).getResposta());
	}

	@RequestMapping(value = "/lista", method = RequestMethod.POST)
	public Resposta lista(@RequestBody UnidadeOrganizacionalCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.unidadeOrganizacionalFiltroExecutar(usuario, filtro).getResposta());
	}

	@RequestMapping(value = "/empregado-por-unidade-organizacional", method = RequestMethod.GET)
	public Resposta empregadoPorUnidadeOrganizacional(Integer[] unidadeOrganizacionalIdList, Principal usuario) throws Exception {
		return new Resposta(facadeBo.unidadeOrganizacionalEmpregadoPorUnidadeOrganizacional(usuario, unidadeOrganizacionalIdList).getResposta());
	}

}