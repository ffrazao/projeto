package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.dto.FormularioCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.FormularioColetaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Formulario;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersao;

@RestController
@RequestMapping("/formulario")
public class FormularioRest {

	@Autowired
	private FacadeBo facadeBo;

	public FormularioRest() {
	}

	@RequestMapping(value = "/coletar", method = RequestMethod.POST)
	public Resposta coletar(@RequestBody FormularioVersao formularioVersao, Principal usuario) throws Exception {
		return new Resposta(facadeBo.formularioColetar(usuario, formularioVersao));
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	public Resposta editar(@RequestBody Formulario formulario, Principal usuario) throws Exception {
		return salvar(formulario, usuario);
	}

	@RequestMapping(value = "/excluir", method = RequestMethod.DELETE)
	public Resposta excluir(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.formularioExcluir(usuario, id).getResposta());
	}

	@RequestMapping(value = "/filtro-coleta-executar", method = RequestMethod.POST)
	public Resposta filtroComColetaExecutar(@RequestBody FormularioColetaCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.formularioColetaFiltroExecutar(usuario, filtro).getResposta());
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	public Resposta filtroExecutar(@RequestBody FormularioCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.formularioFiltroExecutar(usuario, filtro).getResposta());
	}

	@RequestMapping("/filtro-novo")
	public Resposta filtroNovo(Principal usuario) throws Exception {
		return new Resposta(facadeBo.formularioFiltroNovo(usuario).values());
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	public Resposta incluir(@RequestBody Formulario formulario, Principal usuario) throws Exception {
		return salvar(formulario, usuario);
	}

	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	public Resposta novo(Principal usuario) throws Exception {
		return new Resposta(facadeBo.formularioNovo(usuario).getResposta());
	}

	public Resposta salvar(@RequestBody Formulario formulario, Principal usuario) throws Exception {
		return new Resposta(facadeBo.formularioSalvar(usuario, formulario).getResposta());
	}

	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	public Resposta visualizar(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.formularioVisualizar(usuario, id).getResposta());
	}

	@RequestMapping(value = "/visualizar-codigo", method = RequestMethod.GET)
	public Resposta visualizarPorCodigo(@RequestParam String codigo, @RequestParam(required = false) String posicao, Principal usuario) throws Exception {
		return new Resposta(facadeBo.formularioVisualizarPorCodigo(usuario, codigo, posicao).getResposta());
	}

}