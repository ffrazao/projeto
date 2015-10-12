package br.gov.df.emater.aterwebsrv.modelo.sistema;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UsuarioStatusConta;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

/**
 * Classe persistente dos usuarios do sistema.
 * 
 */
@Entity
@Table(name = "usuario", schema = EntidadeBase.SISTEMA_SCHEMA)
@Indexed
public class Usuario extends EntidadeBase implements _ChavePrimaria<Integer>, UserDetails {

	private static final long serialVersionUID = 1L;

	@Transient
	private Collection<? extends GrantedAuthority> authorities;

	@Column(name = "acesso_expira_em")
	private Long expires;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Transient
	private String modulo;

	@Transient
	private String newPassword;

	@Column(name = "senha")
	private String password;

	@OneToOne
	@JoinColumn(name = "pessoa_id")
	@NotNull
	private Pessoa pessoa;

	@Column(name = "nome_usuario")
	@NotEmpty
	@Field(index = Index.YES, store = Store.YES)
	private String username;

	@Enumerated(EnumType.STRING)
	@Column(name = "situacao")
	@NotNull
	private UsuarioStatusConta usuarioStatusConta;

	public Usuario() {
	}

	public Usuario(String name) {
		setUsername(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getExpires() {
		return expires;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getModulo() {
		return modulo;
	}

	public String getNewPassword() {
		return newPassword;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public UsuarioStatusConta getUsuarioStatusConta() {
		return usuarioStatusConta;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return isEnabled() && isCredentialsNonExpired();
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return Arrays.asList(UsuarioStatusConta.A, UsuarioStatusConta.R).contains(getUsuarioStatusConta());
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return getExpires() == null || getExpires() == 0 || Calendar.getInstance().getTimeInMillis() > getExpires();
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return UsuarioStatusConta.A.equals(getUsuarioStatusConta());
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public void setExpires(Long expires) {
		this.expires = expires;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	// @OneToMany(mappedBy = "usuario", targetEntity = UsuarioModulo.class,
	// fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	// private List<UsuarioModulo> usuarioModuloList;

	// @OneToMany(mappedBy = "usuario", targetEntity = UsuarioPerfil.class,
	// fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	// private List<UsuarioPerfil> usuarioPerfilList;

	public void setUsername(String username) {
		this.username = username;
	}

	public void setUsuarioStatusConta(UsuarioStatusConta usuarioStatusConta) {
		this.usuarioStatusConta = usuarioStatusConta;
	}

	@Override
	public String toString() {
		return (pessoa == null ? this.username : (pessoa.getApelidoSigla() == null ? pessoa.getNome() : pessoa.getApelidoSigla()));
	}
}