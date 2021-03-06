package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.dto.ater.ComunidadeListaDto;

@RestController
@RequestMapping("/comunidade")
public class ComunidadeRest {

	@Autowired
	private FacadeBo facadeBo;

	public ComunidadeRest() {
	}

	@RequestMapping(value = "/lista", method = RequestMethod.POST)
	public Resposta lista(@RequestBody ComunidadeListaDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.comunidadeLista(usuario, filtro).getResposta());
	}

}