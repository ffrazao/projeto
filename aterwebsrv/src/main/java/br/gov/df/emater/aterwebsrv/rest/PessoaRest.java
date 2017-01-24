package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.dto.pessoa.CarteiraProdutorRelFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.pessoa.DeclaracaoProdutorRelFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.pessoa.PessoaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@RestController
@RequestMapping("/pessoa")
public class PessoaRest {

	@Autowired
	private FacadeBo facadeBo;

	public PessoaRest() {
	}

	@RequestMapping(value = "/buscar-cep", method = RequestMethod.GET)
	public Resposta buscaCep(@RequestParam("cep") String cep, Principal usuario) throws Exception {
		return new Resposta(facadeBo.pessoaBuscarCep(usuario, cep).getResposta());
	}
	
	@RequestMapping(value = "/publico-alvo-por-pessoa-id", method = RequestMethod.GET)
	public Resposta publicoAlvoPorPessoaId(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.pessoaPublicoAlvoPorPessoaId(usuario, id).getResposta());
	}
	
	@RequestMapping(value = "/carteira-produtor-rel", method = RequestMethod.POST)
	public Resposta carteiraProdutorRel(@RequestBody CarteiraProdutorRelFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.pessoaCarteiraProdutorRel(usuario, filtro).getResposta());
	}

	@RequestMapping(value = "/carteira-produtor-verificar", method = RequestMethod.POST)
	public Resposta carteiraProdutorVerificar(@RequestBody CarteiraProdutorRelFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.pessoaCarteiraProdutorVerificar(usuario, filtro).getResposta());
	}
	
	@RequestMapping(value = "/declaracao-produtor-rel", method = RequestMethod.POST)
	public Resposta declaracaoProdutorRel(@RequestBody DeclaracaoProdutorRelFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.pessoaDeclaracaoProdutorRel(usuario, filtro).getResposta());
	}

	@RequestMapping(value = "/declaracao-produtor-verificar", method = RequestMethod.POST)
	public Resposta declaracaoProdutorVerificar(@RequestBody DeclaracaoProdutorRelFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.pessoaDeclaracaoProdutorVerificar(usuario, filtro).getResposta());
	}

	@RequestMapping(value = "/editar", method = RequestMethod.POST)
	public Resposta editar(@RequestBody Pessoa pessoa, Principal usuario) throws Exception {
		return salvar(pessoa, usuario);
	}

	@RequestMapping(value = "/excluir", method = RequestMethod.DELETE)
	public Resposta excluir(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.pessoaExcluir(usuario, id).getResposta());
	}

	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	public Resposta filtroExecutar(@RequestBody PessoaCadFiltroDto filtro, Principal usuario) throws Exception {
		return new Resposta(facadeBo.pessoaFiltroExecutar(usuario, filtro).getResposta());
	}

	@RequestMapping("/filtro-novo")
	public Resposta filtroNovo(Principal usuario) throws Exception {
		return new Resposta(facadeBo.pessoaFiltroNovo(usuario).values());
	}

	@RequestMapping(value = "/incluir", method = RequestMethod.POST)
	public Resposta incluir(@RequestBody Pessoa pessoa, Principal usuario) throws Exception {
		return salvar(pessoa, usuario);
	}

	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public Resposta novo(@RequestBody Pessoa pessoa, Principal usuario) throws Exception {
		return new Resposta(facadeBo.pessoaNovo(usuario, pessoa).getResposta());
	}

	public Resposta salvar(@RequestBody Pessoa pessoa, Principal usuario) throws Exception {
		return new Resposta(facadeBo.pessoaSalvar(usuario, pessoa).getResposta());
	}

	@RequestMapping(value = "/visualizar", method = RequestMethod.GET)
	public Resposta visualizar(@RequestParam Integer id, Principal usuario) throws Exception {
		return new Resposta(facadeBo.pessoaVisualizar(usuario, id).getResposta());
	}

}