package br.gov.df.emater.aterwebsrv.rest;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.seguranca.UserAuthentication;

@RestController
@RequestMapping("/api")
public class SegurancaRest {

	@Autowired
	private FacadeBo facadeBo;

	public SegurancaRest() {
	}

	@RequestMapping(value = "/acesso", method = RequestMethod.GET)
	public void acesso(@RequestParam String funcionalidade, @RequestParam(required = false) String comando, Principal principal) {
		Usuario usuario = (Usuario) ((UserAuthentication) principal).getDetails();
		Set<String> comandoList = usuario.getFuncionalidadeComandoList().get(funcionalidade);
		if (comandoList == null || (comando != null && !comandoList.contains(comando))) {
			throw new BadCredentialsException("Recurso não disponível");
		}
	}

	@RequestMapping(value = "/esqueci-senha", method = RequestMethod.GET)
	public Resposta esqueciSenha(@RequestParam String email) throws Exception {
		return new Resposta(facadeBo.segurancaEsqueciSenha(email).getResposta());
	}

	@RequestMapping("/login")
	public void login() {
	}

	@RequestMapping(value = "/renovar-senha", method = RequestMethod.POST)
	public Resposta renovarSenha(@RequestBody Usuario usuario) throws Exception {
		return new Resposta(facadeBo.segurancaRenovarSenha(usuario).getResposta());
	}

	@RequestMapping(value = "/salvar-perfil", method = RequestMethod.POST)
	public Resposta salvarPerfil(@RequestBody Usuario usuario, Principal principal) throws Exception {
		return new Resposta(facadeBo.segurancaSalvarPerfil(principal, usuario).getResposta());
	}

	@RequestMapping(value = "/visualizar-perfil", method = RequestMethod.GET)
	public Resposta visualizarPerfil(@RequestParam String username, Principal principal) throws Exception {
		return new Resposta(facadeBo.segurancaVisualizarPerfil(principal, username).getResposta());
	}

}