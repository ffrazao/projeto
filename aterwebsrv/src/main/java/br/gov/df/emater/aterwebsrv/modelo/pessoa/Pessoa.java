package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * The persistent class for the pessoa database table.
 * 
 */
@Entity
@Table(name = "pessoa", schema = EntidadeBase.PESSOA_SCHEMA)
@Inheritance(strategy = InheritanceType.JOINED)
// para identificar classes dentro de contextos polim�rficos
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public abstract class Pessoa extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "apelido_sigla")
	@Field(index = Index.YES, store = Store.YES)
	private String apelidoSigla;

	@OneToMany(mappedBy = "pessoa")
	@IndexedEmbedded
	private List<PessoaArquivo> arquivoList;

	@Transient
	private String fotoPerfil;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotBlank
	@Field(index = Index.YES, store = Store.YES)
	private String nome;

	@Lob
	@Field(index = Index.YES, store = Store.YES)
	private String observacoes;

	@OneToMany(mappedBy = "pessoa")
	@IndexedEmbedded
	private List<PessoaMeioContato> pessoaMeioContatos;

	@OneToMany(mappedBy = "pessoa")
	@IndexedEmbedded
	private List<PessoaRelacionamento> pessoaRelacionamentos;

	@Column(name = "pessoa_tipo")
	@Enumerated(EnumType.STRING)
	private PessoaTipo pessoaTipo;

	@OneToOne(mappedBy = "pessoa", fetch = FetchType.LAZY)
	private PublicoAlvo publicoAlvo;

	@Column(name = "publico_alvo")
	@Enumerated(EnumType.STRING)
	@NotNull
	private Confirmacao publicoAlvoConfirmacao;

	@Enumerated(EnumType.STRING)
	@NotNull
	private PessoaSituacao situacao;

	@Column(name = "situacao_data")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar situacaoData;

	@OneToOne(mappedBy = "pessoa", fetch = FetchType.LAZY)
	private Usuario usuario;

	public Pessoa() {
	}

	public Pessoa(Integer id) {
		super(id);
	}
	
	public Pessoa(Integer id, String nome, String apelidoSigla) {
		this(id);
		setNome(nome);
		setApelidoSigla(apelidoSigla);
	}

	public String getApelidoSigla() {
		return apelidoSigla;
	}

	public List<PessoaArquivo> getArquivoList() {
		return arquivoList;
	}

	public String getFotoPerfil() {
		return this.fotoPerfil;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public List<PessoaMeioContato> getPessoaMeioContatos() {
		return pessoaMeioContatos;
	}

	public List<PessoaRelacionamento> getPessoaRelacionamentos() {
		return pessoaRelacionamentos;
	}

	public PessoaTipo getPessoaTipo() {
		return pessoaTipo;
	}

	public PublicoAlvo getPublicoAlvo() {
		return publicoAlvo;
	}

	public Confirmacao getPublicoAlvoConfirmacao() {
		return publicoAlvoConfirmacao;
	}

	public PessoaSituacao getSituacao() {
		return situacao;
	}

	public Calendar getSituacaoData() {
		return situacaoData;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setApelidoSigla(String apelidoSigla) {
		this.apelidoSigla = apelidoSigla;
	}

	public void setArquivoList(List<PessoaArquivo> arquivoList) {
		this.arquivoList = arquivoList;
	}

	public void setFotoPerfil(String fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public void setPessoaMeioContatos(List<PessoaMeioContato> pessoaMeioContatos) {
		this.pessoaMeioContatos = pessoaMeioContatos;
	}

	public void setPessoaRelacionamentos(List<PessoaRelacionamento> pessoaRelacionamentos) {
		this.pessoaRelacionamentos = pessoaRelacionamentos;
	}

	public void setPessoaTipo(PessoaTipo pessoaTipo) {
		this.pessoaTipo = pessoaTipo;
	}

	public void setPublicoAlvo(PublicoAlvo publicoAlvo) {
		this.publicoAlvo = publicoAlvo;
	}

	public void setPublicoAlvoConfirmacao(Confirmacao publicoAlvoConfirmacao) {
		this.publicoAlvoConfirmacao = publicoAlvoConfirmacao;
	}

	public void setSituacao(PessoaSituacao situacao) {
		this.situacao = situacao;
	}

	public void setSituacaoData(Calendar situacaoData) {
		this.situacaoData = situacaoData;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}