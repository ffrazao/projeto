package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;

@RestController
public class UtilRest {

	@Autowired
	private FacadeBo facadeBo;

	@RequestMapping(value = "dominio", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Resposta getDominio(@RequestParam(required = true) String[] ent, @RequestParam(required = false) String npk, @RequestParam(required = false) String vpk, @RequestParam(required = false) String order, @RequestParam(required = false) String[] fetchs, Principal usuario)
			throws Exception {
		try {
			return new Resposta(facadeBo.dominio(usuario, ent, npk, vpk, order, fetchs).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}
}
