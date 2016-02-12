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
import br.gov.df.emater.aterwebsrv.modelo.atividade.Atividade;
import br.gov.df.emater.aterwebsrv.modelo.dto.AtividadeCadFiltroDto;

@RestController
@RequestMapping("/atividade")
public class AtividadeRest {

	@Autowired
	private FacadeBo facadeBo;

	public AtividadeRest() {
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	@Transactional
	public Resposta editar(@RequestBody Atividade atividade, Principal usuario) {
		return salvar(atividade, usuario);
	}

	@RequestMapping(value = "/excluir", method = RequestMethod.POST)
	@Transactional
	public Resposta excluir(@RequestBody Atividade atividade, Principal usuario) {
		try {
			return new Resposta(facadeBo.atividadeExcluir(usuario, atividade).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	@Transactional(readOnly = true)
	public Resposta filtroExecutar(@RequestBody AtividadeCadFiltroDto filtro, Principal usuario) {
		try {
			return new Resposta(facadeBo.atividadeFiltroExecutar(usuario, filtro).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping("/filtro-novo")
	@Transactional(readOnly = true)
	public Resposta filtroNovo(Principal usuario) {
		try {
			return new Resposta(facadeBo.atividadeFiltroNovo(usuario).values());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	@Transactional
	public Resposta incluir(@RequestBody Atividade atividade, Principal usuario) {
		return salvar(atividade, usuario);
	}

	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Resposta novo(@RequestParam(value = "id", required = false) Integer id, Principal usuario) {
		try {
			return new Resposta(facadeBo.atividadeNovo(usuario, id == null ? null : new Atividade(id)).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@Transactional
	public Resposta salvar(@RequestBody Atividade atividade, Principal usuario) {
		try {
			return new Resposta(facadeBo.atividadeSalvar(usuario, atividade).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Resposta visualizar(@RequestParam Integer id, Principal usuario) {
		try {
			return new Resposta(facadeBo.atividadeVisualizar(usuario, id).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

}