package br.gov.df.emater.aterwebsrv.seguranca;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import br.gov.df.emater.aterwebsrv.dao.sistema.ModuloDao;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UsuarioStatusConta;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Modulo;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

/**
 * Classe customizada para fazer a autenticação de usuários
 * 
 * @author frazao
 * 
 */
@Component("authenticationProvider")
public class AutenticacaoProvider extends DaoAuthenticationProvider {

	private static final int MAX_TENTATIVA_ACESSO_INVALIDO = 3;

	@Autowired
	private UsuarioDao dao;

	@Autowired
	private ModuloDao moduloDao;

	public AutenticacaoProvider() {
		ReflectionSaltSource ss = new ReflectionSaltSource();
		ss.setUserPropertyToUse("id");
		this.setSaltSource(ss);
		this.setPasswordEncoder(new Md5PasswordEncoder());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Authentication authenticate(Authentication autenticacao) throws AuthenticationException {
		Authentication result;
		try {
			result = super.authenticate(autenticacao);
		} catch (BadCredentialsException e) {
			Usuario u = dao.findByUsername((String) autenticacao.getPrincipal());
			if (u != null) {
				Integer t = u.getTentativaAcessoInvalido() == null ? 0 : u.getTentativaAcessoInvalido();
				t++;
				e = new BadCredentialsException(String.format("Tentativa (%d de %d) de acesso inválido", t, MAX_TENTATIVA_ACESSO_INVALIDO), e);
				u.setTentativaAcessoInvalido(t);
//				if (t >= MAX_TENTATIVA_ACESSO_INVALIDO) {
//					u.setUsuarioStatusConta(UsuarioStatusConta.B);
//				}
				dao.saveAndFlush(u);
				if (UsuarioStatusConta.B.equals(u.getUsuarioStatusConta())) {
					e = new BadCredentialsException("Esta conta foi bloqueada por diversas tentativas de acesso inválido", e);
				}
			}
			if ("bad credentials".equalsIgnoreCase(e.getMessage())) {
				e = new BadCredentialsException("Credenciais inválidas!", e);
			}
			throw e;
		}
		// Filtrar modulos ativos
		Map<String, Object> details = (Map<String, Object>) autenticacao.getDetails();
		
		details.put("MODULO", identificaModulo(details.get("MODULO")));
		
		if (result.isAuthenticated()) {
			Usuario u = dao.findOne(((Usuario) result.getPrincipal()).getId());
			u.setTentativaAcessoInvalido(null);
			dao.saveAndFlush(u);

			// limpar informações
			u = ((Usuario) result.getPrincipal());
			u.setId(null);
			u.setPassword(null);
			u.setNewPassword(null);
			u.setDetails((Map<String, Object>) result.getDetails());
		}
		return result;
	}

	private Integer identificaModulo(Object moduloId) {
		if (moduloId == null) {
			List<Modulo> moduloList = moduloDao.findByPrincipal(Confirmacao.S);
			if (moduloList.size() == 1) {
				moduloId = moduloList.get(0).getId().toString();
			}
		}
		Modulo modulo = null;
		if (moduloId != null) {
			if (moduloId instanceof String) {				
				modulo = moduloDao.findOne(Integer.parseInt((String) moduloId));
			} else {
				modulo = moduloDao.findOne((Integer) moduloId);
			}
		} else {
			modulo = moduloDao.findOneByPrincipal(Confirmacao.S);
		}
		if (modulo == null) {
			throw new BadCredentialsException("Módulo de acesso não identificado!");
		}
		if (Confirmacao.N.equals(modulo.getAtivo())) {
			throw new BadCredentialsException("Tentativa de acesso a um módulo inativo!");
		}
		return modulo.getId();
	}

	@Autowired
	@Qualifier("userDetailsService")
	@Override
	public void setUserDetailsService(org.springframework.security.core.userdetails.UserDetailsService userDetailsService) {
		super.setUserDetailsService(userDetailsService);
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
	}
}