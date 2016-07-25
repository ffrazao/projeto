package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.modelo.dto.ProjetoCreditoRuralCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dto.ProjetoCreditoRuralCronogramaDto;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;

@RestController
@RequestMapping("/projeto-credito-rural")
public class ProjetoCreditoRuralRest {

	@Autowired
	private FacadeBo facadeBo;

	public ProjetoCreditoRuralRest() {
	}

	@RequestMapping(value = "/calcular-cronograma", method = RequestMethod.POST)
	public Resposta calcularCronograma(@RequestBody ProjetoCreditoRuralCronogramaDto projetoCredito, Principal usuario) throws Exception {
		return new Resposta(facadeBo.projetoCreditoRuralCalcularCronograma(usuario, projetoCredito).getResposta());
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	public Resposta editar(@RequestBody ProjetoCreditoRural projetoCredito, Principal usuario) throws Exception {
		return salvar(projetoCredito, usuario);
	}

	@RequestMapping(value = "/excluir", method = RequestMethod.POST)
	public Resposta excluir(@RequestBody ProjetoCreditoRural projetoCredito, Principal usuario) throws Exception {
		return new Resposta(facadeBo.projetoCreditoRuralExcluir(usuario, projetoCredito).getResposta());
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	public Resposta filtroExecutar(@RequestBody ProjetoCreditoRuralCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.projetoCreditoRuralFiltroExecutar(usuario, filtro).getResposta());
	}

	@RequestMapping("/filtro-novo")
	public Resposta filtroNovo(Principal usuario) throws Exception {
		return new Resposta(facadeBo.projetoCreditoRuralFiltroNovo(usuario).values());
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	public Resposta incluir(@RequestBody ProjetoCreditoRural projetoCredito, Principal usuario) throws Exception {
		return salvar(projetoCredito, usuario);
	}

	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	public Resposta novo(@RequestParam(value = "id", required = false) Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.projetoCreditoRuralNovo(usuario, id == null ? null : new ProjetoCreditoRural(id)).getResposta());
	}

	public Resposta salvar(@RequestBody ProjetoCreditoRural projetoCredito, Principal usuario) throws Exception {
		return new Resposta(facadeBo.projetoCreditoRuralSalvar(usuario, projetoCredito).getResposta());
	}

	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	public Resposta visualizar(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.projetoCreditoRuralVisualizar(usuario, id).getResposta());
	}

}