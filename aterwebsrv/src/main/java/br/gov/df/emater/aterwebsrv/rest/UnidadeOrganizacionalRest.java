package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.modelo.dto.UnidadeOrganizacionalCadFiltroDto;

@RestController
@RequestMapping("/unidade-organizacional")
public class UnidadeOrganizacionalRest {

	@Autowired
	private FacadeBo facadeBo;

	public UnidadeOrganizacionalRest() {
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	@Transactional(readOnly = true)
	public Resposta filtroExecutar(@RequestBody UnidadeOrganizacionalCadFiltroDto filtro, Principal usuario) {
		try {
			return new Resposta(facadeBo.unidadeOrganizacionalFiltroExecutar(usuario, filtro).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}


}