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
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.modelo.dto.PessoaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@RestController
@RequestMapping("/pessoa")
public class PessoaRest {

	public PessoaRest() {
	}

	@Autowired
	private FacadeBo facadeBo;

	@RequestMapping("/filtro-novo")
	@Transactional(readOnly = true)
	public Resposta filtroNovo(Principal usuario) {
		try {
			return new Resposta(facadeBo.pessoaFiltroNovo(usuario).values());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	@Transactional(readOnly = true)
	public Resposta filtroExecutar(@RequestBody PessoaCadFiltroDto filtro, Principal usuario) {
		try {
			return new Resposta(facadeBo.pessoaFiltroExecutar(usuario, filtro).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	@Transactional(readOnly=true)
	public Resposta novo(@RequestParam("modelo") PessoaTipo pessoaTipo, Principal usuario) {
		try {
			return new Resposta(facadeBo.pessoaNovo(usuario, pessoaTipo).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	@Transactional
	public Resposta incluir(@RequestBody Pessoa pessoa, Principal usuario) {
		return salvar(pessoa, usuario);
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	@Transactional
	public Resposta editar(@RequestBody Pessoa pessoa, Principal usuario) {
		return salvar(pessoa, usuario);
	}

	@Transactional
	public Resposta salvar(@RequestBody Pessoa pessoa, Principal usuario) {
		try {
			return new Resposta(facadeBo.pessoaSalvar(usuario, pessoa).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

}