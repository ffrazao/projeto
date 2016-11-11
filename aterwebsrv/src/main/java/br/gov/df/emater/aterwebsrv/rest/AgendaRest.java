package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.dto.agenda.AgendaCadFiltroDto;

@RestController
@RequestMapping("/agenda")
public class AgendaRest {

	@Autowired
	private FacadeBo facadeBo;

	public AgendaRest() {
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	public Resposta filtroExecutar(@RequestBody AgendaCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.agendaFiltroExecutar(usuario, filtro).getResposta());
	}

	@RequestMapping(value = "/filtro-novo", method = RequestMethod.GET)
	public Resposta filtroNovo(@RequestParam(name = "opcao", required = false) String opcao, Principal usuario) throws Exception {
		return new Resposta(facadeBo.agendaFiltroNovo(usuario, opcao).getResposta());
	}

}