package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.dao.teste.TesteDao;
import br.gov.df.emater.aterwebsrv.modelo.dto.PessoaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.seguranca.UserDetailsService;

@RestController
@RequestMapping("/pessoa")
public class PessoaRest {

	public PessoaRest() {
	}

	@Autowired
	private FacadeBo facadeBo;

	@RequestMapping("/filtro-novo")
	public Resposta filtroNovo(Principal usuario) {
		try {
			return new Resposta(facadeBo.pessoaFiltroNovo(usuario).values());
		} catch (Exception e) {
			try {
				facadeBo.rollBack();
			} catch (Exception e2) {
				return new Resposta(e2);
			}
			return new Resposta(e);
		}
	}
	@Autowired
	private TesteDao testeDao;
	
	@RequestMapping(value = "/filtro-executar", method = RequestMethod.POST)
	public Resposta filtroExecutar(@RequestBody PessoaCadFiltroDto filtro, Principal usuario) {
		try {
			return new Resposta(testeDao.count());
//			return new Resposta(facadeBo.pessoaFiltroExecutar(usuario, filtro));
//			return new Resposta("Teste");
		} catch (Exception e) {
			try {
				facadeBo.rollBack();
			} catch (Exception e2) {
				return new Resposta(e2);
			}
			return new Resposta(e);
		}
	}

}