package br.gov.df.emater.aterwebsrv.rest_planejamento;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoCreditoRuralCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoCreditoRuralReceitaDespesaApoioDto;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
import br.gov.df.emater.aterwebsrv.rest.Resposta;

@RestController
@RequestMapping("/projeto-credito-rural")
public class ProjetoCreditoRuralRest {

	@Autowired
	private FacadeBo facadeBo;

	public ProjetoCreditoRuralRest() {
	}

	@RequestMapping(value = "/calcular-cronograma", method = RequestMethod.POST)
	public Resposta calcularCronograma(@RequestBody ProjetoCreditoRural projetoCredito, Principal usuario) throws Exception {
		return new Resposta(facadeBo.projetoCreditoRuralCalcularCronograma(usuario, projetoCredito).getResposta());
	}

	@RequestMapping(value = "/calcular-fluxo-caixa", method = RequestMethod.POST)
	public Resposta calcularFluxoCaixa(@RequestBody ProjetoCreditoRural projetoCredito, Principal usuario) throws Exception {
		return new Resposta(facadeBo.projetoCreditoRuralCalcularFluxoCaixa(usuario, projetoCredito).getResposta());
	}

	@RequestMapping(value = "/excluir", method = RequestMethod.DELETE)
	public Resposta excluir(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.projetoCreditoRuralExcluir(usuario, id).getResposta());
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	public Resposta filtroExecutar(@RequestBody ProjetoCreditoRuralCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.projetoCreditoRuralFiltroExecutar(usuario, filtro).getResposta());
	}

	@RequestMapping("/filtro-novo")
	public Resposta filtroNovo(Principal usuario) throws Exception {
		return new Resposta(facadeBo.projetoCreditoRuralFiltroNovo(usuario).getResposta());
	}

	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	public Resposta novo(@RequestParam(value = "id", required = false) Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.projetoCreditoRuralNovo(usuario, id == null ? null : new ProjetoCreditoRural(id)).getResposta());
	}

	@RequestMapping(value = "/projeto-tecnico-rel", method = RequestMethod.GET)
	public Resposta projetoTecnicoRel(@RequestParam(value = "idList", required = true) Integer[] idList, Principal usuario) throws Exception {
		return new Resposta(facadeBo.projetoCreditoRuralProjetoTecnicoRel(usuario, idList).getResposta());
	}

	@RequestMapping(value = "/planilha-upload", method = RequestMethod.POST)
	public Resposta planilhaUpload(@RequestBody ProjetoCreditoRuralReceitaDespesaApoioDto registro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.projetoCreditoRuralPlanilhaUpload(usuario, registro).getResposta());
	}


}