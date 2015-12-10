package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.modelo.dto.BemClassificacaoCadFiltroDto;

@RestController
@RequestMapping("/bem-classificacao")
public class BemClassificacaoRest {

	@Autowired
	private FacadeBo facadeBo;

	public BemClassificacaoRest() {
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	@Transactional(readOnly = true)
	public Resposta filtroExecutar(@RequestBody BemClassificacaoCadFiltroDto filtro, Principal usuario) {
		try {
			return new Resposta(facadeBo.bemClassificacaoFiltroExecutar(usuario, filtro).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

}