package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.dto.LogAcaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.sistema.LogAcao;

@RestController
@RequestMapping("/log-acao")
public class LogAcaoRest {

	@Autowired
	private FacadeBo facadeBo;

	public LogAcaoRest() {
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	public Resposta editar(@RequestBody LogAcao logAcao, Principal usuario) throws Exception {
		return salvar(logAcao, usuario);
	}

	@RequestMapping(value = "/excluir", method = RequestMethod.POST)
	public Resposta excluir(@RequestBody LogAcao logAcao, Principal usuario) throws Exception {
		throw new BoException("Operação não implementada!");
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	public Resposta filtroExecutar(@RequestBody LogAcaoCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.logAcaoFiltroExecutar(usuario, filtro).getResposta());
	}

	@RequestMapping("/filtro-novo")
	public Resposta filtroNovo(Principal usuario) throws Exception {
		return new Resposta(facadeBo.logAcaoFiltroNovo(usuario).getResposta());
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	public Resposta incluir(@RequestBody LogAcao logAcao, Principal usuario) throws Exception {
		return salvar(logAcao, usuario);
	}

	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	public Resposta novo(@RequestParam(value = "id", required = false) Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.logAcaoNovo(usuario, id == null ? null : new LogAcao(id)).getResposta());
	}

	public Resposta salvar(@RequestBody LogAcao logAcao, Principal usuario) throws Exception {
		return new Resposta(facadeBo.logAcaoSalvar(usuario, logAcao).getResposta());
	}

	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	public Resposta visualizar(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.logAcaoVisualizar(usuario, id).getResposta());
	}

}