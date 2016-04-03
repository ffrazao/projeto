package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;

@RestController
@RequestMapping("/endereco")
public class EnderecoRest {

	@Autowired
	private FacadeBo facadeBo;

	public EnderecoRest() {
	}

	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	public Resposta novo(Principal usuario) throws Exception {
		return new Resposta(facadeBo.enderecoNovo(usuario).getResposta());
	}

}