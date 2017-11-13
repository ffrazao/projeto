package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.IndiceProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.ProducaoGravaDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;

@RestController
@RequestMapping("/indice-producao")
public class IndiceProducaoRest {

	@Autowired
	private FacadeBo facadeBo;

	public IndiceProducaoRest() {
	}

	
	@RequestMapping(value = "/producao", method = RequestMethod.POST)
	public Resposta producao(@RequestBody ProducaoProprietario producaoProprietario, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoProducao(usuario, producaoProprietario).getResposta());
	}

	@RequestMapping(value = "/bem-classificacao-matriz", method = RequestMethod.GET)
	public Resposta bemClassificacaoMatriz(Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoBemClassificacaoMatriz(usuario).getResposta());
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

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	public Resposta editar(@RequestBody ProducaoGravaDto producaoProprietario, Principal usuario) throws Exception {
		return salvar(producaoProprietario, usuario);
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	public Resposta incluir(@RequestBody ProducaoGravaDto producaoProprietario, Principal usuario) throws Exception {
		return salvar(producaoProprietario, usuario);
	}


	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public Resposta novo(@RequestBody ProducaoProprietario producaoProprietario, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoNovo(usuario, producaoProprietario).getResposta());
	}

	public Resposta salvar(@RequestBody ProducaoGravaDto producaoProprietario, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoSalvar(usuario, producaoProprietario).getResposta());
	}
	
	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	public Resposta visualizar(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.indiceProducaoVisualizar(usuario, id).getResposta());
	}

}