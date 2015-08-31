package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.springframework.format.annotation.DateTimeFormat;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.dominio.CamOrgao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.CnhCategoria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Escolaridade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.EstadoCivil;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Nacionalidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGeracao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.RegimeCasamento;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Sexo;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * The persistent class for the pessoa_fisica database table.
 * 
 */
@Entity
@Table(name = "pessoa_fisica", schema = EntidadeBase.PESSOA_SCHEMA)
@PrimaryKeyJoinColumn(name = "id")
@Indexed
public class PessoaFisica extends Pessoa {

	private static final long serialVersionUID = 1L;

	@Column(name = "cam_numero")
	@Field(index = Index.YES, store = Store.YES)
	private String camNumero;

	@Column(name = "cam_orgao")
	@Enumerated(EnumType.STRING)
	private CamOrgao camOrgao;

	@Column(name = "cam_serie")
	@Field(index = Index.YES, store = Store.YES)
	private String camSerie;

	@Column(name = "cam_unidade_militar")
	private String camUnidadeMilitar;

	@Column(name = "certidao_casamento_cartorio")
	@Field(index = Index.YES, store = Store.YES)
	private String certidaoCasamentoCartorio;

	@Column(name = "certidao_casamento_folha")
	@Field(index = Index.YES, store = Store.YES)
	private String certidaoCasamentoFolha;

	@Column(name = "certidao_casamento_livro")
	@Field(index = Index.YES, store = Store.YES)
	private String certidaoCasamentoLivro;

	@Column(name = "certidao_casamento_regime")
	@Enumerated(EnumType.STRING)
	private RegimeCasamento certidaoCasamentoRegime;

	@Column(name = "cnh_categoria")
	@Enumerated(EnumType.STRING)
	private CnhCategoria cnhCategoria;

	@Column(name = "cnh_numero")
	@Field(index = Index.YES, store = Store.YES)
	private String cnhNumero;

	@Column(name = "cnh_primeira_habilitacao")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar cnhPrimeiraHabilitacao;

	@Column(name = "cnh_validade")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar cnhValidade;

	@Field(index = Index.YES, store = Store.YES)
	private String cpf;

	@Column(name = "ctps_numero")
	@Field(index = Index.YES, store = Store.YES)
	private String ctpsNumero;

	@Column(name = "ctps_serie")
	@Field(index = Index.YES, store = Store.YES)
	private String ctpsSerie;

	@Column(name = "dap_registro")
	@Field(index = Index.YES, store = Store.YES)
	private String dapRegistro;

	@Enumerated(EnumType.STRING)
	private Escolaridade escolaridade;
	
	@Column(name = "estado_civil")
	@Enumerated(EnumType.STRING)
	private EstadoCivil estadoCivil;

	@Enumerated(EnumType.STRING)
	private Nacionalidade nacionalidade;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar nascimento;

	@ManyToOne
	@JoinColumn(name = "nascimento_pessoa_grupo_id")
	private PessoaGrupo nascimentoPessoaGrupo;

	@Column(name = "nis_numero")
	@Field(index = Index.YES, store = Store.YES)
	private String nisNumero;

	@Transient
	@Enumerated(EnumType.STRING)
	private PessoaGeracao pessoaGeracao;

	@ManyToOne
	@JoinColumn(name = "profissao_id")
	private Profissao profissao;

	@Column(name = "rg_data_emissao")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar rgDataEmissao;

	@Column(name = "rg_numero")
	@Field(index = Index.YES, store = Store.YES)
	private String rgNumero;

	@Column(name = "rg_orgao_emissor")
	private String rgOrgaoEmissor;

	@ManyToOne
	@JoinColumn(name = "rg_pessoa_grupo_id")
	private PessoaGrupoMunicipioVi rgPessoaGrupoMunicipioVi;

	@Enumerated(EnumType.STRING)
	@NotNull
	private Sexo sexo;

	@Column(name = "titulo_numero")
	@Field(index = Index.YES, store = Store.YES)
	private String tituloNumero;

	@Column(name = "titulo_secao")
	@Field(index = Index.YES, store = Store.YES)
	private String tituloSecao;

	@Column(name = "titulo_zona")
	@Field(index = Index.YES, store = Store.YES)
	private String tituloZona;

	public PessoaFisica() {
		setPessoaTipo(PessoaTipo.PF);
	}

	public PessoaFisica(Integer id) {
		super(id);
		setPessoaTipo(PessoaTipo.PF);
	}

	public PessoaFisica(Integer id, String nome, String apelidoSigla) {
		super(id, nome, apelidoSigla);
		setPessoaTipo(PessoaTipo.PF);
	}
	
	public String getCamNumero() {
		return camNumero;
	}

	public CamOrgao getCamOrgao() {
		return camOrgao;
	}

	public String getCamSerie() {
		return camSerie;
	}

	public String getCamUnidadeMilitar() {
		return camUnidadeMilitar;
	}

	public String getCertidaoCasamentoCartorio() {
		return certidaoCasamentoCartorio;
	}

	public String getCertidaoCasamentoFolha() {
		return certidaoCasamentoFolha;
	}

	public String getCertidaoCasamentoLivro() {
		return certidaoCasamentoLivro;
	}

	public RegimeCasamento getCertidaoCasamentoRegime() {
		return certidaoCasamentoRegime;
	}

