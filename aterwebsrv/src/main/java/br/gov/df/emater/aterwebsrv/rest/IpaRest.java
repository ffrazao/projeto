package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;

@RestController
@RequestMapping("/ipa")
public class IpaRest {

	@Autowired
	private FacadeBo facadeBo;

	public IpaRest() {
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	@Transactional
	public Resposta editar(@RequestBody Ipa ipa, Principal usuario) {
		return salvar(ipa, usuario);
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	@Transactional(readOnly = true)
	public Resposta filtroExecutar(@RequestBody IpaCadFiltroDto filtro, Principal usuario) {
		try {
			return new Resposta(facadeBo.ipaFiltroExecutar(usuario, filtro).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping("/filtro-novo")
	@Transactional(readOnly = true)
	public Resposta filtroNovo(Principal usuario) {
		try {
			return new Resposta(facadeBo.ipaFiltroNovo(usuario).values());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	@Transactional
	public Resposta incluir(@RequestBody Ipa ipa, Principal usuario) {
		return salvar(ipa, usuario);
	}

	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Resposta novo(@RequestParam("modelo") IpaTipo ipaTipo, Principal usuario) {
		try {
			return new Resposta(facadeBo.ipaNovo(usuario, ipaTipo).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@Transactional
	public Resposta salvar(@RequestBody Ipa ipa, Principal usuario) {
		try {
			return new Resposta(facadeBo.ipaSalvar(usuario, ipa).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Resposta visualizar(@RequestParam Integer id, Principal usuario) {
		try {
			return new Resposta(facadeBo.ipaVisualizar(usuario, id).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

}