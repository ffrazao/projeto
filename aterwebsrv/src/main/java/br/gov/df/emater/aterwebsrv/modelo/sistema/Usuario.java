package br.gov.df.emater.aterwebsrv.modelo.sistema;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UsuarioStatusConta;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

/**
 * Classe persistente dos usuarios do sistema.
 * 
 */
@Entity
@Table(name = "usuario", schema = EntidadeBase.SISTEMA_SCHEMA)
// @Indexed
public class Usuario extends EntidadeBase implements _ChavePrimaria<Integer>, UserDetails, InfoBasica<Usuario> {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "usuario")
	private Set<UsuarioPerfil> authorities;

	@Column(name = "acesso_expira_em")
	private Long expires;

	@Transient
	private Map<String, Set<String>> funcionalidadeComandoList;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Transient
	private UnidadeOrganizacional lotacaoAtual;

	@Transient
	private String modulo;

	@Transient
	private String newPassword;

	@Column(name = "senha")
	private String password;

	@ManyToOne
	@JoinColumn(name = "pessoa_id")
	private Pessoa pessoa;

	@ManyToOne
	@JoinColumn(name = "unidade_organizacional_id")
	private UnidadeOrganizacional unidadeOrganizacional;

	@Column(name = "nome_usuario")
	@NotEmpty
	// @Field(index = Index.YES, store = Store.YES)
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

	public Usuario(String name, Pessoa pessoa) {
		this(name);
		setPessoa(pessoa);
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
	public Set<UsuarioPerfil> getAuthorities() {
		return authorities;
	}

	public Long getExpires() {
		return expires;
	}

	public Map<String, Set<String>> getFuncionalidadeComandoList() {
		return funcionalidadeComandoList;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public UnidadeOrganizacional getLotacaoAtual() {
		return lotacaoAtual;
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

	public UnidadeOrganizacional getUnidadeOrganizacional() {
		return unidadeOrganizacional;
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

	public Usuario infoBasica() {
		return new Usuario(this.username, this.pessoa.infoBasica());
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

	public void setAuthorities(Set<UsuarioPerfil> authorities) {
		this.authorities = authorities;
	}

	public void setExpires(Long expires) {
		this.expires = expires;
	}

	public void setFuncionalidadeComandoList(Map<String, Set<String>> funcionalidadeComandoList) {
		this.funcionalidadeComandoList = funcionalidadeComandoList;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setLotacaoAtual(UnidadeOrganizacional lotacaoAtual) {
		this.lotacaoAtual = lotacaoAtual;
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

	public void setUnidadeOrganizacional(UnidadeOrganizacional unidadeOrganizacional) {
		this.unidadeOrganizacional = unidadeOrganizacional;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setUsuarioStatusConta(UsuarioStatusConta usuarioStatusConta) {
		this.usuarioStatusConta = usuarioStatusConta;
	}

	@Override
	public String toString() {
		return (pessoa == null ? getUsername() : (pessoa.getApelidoSigla() == null ? pessoa.getNome() : pessoa.getApelidoSigla()));
	}
}