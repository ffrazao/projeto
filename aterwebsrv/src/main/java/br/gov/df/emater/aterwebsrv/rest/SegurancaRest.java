package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;

@RestController
public class SegurancaRest {
	
	@Autowired
	private FacadeBo facadeBo;

	@RequestMapping("/login")
	public Resposta login(Principal usuario) {
		try {
			return new Resposta(facadeBo.pessoaFiltroExecutar(usuario, null));
		} catch (Exception e) {
			return new Resposta(e);
		}
	}
	
}