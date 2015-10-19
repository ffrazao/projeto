package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.seguranca.UserAuthentication;

@RestController
@RequestMapping("/api")
public class SegurancaRest {

	@RequestMapping("/login")
	public void login() {
		System.out.println("Logando");
	}

	@RequestMapping("/acesso")
	public void acesso(WebRequest webRequest, Principal principal) {
		System.out.println(webRequest);
		Usuario usuario = (Usuario)((UserAuthentication) principal).getDetails();
		System.out.println(usuario);
	}

}