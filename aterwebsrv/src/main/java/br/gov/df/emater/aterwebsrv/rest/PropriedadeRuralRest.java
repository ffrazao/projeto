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
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.dto.PropriedadeRuralCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dto.PropriedadeRuralPorComunidadePublicoAlvoDto;

@RestController
@RequestMapping("/propriedade-rural")
public class PropriedadeRuralRest {

	@Autowired
	private FacadeBo facadeBo;

	public PropriedadeRuralRest() {
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	@Transactional
	public Resposta editar(@RequestBody PropriedadeRural propriedadeRural, Principal usuario) {
		return salvar(propriedadeRural, usuario);
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	@Transactional(readOnly = true)
	public Resposta filtroExecutar(@RequestBody PropriedadeRuralCadFiltroDto filtro, Principal usuario) {
		try {
			return new Resposta(facadeBo.propriedadeRuralFiltroExecutar(usuario, filtro).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping("/filtro-novo")
	@Transactional(readOnly = true)
	public Resposta filtroNovo(Principal usuario) {
		try {
			return new Resposta(facadeBo.propriedadeRuralFiltroNovo(usuario).values());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	@Transactional
	public Resposta incluir(@RequestBody PropriedadeRural propriedadeRural, Principal usuario) {
		return salvar(propriedadeRural, usuario);
	}

	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Resposta novo(Principal usuario) {
		try {
			return new Resposta(facadeBo.propriedadeRuralNovo(usuario).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@Transactional
	public Resposta salvar(@RequestBody PropriedadeRural propriedadeRural, Principal usuario) {
		try {
			return new Resposta(facadeBo.propriedadeRuralSalvar(usuario, propriedadeRural).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Resposta visualizar(@RequestParam Integer id, Principal usuario) {
		try {
			return new Resposta(facadeBo.propriedadeRuralVisualizar(usuario, id).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping(value = "/filtrar-por-publico-alvo-comunidade", method = RequestMethod.POST)
	@Transactional(readOnly = true)
	public Resposta propriedadeRuralFiltrarPorPublicoAlvoEComunidade(@RequestBody PropriedadeRuralPorComunidadePublicoAlvoDto filtro, Principal usuario) {
		try {
			return new Resposta(facadeBo.propriedadeRuralFiltrarPorPublicoAlvoEComunidade(usuario, filtro).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}
	
}