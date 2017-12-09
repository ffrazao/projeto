package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBoImportar;
import br.gov.df.emater.aterwebsrv.rest.Resposta;

@RestController
public class ImportadorRest {

	@Autowired
	private FacadeBoImportar facadeBo;

	public ImportadorRest() {
	}

	@RequestMapping(value = "/importar")
	public Resposta filtroExecutar(Principal usuario) throws Exception {
		return new Resposta(facadeBo.importar(usuario).getResposta());
	}

}