package br.gov.df.emater.aterwebsrv.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;

@Controller
public class SegurancaRest {

	@Autowired
	private FacadeBo facadeBo;

	@RequestMapping("/login")
	public String login() {
		return "/login";
	}

}