	public CnhCategoria getCnhCategoria() {
		return cnhCategoria;
	}

	public String getCnhNumero() {
		return cnhNumero;
	}

	public Calendar getCnhPrimeiraHabilitacao() {
		return cnhPrimeiraHabilitacao;
	}

	public Calendar getCnhValidade() {
		return cnhValidade;
	}

	public String getCpf() {
		return cpf;
	}

	public String getCtpsNumero() {
		return ctpsNumero;
	}

	public String getCtpsSerie() {
		return ctpsSerie;
	}

	public String getDapRegistro() {
		return dapRegistro;
	}

	public Escolaridade getEscolaridade() {
		return escolaridade;
	}

	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public Nacionalidade getNacionalidade() {
		return nacionalidade;
	}

	public Calendar getNascimento() {
		return nascimento;
	}

	public PessoaGrupo getNascimentoPessoaGrupo() {
		return nascimentoPessoaGrupo;
	}

	public String getNisNumero() {
		return nisNumero;
	}

	public PessoaGeracao getPessoaGeracao() {
		return pessoaGeracao;
	}

	public Profissao getProfissao() {
		return profissao;
	}

	public Calendar getRgDataEmissao() {
		return rgDataEmissao;
	}

	public String getRgNumero() {
		return rgNumero;
	}

	public String getRgOrgaoEmissor() {
		return rgOrgaoEmissor;
	}

	public PessoaGrupoMunicipioVi getRgPessoaGrupoMunicipioVi() {
		return rgPessoaGrupoMunicipioVi;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public String getTituloNumero() {
		return tituloNumero;
	}

	public String getTituloSecao() {
		return tituloSecao;
	}

	public String getTituloZona() {
		return tituloZona;
	}

	public void setCamNumero(String camNumero) {
		this.camNumero = camNumero;
	}

	public void setCamOrgao(CamOrgao camOrgao) {
		this.camOrgao = camOrgao;
	}

	public void setCamSerie(String camSerie) {
		this.camSerie = camSerie;
	}

	public void setCamUnidadeMilitar(String camUnidadeMilitar) {
		this.camUnidadeMilitar = camUnidadeMilitar;
	}

	public void setCertidaoCasamentoCartorio(String certidaoCasamentoCartorio) {
		this.certidaoCasamentoCartorio = certidaoCasamentoCartorio;
	}

	public void setCertidaoCasamentoFolha(String certidaoCasamentoFolha) {
		this.certidaoCasamentoFolha = certidaoCasamentoFolha;
	}

	public void setCertidaoCasamentoLivro(String certidaoCasamentoLivro) {
		this.certidaoCasamentoLivro = certidaoCasamentoLivro;
	}

	public void setCertidaoCasamentoRegime(RegimeCasamento certidaoCasamentoRegime) {
		this.certidaoCasamentoRegime = certidaoCasamentoRegime;
	}

	public void setCnhCategoria(CnhCategoria cnhCategoria) {
		this.cnhCategoria = cnhCategoria;
	}

	public void setCnhNumero(String cnhNumero) {
		this.cnhNumero = cnhNumero;
	}

	public void setCnhPrimeiraHabilitacao(Calendar cnhPrimeiraHabilitacao) {
		this.cnhPrimeiraHabilitacao = cnhPrimeiraHabilitacao;
	}

	public void setCnhValidade(Calendar cnhValidade) {
		this.cnhValidade = cnhValidade;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setCtpsNumero(String ctpsNumero) {
		this.ctpsNumero = ctpsNumero;
	}

	public void setCtpsSerie(String ctpsSerie) {
		this.ctpsSerie = ctpsSerie;
	}

	public void setDapRegistro(String dapRegistro) {
		this.dapRegistro = dapRegistro;
	}

	public void setEscolaridade(Escolaridade escolaridade) {
		this.escolaridade = escolaridade;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public void setNacionalidade(Nacionalidade nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public void setNascimento(Calendar nascimento) {
		this.nascimento = nascimento;
	}

	public void setNascimentoPessoaGrupo(PessoaGrupo nascimentoPessoaGrupo) {
		this.nascimentoPessoaGrupo = nascimentoPessoaGrupo;
	}

	public void setNisNumero(String nisNumero) {
		this.nisNumero = nisNumero;
	}

	public void setPessoaGeracao(PessoaGeracao pessoaGeracao) {
		this.pessoaGeracao = pessoaGeracao;
	}

	public void setProfissao(Profissao profissao) {
		this.profissao = profissao;
	}

	public void setRgDataEmissao(Calendar rgDataEmissao) {
		this.rgDataEmissao = rgDataEmissao;
	}

	public void setRgNumero(String rgNumero) {
		this.rgNumero = rgNumero;
	}

	public void setRgOrgaoEmissor(String rgOrgaoEmissor) {
		this.rgOrgaoEmissor = rgOrgaoEmissor;
	}

	public void setRgPessoaGrupoMunicipioVi(PessoaGrupoMunicipioVi rgPessoaGrupoMunicipioVi) {
		this.rgPessoaGrupoMunicipioVi = rgPessoaGrupoMunicipioVi;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public void setTituloNumero(String tituloNumero) {
		this.tituloNumero = tituloNumero;
	}

	public void setTituloSecao(String tituloSecao) {
		this.tituloSecao = tituloSecao;
	}

	public void setTituloZona(String tituloZona) {
		this.tituloZona = tituloZona;
	}

}