package br.gov.df.emater.aterwebsrv.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

/**
 * Classe customizada para fazer a autenticação de usuários
 * 
 * @author frazao
 * 
 */
@Component("authenticationProvider")
public class AutenticacaoProvider extends DaoAuthenticationProvider {

	public AutenticacaoProvider() {
		ReflectionSaltSource ss = new ReflectionSaltSource();
		ss.setUserPropertyToUse("id");
		this.setSaltSource(ss);
		this.setPasswordEncoder(new Md5PasswordEncoder());
	}
	
	@Autowired
	@Qualifier("userDetailsService")
	@Override
	public void setUserDetailsService(org.springframework.security.core.userdetails.UserDetailsService userDetailsService) {
		super.setUserDetailsService(userDetailsService);
	}

	@Override
	public Authentication authenticate(Authentication autenticacao) throws AuthenticationException {
		Authentication result = super.authenticate(autenticacao);
		if (result.isAuthenticated()) {
			// testar modulo
		}
		Usuario u = ((Usuario)result.getPrincipal());
		u.setId(null);
		u.setPassword(null);
		u.setNewPassword(null);
		return result;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
	}
}