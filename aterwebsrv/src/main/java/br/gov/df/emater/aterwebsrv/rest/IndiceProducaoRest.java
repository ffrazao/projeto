package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.dto.IndiceProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;

@RestController
@RequestMapping("/indice-producao")
public class IndiceProducaoRest {

	@Autowired
	private FacadeBo facadeBo;

	public IndiceProducaoRest() {
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	public Resposta editar(@RequestBody Producao producao, Principal usuario) throws Exception {
		return salvar(producao, usuario);
	}

	@RequestMapping(value = "/excluir", method = RequestMethod.DELETE)
	public Resposta excluir(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoExcluir(usuario, id).getResposta());
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	public Resposta filtroExecutar(@RequestBody IndiceProducaoCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoFiltroExecutar(usuario, filtro).getResposta());
	}

	@RequestMapping("/filtro-novo")
	public Resposta filtroNovo(Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoFiltroNovo(usuario).values());
	}

	@RequestMapping(value = "/filtro-producao-propriedade-rural", method = RequestMethod.POST)
	public Resposta filtroProducaoPropriedadeRural(@RequestBody IndiceProducaoCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoFiltroProducaoPropriedadeRural(usuario, filtro).getResposta());
	}

	@RequestMapping(value = "/filtro-producao-publico-alvo", method = RequestMethod.POST)
	public Resposta filtroProducaoPublicoAlvo(@RequestBody IndiceProducaoCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoFiltroProducaoPublicoAlvo(usuario, filtro).getResposta());
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	public Resposta incluir(@RequestBody Producao producao, Principal usuario) throws Exception {
		return salvar(producao, usuario);
	}

	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public Resposta novo(@RequestBody Producao producao, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoNovo(usuario, producao).getResposta());
	}

	public Resposta salvar(@RequestBody Producao producao, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoSalvar(usuario, producao).getResposta());
	}

	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	public Resposta visualizar(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoVisualizar(usuario, id).getResposta());
	}

}