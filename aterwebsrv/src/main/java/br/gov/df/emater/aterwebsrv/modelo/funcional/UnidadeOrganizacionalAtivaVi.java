package br.gov.df.emater.aterwebsrv.modelo.funcional;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UnidadeOrganizacionalClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

/*
 * ATENÇÃO! Qualquer alteração na estrutura desta classe também deve ser feita na interface UnidadeOrganizacionalBase 
 */
@Entity
@Table(name = "unidade_organizacional_ativa_vi", schema = EntidadeBase.FUNCIONAL_SCHEMA)
// @Immutable
public class UnidadeOrganizacionalAtivaVi extends EntidadeBase
		implements _ChavePrimaria<Integer>, InfoBasica<UnidadeOrganizacionalAtivaVi> {

	private static final long serialVersionUID = 1L;

	@Column(name = "apelido_sigla")
	private String apelidoSigla;

	@Column(name = "chave_sisater")
	private String chaveSisater;

	@Enumerated(EnumType.STRING)
	private UnidadeOrganizacionalClassificacao classificacao;

	@OneToMany(mappedBy = "unidadeOrganizacional")
	private List<Comunidade> comunidadeList;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inicio;

	private String nome;

	@ManyToOne
	@JoinColumn(name = "pessoa_juridica_id")
	private PessoaJuridica pessoaJuridica;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar termino;

	public UnidadeOrganizacionalAtivaVi() {
	}

	public UnidadeOrganizacionalAtivaVi(Serializable id) {
		super(id);
	}

	public UnidadeOrganizacionalAtivaVi(Serializable id, String nome, String sigla, PessoaJuridica pessoaJuridica,
			UnidadeOrganizacionalClassificacao classificacao) {
		this(id);
		setNome(nome);
		setApelidoSigla(sigla);
		setPessoaJuridica(pessoaJuridica);
		setClassificacao(classificacao);
	}

	public String getApelidoSigla() {
		return apelidoSigla;
	}

	public String getChaveSisater() {
		return chaveSisater;
	}

	public UnidadeOrganizacionalClassificacao getClassificacao() {
		return classificacao;
	}

	public List<Comunidade> getComunidadeList() {
		return comunidadeList;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public String getNome() {
		return nome;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public Calendar getTermino() {
		return termino;
	}

	public UnidadeOrganizacionalAtivaVi infoBasica() {
		return new UnidadeOrganizacionalAtivaVi(this.id, this.nome, this.apelidoSigla,
				this.pessoaJuridica == null ? null : (PessoaJuridica) pessoaJuridica.infoBasica(), this.classificacao);
	}

	public void setApelidoSigla(String apelidoSigla) {
		this.apelidoSigla = apelidoSigla;
	}

	public void setChaveSisater(String chaveSisater) {
		this.chaveSisater = chaveSisater;
	}

	public void setClassificacao(UnidadeOrganizacionalClassificacao classificacao) {
		this.classificacao = classificacao;
	}

	public void setComunidadeList(List<Comunidade> comunidadeList) {
		this.comunidadeList = comunidadeList;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

	public void setTermino(Calendar termino) {
		this.termino = termino;
	}

}