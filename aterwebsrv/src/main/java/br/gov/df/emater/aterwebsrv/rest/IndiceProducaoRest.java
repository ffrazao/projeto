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
import br.gov.df.emater.aterwebsrv.modelo.dto.IndiceProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;

@RestController
@RequestMapping("/indice-producao")
public class IndiceProducaoRest {

	@Autowired
	private FacadeBo facadeBo;

	public IndiceProducaoRest() {
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	@Transactional
	public Resposta editar(@RequestBody Producao producao, Principal usuario) {
		return salvar(producao, usuario);
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	@Transactional(readOnly = true)
	public Resposta filtroExecutar(@RequestBody IndiceProducaoCadFiltroDto filtro, Principal usuario) {
		try {
			return new Resposta(facadeBo.indiceProducaoFiltroExecutar(usuario, filtro).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping("/filtro-novo")
	@Transactional(readOnly = true)
	public Resposta filtroNovo(Principal usuario) {
		try {
			return new Resposta(facadeBo.indiceProducaoFiltroNovo(usuario).values());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	@Transactional
	public Resposta incluir(@RequestBody Producao producao, Principal usuario) {
		return salvar(producao, usuario);
	}

	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Resposta novo(@RequestParam(value = "id", required = false) Integer id, Principal usuario) {
		try {
			return new Resposta(facadeBo.indiceProducaoNovo(usuario, id == null ? null : new Producao(id)).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@Transactional
	public Resposta salvar(@RequestBody Producao producao, Principal usuario) {
		try {
			return new Resposta(facadeBo.indiceProducaoSalvar(usuario, producao).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public Resposta visualizar(@RequestParam Integer id, Principal usuario) {
		try {
			return new Resposta(facadeBo.indiceProducaoVisualizar(usuario, id).getResposta());
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

}