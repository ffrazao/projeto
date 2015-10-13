package br.gov.df.emater.aterwebsrv.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SegurancaRest {

	@RequestMapping("/login")
	public void login() {
		System.out.println("Logando");
	}

}