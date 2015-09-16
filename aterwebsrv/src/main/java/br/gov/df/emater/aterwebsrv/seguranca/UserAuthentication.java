package br.gov.df.emater.aterwebsrv.seguranca;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

public class UserAuthentication implements Authentication {

	private final Usuario user;
	private boolean authenticated = true;

	public UserAuthentication(Usuario user) {
		this.user = user;
	}

	@Override
	public String getName() {
		return user.getPessoa().getNome();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;//user.getAuthorities();
	}

	@Override
	public Object getCredentials() {
		return user.getSenha();
	}

	@Override
	public Usuario getDetails() {
		return user;
	}

	@Override
	public Object getPrincipal() {
		return user.getNomeUsuario();
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
