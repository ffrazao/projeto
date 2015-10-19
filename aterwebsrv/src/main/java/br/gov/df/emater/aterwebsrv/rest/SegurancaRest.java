package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.seguranca.UserAuthentication;

@RestController
@RequestMapping("/api")
public class SegurancaRest {

	@RequestMapping("/login")
	public void login() {
		System.out.println("Logando");
	}

	@RequestMapping(value = "/acesso", method = RequestMethod.GET)
	public void acesso(@RequestParam String funcionalidade, Principal principal) {
		Usuario usuario = (Usuario)((UserAuthentication) principal).getDetails();
		if (!usuario.getFuncionalidadeComandoList().containsKey(funcionalidade)) {
			throw new BadCredentialsException("Recurso não disponível");
		}
	}

}