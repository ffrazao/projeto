package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.dto.funcional.ColaboradorListaDto;

@RestController
@RequestMapping("/colaborador")
public class ColaboradorRest {

	@Autowired
	private FacadeBo facadeBo;

	public ColaboradorRest() {
	}

	@RequestMapping(value = "/lista", method = RequestMethod.POST)
	public Resposta lista(@RequestBody ColaboradorListaDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.colaboradorLista(usuario, filtro).getResposta());
	}

}