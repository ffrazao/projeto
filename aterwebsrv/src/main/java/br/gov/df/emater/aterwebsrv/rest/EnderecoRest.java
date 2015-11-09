package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
	@Transactional(readOnly = true)
	public Resposta novo(Principal usuario) {
		try {
			return new Resposta(facadeBo.enderecoNovo(usuario).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}
	
}