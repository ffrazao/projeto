package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.modelo.dto.PessoaCadFiltroDto;

@RestController
@RequestMapping("/pessoa")
public class PessoaRest {
	
	public PessoaRest() {
		System.out.println("novo PessoaRest");
	}

	@Autowired
	private FacadeBo facadeBo;
	
	@RequestMapping("/filtro-novo")
	public Resposta filtroNovo(Principal usuario) {
		try {
			return new Resposta(facadeBo.pessoaFiltroNovo(usuario));
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

	@RequestMapping("/filtro-executar")
	public Resposta filtroExecutar(PessoaCadFiltroDto filtro, Principal usuario) {
		try {
			return new Resposta(facadeBo.pessoaFiltroExecutar(usuario, filtro));
		} catch (Exception e) {
			return new Resposta(e);
		}
	}

}