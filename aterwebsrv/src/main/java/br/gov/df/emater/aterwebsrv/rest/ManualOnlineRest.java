package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.dto.sistema.ManualOnlineCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Comando;
import br.gov.df.emater.aterwebsrv.modelo.sistema.ManualOnline;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Modulo;

@RestController
@RequestMapping("/manual-online")
public class ManualOnlineRest {

	@Autowired
	private FacadeBo facadeBo;

	public ManualOnlineRest() {
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	public Resposta editar(@RequestBody ManualOnline manualOnline, Principal usuario) throws Exception {
		return salvar(manualOnline, usuario);
	}

	@RequestMapping(value = "/excluir", method = RequestMethod.DELETE)
	public Resposta excluir(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.manualOnlineExcluir(usuario, id).getResposta());
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	public Resposta filtroExecutar(@RequestBody ManualOnlineCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.manualOnlineFiltroExecutar(usuario, filtro).getResposta());
	}

	@RequestMapping("/filtro-novo")
	public Resposta filtroNovo(Principal usuario) throws Exception {
		return new Resposta(facadeBo.manualOnlineFiltroNovo(usuario).getResposta());
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	public Resposta incluir(@RequestBody ManualOnline manualOnline, Principal usuario) throws Exception {
		return salvar(manualOnline, usuario);
	}

	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	public Resposta novo(@RequestParam(value = "id", required = false) Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.manualOnlineNovo(usuario, id == null ? null : new ManualOnline(id)).getResposta());
	}

	public Resposta salvar(@RequestBody ManualOnline manualOnline, Principal usuario) throws Exception {
		return new Resposta(facadeBo.manualOnlineSalvar(usuario, manualOnline).getResposta());
	}

	@RequestMapping(value = "/salvar-comando", method = RequestMethod.POST)
	public Resposta salvarComando(@RequestBody Comando comando, Principal usuario) throws Exception {
		return new Resposta(facadeBo.manualOnlineSalvarComando(usuario, comando).getResposta());
	}

	@RequestMapping(value = "/salvar-modulo", method = RequestMethod.POST)
	public Resposta salvarModulo(@RequestBody Modulo modulo, Principal usuario) throws Exception {
		return new Resposta(facadeBo.manualOnlineSalvarModulo(usuario, modulo).getResposta());
	}

	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	public Resposta visualizar(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.manualOnlineVisualizar(usuario, id).getResposta());
	}

	@RequestMapping(value = "/ajuda", method = RequestMethod.GET)
	public Resposta ajuda(@RequestParam String codigo, Principal usuario) throws Exception {
		return new Resposta(facadeBo.manualOnlineAjuda(usuario, codigo).getResposta());
	}

}