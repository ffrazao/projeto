package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.modelo.dto.FuncionalidadeCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Comando;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Funcionalidade;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Modulo;

@RestController
@RequestMapping("/funcionalidade")
public class FuncionalidadeRest {

	@Autowired
	private FacadeBo facadeBo;

	public FuncionalidadeRest() {
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	public Resposta editar(@RequestBody Funcionalidade funcionalidade, Principal usuario) throws Exception {
		return salvar(funcionalidade, usuario);
	}

	@RequestMapping(value = "/excluir", method = RequestMethod.DELETE)
	public Resposta excluir(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.funcionalidadeExcluir(usuario, id).getResposta());
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	public Resposta filtroExecutar(@RequestBody FuncionalidadeCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.funcionalidadeFiltroExecutar(usuario, filtro).getResposta());
	}

	@RequestMapping("/filtro-novo")
	public Resposta filtroNovo(Principal usuario) throws Exception {
		return new Resposta(facadeBo.funcionalidadeFiltroNovo(usuario).values());
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	public Resposta incluir(@RequestBody Funcionalidade funcionalidade, Principal usuario) throws Exception {
		return salvar(funcionalidade, usuario);
	}

	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	public Resposta novo(@RequestParam(value = "id", required = false) Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.funcionalidadeNovo(usuario, id == null ? null : new Funcionalidade(id)).getResposta());
	}

	public Resposta salvar(@RequestBody Funcionalidade funcionalidade, Principal usuario) throws Exception {
		return new Resposta(facadeBo.funcionalidadeSalvar(usuario, funcionalidade).getResposta());
	}

	@RequestMapping(value = "/salvar-comando", method = RequestMethod.POST)
	public Resposta salvarComando(@RequestBody Comando comando, Principal usuario) throws Exception {
		return new Resposta(facadeBo.funcionalidadeSalvarComando(usuario, comando).getResposta());
	}

	@RequestMapping(value = "/salvar-modulo", method = RequestMethod.POST)
	public Resposta salvarModulo(@RequestBody Modulo modulo, Principal usuario) throws Exception {
		return new Resposta(facadeBo.funcionalidadeSalvarModulo(usuario, modulo).getResposta());
	}

	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	public Resposta visualizar(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.funcionalidadeVisualizar(usuario, id).getResposta());
	}

}