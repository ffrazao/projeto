package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.modelo.dto.UsuarioCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@RestController
@RequestMapping("/usuario")
public class UsuarioRest {

	@Autowired
	private FacadeBo facadeBo;

	public UsuarioRest() {
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	public Resposta editar(@RequestBody Usuario usr, Principal usuario) throws Exception {
		return salvar(usr, usuario);
	}

	@RequestMapping(value = "/excluir", method = RequestMethod.DELETE)
	public Resposta excluir(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.usuarioExcluir(usuario, id).getResposta());
	}


	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	public Resposta filtroExecutar(@RequestBody UsuarioCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.usuarioFiltroExecutar(usuario, filtro).getResposta());
	}

	@RequestMapping("/filtro-novo")
	public Resposta filtroNovo(Principal usuario) throws Exception {
		return new Resposta(facadeBo.usuarioFiltroNovo(usuario).values());
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	public Resposta incluir(@RequestBody Usuario usr, Principal usuario) throws Exception {
		return salvar(usr, usuario);
	}

	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public Resposta novo(@RequestBody Usuario usr, Principal usuario) throws Exception {
		return new Resposta(facadeBo.usuarioNovo(usuario, usr).getResposta());
	}

	public Resposta salvar(@RequestBody Usuario usr, Principal usuario) throws Exception {
		return new Resposta(facadeBo.usuarioSalvar(usuario, usr).getResposta());
	}

	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	public Resposta visualizar(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.usuarioVisualizar(usuario, id).getResposta());
	}

}