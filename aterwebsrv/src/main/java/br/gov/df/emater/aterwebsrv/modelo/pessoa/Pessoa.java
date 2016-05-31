package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.util.ArrayList;
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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo._LogInclusaoAlteracao;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.modelo.pendencia.Pendenciavel;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

/**
 * The persistent class for the pessoa database table.
 * 
 */
@Entity
@Table(name = "pessoa", schema = EntidadeBase.PESSOA_SCHEMA)
@Inheritance(strategy = InheritanceType.JOINED)
// para identificar classes dentro de contextos polimórficos
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public abstract class Pessoa extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<Pessoa>, Pendenciavel<PessoaPendencia>, _LogInclusaoAlteracao {

	private static final long serialVersionUID = 1L;

	@Column(name = "alteracao_data", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar alteracaoData;

	@ManyToOne
	@JoinColumn(name = "alteracao_usuario_id")
	private Usuario alteracaoUsuario;

	@Column(name = "apelido_sigla")
	// @Field(index = Index.YES, store = Store.YES)
	protected String apelidoSigla;

	@OneToMany(mappedBy = "pessoa")
	private List<PessoaArquivo> arquivoList;

	@Column(name = "chave_sisater")
	private String chaveSisater;

	@Transient
	private Object diagnosticoList;

	@OneToMany(mappedBy = "pessoa")
	private List<PessoaEmail> emailList;

	@OneToMany(mappedBy = "pessoa")
	private List<PessoaEndereco> enderecoList;

	@OneToMany(mappedBy = "pessoa")
	private List<PessoaGrupoSocial> grupoSocialList;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Integer id;

	@Column(name = "inclusao_data", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inclusaoData;

	@ManyToOne
	@JoinColumn(name = "inclusao_usuario_id")
	private Usuario inclusaoUsuario;

	@Column(name = "inscricao_estadual")
	private String inscricaoEstadual;

	@Column(name = "inscricao_estadual_uf")
	private String inscricaoEstadualUf;

	@NotBlank
	// @Field(index = Index.YES, store = Store.YES)
	protected String nome;

	@Lob
	// @Field(index = Index.YES, store = Store.YES)
	private String observacoes;

	@OneToMany(mappedBy = "pessoa")
	private List<PessoaPendencia> pendenciaList = new ArrayList<PessoaPendencia>();

	@ManyToOne
	@JoinColumn(name = "perfil_arquivo_id")
	private Arquivo perfilArquivo;

	@Column(name = "pessoa_tipo")
	@Enumerated(EnumType.STRING)
	protected PessoaTipo pessoaTipo;

	@OneToOne(mappedBy = "pessoa", fetch = FetchType.LAZY)
	private PublicoAlvo publicoAlvo;

	@Column(name = "publico_alvo")
	@Enumerated(EnumType.STRING)
	@NotNull
	private Confirmacao publicoAlvoConfirmacao;

	@OneToMany(mappedBy = "pessoa")
	private List<PessoaRelacionamento> relacionamentoList;

	@Enumerated(EnumType.STRING)
	@NotNull
	private PessoaSituacao situacao;

	@Column(name = "situacao_data")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar situacaoData;

	@OneToMany(mappedBy = "pessoa")
	private List<PessoaTelefone> telefoneList;

	public Pessoa() {
	}

	public Pessoa(Integer id) {
		super(id);
	}

	public Pessoa(Integer id, PessoaTipo pessoaTipo, String nome, String apelidoSigla, Arquivo perfilArquivo, PessoaSituacao situacao, Confirmacao publicoAlvoConfirmacao) {
		this(id);
		setPessoaTipo(pessoaTipo);
		setNome(nome);
		setApelidoSigla(apelidoSigla);
		setPerfilArquivo(perfilArquivo);
		setSituacao(situacao);
		setPublicoAlvoConfirmacao(publicoAlvoConfirmacao);
	}

	@Override
	public Calendar getAlteracaoData() {
		return alteracaoData;
	}

	@Override
	public Usuario getAlteracaoUsuario() {
		return alteracaoUsuario;
	}

	public String getApelidoSigla() {
		return apelidoSigla;
	}

	public List<PessoaArquivo> getArquivoList() {
		return arquivoList;
	}

	public String getChaveSisater() {
		return chaveSisater;
	}

	public Object getDiagnosticoList() {
		return diagnosticoList;
	}

	public List<PessoaEmail> getEmailList() {
		return emailList;
	}

	public List<PessoaEndereco> getEnderecoList() {
		return enderecoList;
	}

	public List<PessoaGrupoSocial> getGrupoSocialList() {
		return grupoSocialList;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public Calendar getInclusaoData() {
		return inclusaoData;
	}

	@Override
	public Usuario getInclusaoUsuario() {
		return inclusaoUsuario;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public String getInscricaoEstadualUf() {
		return inscricaoEstadualUf;
	}

	public String getNome() {
		return nome;
	}

	public String getObservacoes() {
		return observacoes;
	}

	@Override
	public List<PessoaPendencia> getPendenciaList() {
		return pendenciaList;
	}

	public Arquivo getPerfilArquivo() {
		return perfilArquivo;
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

	public List<PessoaRelacionamento> getRelacionamentoList() {
		return relacionamentoList;
	}

	public PessoaSituacao getSituacao() {
		return situacao;
	}

	public Calendar getSituacaoData() {
		return situacaoData;
	}

	public List<PessoaTelefone> getTelefoneList() {
		return telefoneList;
	}

	@Override
	public void setAlteracaoData(Calendar alteracaoData) {
		this.alteracaoData = alteracaoData;
	}

	@Override
	public void setAlteracaoUsuario(Usuario alteracaoUsuario) {
		this.alteracaoUsuario = alteracaoUsuario;
	}

	public void setApelidoSigla(String apelidoSigla) {
		this.apelidoSigla = apelidoSigla;
	}

	public void setArquivoList(List<PessoaArquivo> arquivoList) {
		this.arquivoList = arquivoList;
	}

	public void setChaveSisater(String chaveSisater) {
		this.chaveSisater = chaveSisater;
	}

	public void setDiagnosticoList(Object diagnosticoList) {
		this.diagnosticoList = diagnosticoList;
	}

	public void setEmailList(List<PessoaEmail> emailList) {
		this.emailList = emailList;
	}

	public void setEnderecoList(List<PessoaEndereco> enderecoList) {
		this.enderecoList = enderecoList;
	}

	public void setGrupoSocialList(List<PessoaGrupoSocial> grupoSocialList) {
		this.grupoSocialList = grupoSocialList;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public void setInclusaoData(Calendar inclusaoData) {
		this.inclusaoData = inclusaoData;
	}

	@Override
	public void setInclusaoUsuario(Usuario inclusaoUsuario) {
		this.inclusaoUsuario = inclusaoUsuario;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public void setInscricaoEstadualUf(String inscricaoEstadualUf) {
		this.inscricaoEstadualUf = inscricaoEstadualUf;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	@Override
	public void setPendenciaList(List<PessoaPendencia> pendenciaList) {
		this.pendenciaList = pendenciaList;
	}

	public void setPerfilArquivo(Arquivo perfilArquivo) {
		this.perfilArquivo = perfilArquivo;
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

	public void setRelacionamentoList(List<PessoaRelacionamento> relacionamentoList) {
		this.relacionamentoList = relacionamentoList;
	}

	public void setSituacao(PessoaSituacao situacao) {
		this.situacao = situacao;
	}

	public void setSituacaoData(Calendar situacaoData) {
		this.situacaoData = situacaoData;
	}

	public void setTelefoneList(List<PessoaTelefone> telefoneList) {
		this.telefoneList = telefoneList;
	}
}