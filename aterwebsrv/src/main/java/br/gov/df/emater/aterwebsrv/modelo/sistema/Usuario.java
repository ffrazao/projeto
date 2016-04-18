package br.gov.df.emater.aterwebsrv.modelo.sistema;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UsuarioStatusConta;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEmail;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerTimestamp;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerTimestamp;

/**
 * Classe persistente dos usuarios do sistema.
 * 
 */
@Entity
@Table(name = "usuario", schema = EntidadeBase.SISTEMA_SCHEMA)
// @Indexed
public class Usuario extends EntidadeBase implements _ChavePrimaria<Integer>, UserDetails, InfoBasica<Usuario> {

	private static final long serialVersionUID = 1L;

	@Column(name = "acesso_expira_em")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerTimestamp.class)
	@JsonDeserialize(using = JsonDeserializerTimestamp.class)
	private Calendar acessoExpiraEm;

	@OneToMany(mappedBy = "usuario")
	private Set<UsuarioPerfil> authorities;

	@Transient
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerTimestamp.class)
	@JsonDeserialize(using = JsonDeserializerTimestamp.class)
	private Calendar expires;

	@Transient
	private Map<String, Set<String>> funcionalidadeComandoList;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "info_sobre_usuario")
	private String infoSobreUsuario;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pessoa_email_id")
	private PessoaEmail pessoaEmail;

	@Column(name = "tentativa_acesso_invalido")
	private Integer tentativaAcessoInvalido;

	@ManyToOne
	@JoinColumn(name = "unidade_organizacional_id")
	private UnidadeOrganizacional unidadeOrganizacional;

	@Column(name = "nome_usuario")
	@NotEmpty
	// @Field(index = Index.YES, store = Store.YES)
	private String username;

	@Column(name = "usuario_atualizou_perfil")
	@Enumerated(EnumType.STRING)
	protected Confirmacao usuarioAtualizouPerfil;

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

	public Calendar getAcessoExpiraEm() {
		return acessoExpiraEm;
	}

	@Override
	public Set<UsuarioPerfil> getAuthorities() {
		return authorities;
	}

	public Calendar getExpires() {
		return expires;
	}

	public Map<String, Set<String>> getFuncionalidadeComandoList() {
		return funcionalidadeComandoList;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getInfoSobreUsuario() {
		return infoSobreUsuario;
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

	public PessoaEmail getPessoaEmail() {
		return pessoaEmail;
	}

	public Integer getTentativaAcessoInvalido() {
		return tentativaAcessoInvalido;
	}

	public UnidadeOrganizacional getUnidadeOrganizacional() {
		return unidadeOrganizacional;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public Confirmacao getUsuarioAtualizouPerfil() {
		return usuarioAtualizouPerfil;
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
		return (getAcessoExpiraEm() == null || getAcessoExpiraEm().after(Calendar.getInstance()));
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return Arrays.asList(UsuarioStatusConta.A, UsuarioStatusConta.R).contains(getUsuarioStatusConta()) || PessoaSituacao.A.equals(getPessoa().getSituacao());
	}

	public void setAcessoExpiraEm(Calendar acessoExpiraEm) {
		this.acessoExpiraEm = acessoExpiraEm;
	}

	public void setAuthorities(Set<UsuarioPerfil> authorities) {
		this.authorities = authorities;
	}

	public void setExpires(Calendar expires) {
		this.expires = expires;
	}

	public void setFuncionalidadeComandoList(Map<String, Set<String>> funcionalidadeComandoList) {
		this.funcionalidadeComandoList = funcionalidadeComandoList;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setInfoSobreUsuario(String infoSobreUsuario) {
		this.infoSobreUsuario = infoSobreUsuario;
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

	public void setPessoaEmail(PessoaEmail pessoaEmail) {
		this.pessoaEmail = pessoaEmail;
	}

	public void setTentativaAcessoInvalido(Integer tentativaAcessoInvalido) {
		this.tentativaAcessoInvalido = tentativaAcessoInvalido;
	}

	public void setUnidadeOrganizacional(UnidadeOrganizacional unidadeOrganizacional) {
		this.unidadeOrganizacional = unidadeOrganizacional;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setUsuarioAtualizouPerfil(Confirmacao usuarioAtualizouPerfil) {
		this.usuarioAtualizouPerfil = usuarioAtualizouPerfil;
	}

	public void setUsuarioStatusConta(UsuarioStatusConta usuarioStatusConta) {
		this.usuarioStatusConta = usuarioStatusConta;
	}

	@Override
	public String toString() {
		return (pessoa == null ? getUsername() : (pessoa.getApelidoSigla() == null ? pessoa.getNome() : pessoa.getApelidoSigla()));
	}
}