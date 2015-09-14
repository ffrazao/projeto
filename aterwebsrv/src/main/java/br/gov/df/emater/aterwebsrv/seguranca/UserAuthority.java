package br.gov.df.emater.aterwebsrv.seguranca;

import org.springframework.security.core.GrantedAuthority;

import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

//@Entity
//@IdClass(UserAuthority.class)
public class UserAuthority implements GrantedAuthority {

	// @NotNull
	// @ManyToOne(fetch = FetchType.LAZY)
	// @JsonIgnore
	// @Id
	private Usuario user;

	// @NotNull
	// @Id
	private String authority;

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	@Override
	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof UserAuthority))
			return false;

		UserAuthority ua = (UserAuthority) obj;
		return ua.getAuthority() == this.getAuthority() || ua.getAuthority().equals(this.getAuthority());
	}

	@Override
	public int hashCode() {
		return getAuthority() == null ? 0 : getAuthority().hashCode();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + getAuthority();
	}
}
