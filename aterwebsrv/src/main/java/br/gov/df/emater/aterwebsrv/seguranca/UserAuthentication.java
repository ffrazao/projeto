package br.gov.df.emater.aterwebsrv.seguranca;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

public class UserAuthentication implements Authentication {

	private static final long serialVersionUID = 1L;

	private boolean authenticated = true;

	private final Usuario user;

	public UserAuthentication(Usuario user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getAuthorities();
	}

	@Override
	public Object getCredentials() {
		return user.getPassword();
	}

	@Override
	public Usuario getDetails() {
		return user;
	}

	@Override
	public String getName() {
		return user.getUsername();
	}

	@Override
	public Object getPrincipal() {
		return user.getUsername();
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}
}
