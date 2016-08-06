package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.dto.PerfilCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Comando;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Modulo;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Perfil;

@RestController
@RequestMapping("/perfil")
public class PerfilRest {

	@Autowired
	private FacadeBo facadeBo;

	public PerfilRest() {
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	public Resposta editar(@RequestBody Perfil perfil, Principal usuario) throws Exception {
		return salvar(perfil, usuario);
	}

	@RequestMapping(value = "/excluir", method = RequestMethod.DELETE)
	public Resposta excluir(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.perfilExcluir(usuario, id).getResposta());
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	public Resposta filtroExecutar(@RequestBody PerfilCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.perfilFiltroExecutar(usuario, filtro).getResposta());
	}

	@RequestMapping("/filtro-novo")
	public Resposta filtroNovo(Principal usuario) throws Exception {
		return new Resposta(facadeBo.perfilFiltroNovo(usuario).values());
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	public Resposta incluir(@RequestBody Perfil perfil, Principal usuario) throws Exception {
		return salvar(perfil, usuario);
	}

	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	public Resposta novo(@RequestParam(value = "id", required = false) Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.perfilNovo(usuario, id == null ? null : new Perfil(id)).getResposta());
	}

	public Resposta salvar(@RequestBody Perfil perfil, Principal usuario) throws Exception {
		return new Resposta(facadeBo.perfilSalvar(usuario, perfil).getResposta());
	}

	@RequestMapping(value = "/salvar-comando", method = RequestMethod.POST)
	public Resposta salvarComando(@RequestBody Comando comando, Principal usuario) throws Exception {
		return new Resposta(facadeBo.perfilSalvarComando(usuario, comando).getResposta());
	}

	@RequestMapping(value = "/salvar-modulo", method = RequestMethod.POST)
	public Resposta salvarModulo(@RequestBody Modulo modulo, Principal usuario) throws Exception {
		return new Resposta(facadeBo.perfilSalvarModulo(usuario, modulo).getResposta());
	}

	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	public Resposta visualizar(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.perfilVisualizar(usuario, id).getResposta());
	}

}