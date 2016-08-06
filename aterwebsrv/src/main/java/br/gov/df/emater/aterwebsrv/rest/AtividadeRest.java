package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.dto.AtividadeCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Atividade;

@RestController
@RequestMapping("/atividade")
public class AtividadeRest {

	@Autowired
	private FacadeBo facadeBo;

	public AtividadeRest() {
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	public Resposta editar(@RequestBody Atividade atividade, Principal usuario) throws Exception {
		return salvar(atividade, usuario);
	}

	@RequestMapping(value = "/excluir", method = RequestMethod.DELETE)
	public Resposta excluir(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.atividadeExcluir(usuario, id).getResposta());
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	public Resposta filtroExecutar(@RequestBody AtividadeCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.atividadeFiltroExecutar(usuario, filtro).getResposta());
	}

	@RequestMapping("/filtro-novo")
	public Resposta filtroNovo(Principal usuario) throws Exception {
		return new Resposta(facadeBo.atividadeFiltroNovo(usuario).values());
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	public Resposta incluir(@RequestBody Atividade atividade, Principal usuario) throws Exception {
		return salvar(atividade, usuario);
	}

	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	public Resposta novo(@RequestParam(value = "id", required = false) Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.atividadeNovo(usuario, id == null ? null : new Atividade(id)).getResposta());
	}

	public Resposta salvar(@RequestBody Atividade atividade, Principal usuario) throws Exception {
		return new Resposta(facadeBo.atividadeSalvar(usuario, atividade).getResposta());
	}

	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	public Resposta visualizar(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.atividadeVisualizar(usuario, id).getResposta());
	}

}