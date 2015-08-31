package br.gov.df.emater.aterwebsrv.modelo.sistema;

import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UsuarioStatusConta;
import br.gov.df.emater.aterwebsrv.modelo.funcional.EmpregoVi;
import br.gov.df.emater.aterwebsrv.modelo.funcional.LotacaoVi;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerTimestamp;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerTimestamp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Classe persistente dos usuarios do sistema.
 * 
 */
@Entity
@Table(name = "usuario", schema = EntidadeBase.SISTEMA_SCHEMA)
@Indexed
public class Usuario extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "acesso_expira_em")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar acessoExpiraEm;

	@Transient
	private List<EmpregoVi> empregoList;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Transient
	private List<LotacaoVi> lotacaoList;

	@Column(name = "nome_usuario")
	@NotEmpty
	@Field(index = Index.YES, store = Store.YES)
	private String nomeUsuario;

	@OneToOne
	@JoinColumn(name = "pessoa_id")
	@NotNull
	private Pessoa pessoa;

	private String senha;

	@Transient
	private String sessaoId;

	@Transient
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss:SSS")
	@JsonSerialize(using = JsonSerializerTimestamp.class)
	@JsonDeserialize(using = JsonDeserializerTimestamp.class)
	private Calendar sessaoInicio;

	@Transient
	private String sessaoNumeroIp;

	@OneToMany(mappedBy = "usuario", targetEntity = UsuarioModulo.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UsuarioModulo> usuarioModuloList;

	@OneToMany(mappedBy = "usuario", targetEntity = UsuarioPerfil.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UsuarioPerfil> usuarioPerfilList;

	@Enumerated(EnumType.STRING)
	@Column(name = "status_conta")
	@NotNull
	private UsuarioStatusConta usuarioStatusConta;

	public Usuario() {
	}

	public Usuario(Integer id) {
		super(id);
	}

	public Usuario(Integer id, String nomeUsuario) {
		this(id);
		setNomeUsuario(nomeUsuario);
	}

	public Usuario(String nomeUsuario) {
		this();
		setNomeUsuario(nomeUsuario);
	}

	public Usuario(String nomeUsuario, String senha) {
		this(nomeUsuario);
		setSenha(senha);
	}

	public Usuario(Integer id, String nomeUsuario, Pessoa pessoa) {
		this(id, nomeUsuario);
		setPessoa(pessoa);
	}
	
	public Usuario simplificar() {
		try {
			Pessoa p = this.getPessoa().getClass().newInstance();
			p.setId(this.getPessoa().getId());
			p.setNome(this.getPessoa().getNome());
			p.setApelidoSigla(this.getPessoa().getApelidoSigla());
			p.setPessoaTipo(this.getPessoa().getPessoaTipo());
			return new Usuario(this.getId(), this.getNomeUsuario(), p);
		} catch (Exception e) {
		}
		return null;
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
		if (nomeUsuario == null) {
			if (other.nomeUsuario != null)
				return false;
		} else if (!nomeUsuario.equals(other.nomeUsuario))
			return false;
		return true;
	}

	public Calendar getAcessoExpiraEm() {
		return acessoExpiraEm;
	}

	public List<EmpregoVi> getEmpregoList() {
		return empregoList;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public List<LotacaoVi> getLotacaoList() {
		return lotacaoList;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public String getSenha() {
		return senha;
	}

	public String getSessaoId() {
		return sessaoId;
	}

	public Calendar getSessaoInicio() {
		return sessaoInicio;
	}

	public String getSessaoNumeroIp() {
		return sessaoNumeroIp;
	}

	public List<UsuarioModulo> getUsuarioModuloList() {
		return usuarioModuloList;
	}

	public List<UsuarioPerfil> getUsuarioPerfilList() {
		return usuarioPerfilList;
	}

	public UsuarioStatusConta getUsuarioStatusConta() {
		return usuarioStatusConta;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((nomeUsuario == null) ? 0 : nomeUsuario.hashCode());
		return result;
	}

	public void setAcessoExpiraEm(Calendar acessoExpiraEm) {
		this.acessoExpiraEm = acessoExpiraEm;
	}

	public void setEmpregoList(List<EmpregoVi> empregoList) {
		this.empregoList = empregoList;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setLotacaoList(List<LotacaoVi> lotacaoList) {
		this.lotacaoList = lotacaoList;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setSessaoId(String sessaoId) {
		this.sessaoId = sessaoId;
	}

	public void setSessaoInicio(Calendar sessaoInicio) {
		this.sessaoInicio = sessaoInicio;
	}

	public void setSessaoNumeroIp(String sessaoNumeroIp) {
		this.sessaoNumeroIp = sessaoNumeroIp;
	}

	public void setUsuarioModuloList(List<UsuarioModulo> usuarioModuloList) {
		this.usuarioModuloList = usuarioModuloList;
	}

	public void setUsuarioPerfilList(List<UsuarioPerfil> usuarioPerfilList) {
		this.usuarioPerfilList = usuarioPerfilList;
	}

	public void setUsuarioStatusConta(UsuarioStatusConta usuarioStatusConta) {
		this.usuarioStatusConta = usuarioStatusConta;
	}

	@Override
	public String toString() {
		return (pessoa == null ? this.nomeUsuario
				: (pessoa.getApelidoSigla() == null ? pessoa.getNome() : pessoa
						.getApelidoSigla()));
	}

}