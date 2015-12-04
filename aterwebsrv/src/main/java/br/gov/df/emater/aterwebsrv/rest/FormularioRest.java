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
import br.gov.df.emater.aterwebsrv.modelo.dto.FormularioCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dto.FormularioColetaCadFiltroDto;
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
	@Transactional
	public Resposta coletar(@RequestBody FormularioVersao formularioVersao, Principal usuario) {
		try {
			return new Resposta(facadeBo.formularioColetar(usuario, formularioVersao));
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	@Transactional
	public Resposta editar(@RequestBody Formulario formulario, Principal usuario) {
		return salvar(formulario, usuario);
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	@Transactional(readOnly = true)
	public Resposta filtroExecutar(@RequestBody FormularioCadFiltroDto filtro, Principal usuario) {
		try {
			return new Resposta(facadeBo.formularioFiltroExecutar(usuario, filtro).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping(value = "/filtro-coleta-executar", method = RequestMethod.POST)
	@Transactional(readOnly = true)
	public Resposta filtroComColetaExecutar(@RequestBody FormularioColetaCadFiltroDto filtro, Principal usuario) {
		try {
			return new Resposta(facadeBo.formularioColetaFiltroExecutar(usuario, filtro).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping("/filtro-novo")
	@Transactional(readOnly = true)
	public Resposta filtroNovo(Principal usuario) {
		try {
			return new Resposta(facadeBo.formularioFiltroNovo(usuario).values());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	@Transactional
	public Resposta incluir(@RequestBody Formulario formulario, Principal usuario) {
		return salvar(formulario, usuario);
	}

	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Resposta novo(Principal usuario) {
		try {
			return new Resposta(facadeBo.formularioNovo(usuario).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@Transactional
	public Resposta salvar(@RequestBody Formulario formulario, Principal usuario) {
		try {
			return new Resposta(facadeBo.formularioSalvar(usuario, formulario).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Resposta visualizar(@RequestParam Integer id, Principal usuario) {
		try {
			return new Resposta(facadeBo.formularioVisualizar(usuario, id).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping(value = "/visualizar-codigo", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Resposta visualizarPorCodigo(@RequestParam String codigo, Principal usuario) {
		try {
			return new Resposta(facadeBo.formularioVisualizarPorCodigo(usuario, codigo).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

}