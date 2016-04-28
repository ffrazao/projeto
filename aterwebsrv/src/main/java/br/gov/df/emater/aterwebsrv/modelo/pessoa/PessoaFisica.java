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

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioData;
import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.dominio.CamOrgao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.CnhCategoria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Escolaridade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.EstadoCivil;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGeracao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaNacionalidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.RegimeCasamento;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

/**
 * The persistent class for the pessoa_fisica database table.
 * 
 */
@Entity
@Table(name = "pessoa_fisica", schema = EntidadeBase.PESSOA_SCHEMA)
@PrimaryKeyJoinColumn(name = "id")
// @Indexed
public class PessoaFisica extends Pessoa {

	private static final long serialVersionUID = 1L;

	@Column(name = "cam_numero")
	// @Field(index = Index.YES, store = Store.YES)
	private String camNumero;

	@Column(name = "cam_orgao")
	@Enumerated(EnumType.STRING)
	private CamOrgao camOrgao;

	@Column(name = "cam_serie")
	// @Field(index = Index.YES, store = Store.YES)
	private String camSerie;

	@Column(name = "cam_unidade_militar")
	private String camUnidadeMilitar;

	@Column(name = "certidao_casamento_cartorio")
	// @Field(index = Index.YES, store = Store.YES)
	private String certidaoCasamentoCartorio;

	@Column(name = "certidao_casamento_folha")
	// @Field(index = Index.YES, store = Store.YES)
	private String certidaoCasamentoFolha;

	@Column(name = "certidao_casamento_livro")
	// @Field(index = Index.YES, store = Store.YES)
	private String certidaoCasamentoLivro;

	@Column(name = "certidao_casamento_regime")
	@Enumerated(EnumType.STRING)
	private RegimeCasamento certidaoCasamentoRegime;

	@Column(name = "cnh_categoria")
	@Enumerated(EnumType.STRING)
	private CnhCategoria cnhCategoria;

	@Column(name = "cnh_numero")
	// @Field(index = Index.YES, store = Store.YES)
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

	// @Field(index = Index.YES, store = Store.YES)
	private String cpf;

	@Column(name = "ctps_numero")
	// @Field(index = Index.YES, store = Store.YES)
	private String ctpsNumero;

	@Column(name = "ctps_serie")
	// @Field(index = Index.YES, store = Store.YES)
	private String ctpsSerie;


	@Enumerated(EnumType.STRING)
	private Escolaridade escolaridade;

	@Column(name = "estado_civil")
	@Enumerated(EnumType.STRING)
	private EstadoCivil estadoCivil;

	@Enumerated(EnumType.STRING)
	@NotNull
	private PessoaGenero genero;

	@Transient
	@Enumerated(EnumType.STRING)
	private PessoaGeracao geracao;

	@Transient
	private Integer idade;

	@Enumerated(EnumType.STRING)
	private PessoaNacionalidade nacionalidade;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar nascimento;

	@Transient
	private Estado nascimentoEstado;

	@ManyToOne
	@JoinColumn(name = "nascimento_municipio_id")
	private Municipio nascimentoMunicipio;

	@ManyToOne
	@JoinColumn(name = "nascimento_pais_id")
	private Pais nascimentoPais;

	@Column(name = "nis_numero")
	// @Field(index = Index.YES, store = Store.YES)
	private String nisNumero;

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
	// @Field(index = Index.YES, store = Store.YES)
	private String rgNumero;

	@Column(name = "rg_orgao_emissor")
	private String rgOrgaoEmissor;

	@Column(name = "titulo_numero")
	// @Field(index = Index.YES, store = Store.YES)
	private String tituloNumero;

	@Column(name = "titulo_secao")
	// @Field(index = Index.YES, store = Store.YES)
	private String tituloSecao;

	@Column(name = "titulo_zona")
	// @Field(index = Index.YES, store = Store.YES)
	private String tituloZona;

	public PessoaFisica() {
		setPessoaTipo(PessoaTipo.PF);
	}

	public PessoaFisica(Integer id) {
		super(id);
		setPessoaTipo(PessoaTipo.PF);
	}

	public PessoaFisica(Integer id, String nome, String apelidoSigla, Arquivo perfilArquivo, PessoaSituacao situacao, Confirmacao publicoAlvoConfirmacao, PessoaGenero genero, String cpf) {
		super(id, PessoaTipo.PF, nome, apelidoSigla, perfilArquivo, situacao, publicoAlvoConfirmacao);
		setGenero(genero);
		setCpf(cpf);
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

	public Escolaridade getEscolaridade() {
		return escolaridade;
	}

	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public PessoaGenero getGenero() {
		return genero;
	}

	public PessoaGeracao getGeracao() {
		if (this.nascimento == null) {
			return null;
		}
		int idade = UtilitarioData.getInstance().qtdAnosEntre(this.nascimento, Calendar.getInstance());
		if (idade > 0 && idade <= 12) {
			return PessoaGeracao.C;
		} else if (idade > 13 && idade <= 17) {
			return PessoaGeracao.J;
		} else if (idade > 18 && idade <= 69) {
			return PessoaGeracao.A;
		} else if (idade > 70 && idade <= 130) {
			return PessoaGeracao.I;
		} else {
			return null;
		}
	}

	public Integer getIdade() {
		if (getNascimento() == null) {
			return null;
		}
		return UtilitarioData.getInstance().qtdAnosEntre(getNascimento(), Calendar.getInstance());
	}

	public PessoaNacionalidade getNacionalidade() {
		return nacionalidade;
	}

	public Calendar getNascimento() {
		return nascimento;
	}

	public Estado getNascimentoEstado() {
		return nascimentoEstado;
	}

	public Municipio getNascimentoMunicipio() {
		return nascimentoMunicipio;
	}

	public Pais getNascimentoPais() {
		return nascimentoPais;
	}

	public String getNisNumero() {
		return nisNumero;
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

	public String getTituloNumero() {
		return tituloNumero;
	}

	public String getTituloSecao() {
		return tituloSecao;
	}

	public String getTituloZona() {
		return tituloZona;
	}

	public PessoaFisica infoBasica() {
		return new PessoaFisica(this.getId(), this.getNome(), this.getApelidoSigla(), this.getPerfilArquivo() == null ? null : this.getPerfilArquivo().infoBasica(), this.getSituacao(), this.getPublicoAlvoConfirmacao(), this.getGenero(), this.getCpf());
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

	public void setEscolaridade(Escolaridade escolaridade) {
		this.escolaridade = escolaridade;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public void setGenero(PessoaGenero genero) {
		this.genero = genero;
	}

	public void setGeracao(PessoaGeracao geracao) {
		this.geracao = geracao;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public void setNacionalidade(PessoaNacionalidade nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public void setNascimento(Calendar nascimento) {
		this.nascimento = nascimento;
	}

	public void setNascimentoEstado(Estado nascimentoEstado) {
		this.nascimentoEstado = nascimentoEstado;
	}

	public void setNascimentoMunicipio(Municipio nascimentoMunicipio) {
		this.nascimentoMunicipio = nascimentoMunicipio;
	}

	public void setNascimentoPais(Pais nascimentoPais) {
		this.nascimentoPais = nascimentoPais;
	}

	public void setNisNumero(String nisNumero) {
		this.nisNumero = nisNumero;
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