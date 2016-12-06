package br.gov.df.emater.aterwebsrv.rest_planejamento;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.dto.ater.PropriedadeRuralCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.ater.PublicoAlvoPropriedadeRuralCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.rest.Resposta;

@RestController
@RequestMapping("/planejamento")
public class PlanejamentoRest {

	@Autowired
	private FacadeBo facadeBo;

	public PlanejamentoRest() {
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	public Resposta editar(@RequestBody PropriedadeRural propriedadeRural, Principal usuario) throws Exception {
		return salvar(propriedadeRural, usuario);
	}
	
	@RequestMapping(value = "/excluir", method = RequestMethod.DELETE)
	public Resposta excluir(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.propriedadeRuralExcluir(usuario, id).getResposta());
	}

	@RequestMapping(value = "/filtro-publico-alvo", method = RequestMethod.POST)
	public Resposta filtrarPorPublicoAlvoPropriedadeRuralComunidade(@RequestBody List<PublicoAlvo> publicoAlvoList, Principal usuario) throws Exception {
		return new Resposta(facadeBo.propriedadeRuralFiltrarPorPublicoAlvo(usuario, publicoAlvoList).getResposta());
	}

	@RequestMapping(value = "/filtro-publico-alvo-propriedade-rural-comunidade", method = RequestMethod.POST)
	public Resposta filtrarPorPublicoAlvoPropriedadeRuralComunidade(@RequestBody PublicoAlvoPropriedadeRuralCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.propriedadeRuralFiltrarPorPublicoAlvoPropriedadeRuralComunidade(usuario, filtro).getResposta());
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	public Resposta filtroExecutar(@RequestBody PropriedadeRuralCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.propriedadeRuralFiltroExecutar(usuario, filtro).getResposta());
	}

	@RequestMapping("/filtro-novo")
	public Resposta filtroNovo(Principal usuario) throws Exception {
		return new Resposta(facadeBo.propriedadeRuralFiltroNovo(usuario).values());
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	public Resposta incluir(@RequestBody PropriedadeRural propriedadeRural, Principal usuario) throws Exception {
		return salvar(propriedadeRural, usuario);
	}

	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	public Resposta novo(Principal usuario) throws Exception {
		return new Resposta(facadeBo.propriedadeRuralNovo(usuario).getResposta());
	}

	public Resposta salvar(@RequestBody PropriedadeRural propriedadeRural, Principal usuario) throws Exception {
		return new Resposta(facadeBo.propriedadeRuralSalvar(usuario, propriedadeRural).getResposta());
	}

	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	public Resposta visualizar(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.propriedadeRuralVisualizar(usuario, id).getResposta());
	}
}