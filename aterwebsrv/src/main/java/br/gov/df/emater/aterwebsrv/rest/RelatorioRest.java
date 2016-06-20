package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;

@RestController
@RequestMapping("/relatorio")
public class RelatorioRest {

	@Autowired
	private FacadeBo facadeBo;

	public RelatorioRest() {
	}

	@RequestMapping(value = "/compilar", method = RequestMethod.GET)
	public Resposta compilar(@RequestParam(value = "nome", required = false) String nome, Principal usuario) throws Exception {
		return new Resposta(facadeBo.relatorioCompilar(usuario, nome).getResposta());
	}

}