package br.gov.df.emater.aterwebsrv.modelo.cad_geral;

import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Escolaridade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Sexo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.StatusValidoInvalido;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "cad_pessoa", schema = EntidadeBase.CAD_GERAL_SCHEMA)
public class CadPessoa extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "bairro")
	private String bairro;

	@ManyToOne
	@JoinColumn(name = "origem_id")
	private CadOrigem cadOrigem;

	@OneToMany(mappedBy = "cadPessoa")
	private List<CadPessoaErro> cadPessoaErroList;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "cad_pessoa_propriedade", schema = EntidadeBase.CAD_GERAL_SCHEMA, joinColumns = { @JoinColumn(name = "cad_pessoa_id") }, inverseJoinColumns = { @JoinColumn(name = "cad_propriedade_id") })
	private List<CadPropriedade> cadPropriedadeList;

	@Column(name = "caixa_postal")
	private String caixaPostal;

	@Column(name = "cep")
	private String cep;

	@Column(name = "chave")
	private String chave;

	@Column(name = "cidade")
	private String cidade;

	@Column(name = "cpf")
	private String cpf;

	@Column(name = "data_informacoes")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar dataInformacoes;

	@Column(name = "email")
	private String email;

	@Column(name = "endereco")
	private String endereco;

	@Column(name = "escolaridade")
	@Enumerated(EnumType.STRING)
	private Escolaridade escolaridade;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "nascimento")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar nascimento;

	@Column(name = "nome")
	private String nome;

	@Column(name = "numero_dap")
	private String numeroDap;

	@Column(name = "numero_inscricao_sef_df")
	private String numeroInscricaoSefDf;

	@Column(name = "reside_propriedade")
	@Enumerated(EnumType.STRING)
	private Confirmacao residePropriedade;

	@Column(name = "sexo")
	@Enumerated(EnumType.STRING)
	private Sexo sexo;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private StatusValidoInvalido status;

	@Column(name = "telefone_1")
	private String telefone1;

	@Column(name = "telefone_2")
	private String telefone2;

	@Column(name = "uf")
	private String uf;

	public CadPessoa() {
	}

	public String getBairro() {
		return bairro;
	}

	public CadOrigem getCadOrigem() {
		return cadOrigem;
	}

	public List<CadPessoaErro> getCadPessoaErroList() {
		return cadPessoaErroList;
	}

	public List<CadPropriedade> getCadPropriedadeList() {
		return cadPropriedadeList;
	}

	public String getCaixaPostal() {
		return caixaPostal;
	}

	public String getCep() {
		return cep;
	}

	public String getChave() {
		return chave;
	}

	public String getCidade() {
		return cidade;
	}

	public String getCpf() {
		return cpf;
	}

	public Calendar getDataInformacoes() {
		return dataInformacoes;
	}

	public String getEmail() {
		return email;
	}

	public String getEndereco() {
		return endereco;
	}

	public Escolaridade getEscolaridade() {
		return escolaridade;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Calendar getNascimento() {
		return nascimento;
	}

	public String getNome() {
		return nome;
	}

	public String getNumeroDap() {
		return numeroDap;
	}

	public String getNumeroInscricaoSefDf() {
		return numeroInscricaoSefDf;
	}

	public Confirmacao getResidePropriedade() {
		return residePropriedade;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public StatusValidoInvalido getStatus() {
		return status;
	}

	public String getTelefone1() {
		return telefone1;
	}

	public String getTelefone2() {
		return telefone2;
	}

	public String getUf() {
		return uf;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public void setCadOrigem(CadOrigem cadOrigem) {
		this.cadOrigem = cadOrigem;
	}

	public void setCadPessoaErroList(List<CadPessoaErro> cadPessoaErroList) {
		this.cadPessoaErroList = cadPessoaErroList;
	}

	public void setCadPropriedadeList(List<CadPropriedade> cadPropriedadeList) {
		this.cadPropriedadeList = cadPropriedadeList;
	}

	public void setCaixaPostal(String caixaPostal) {
		this.caixaPostal = caixaPostal;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setDataInformacoes(Calendar dataInformacoes) {
		this.dataInformacoes = dataInformacoes;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public void setEscolaridade(Escolaridade escolaridade) {
		this.escolaridade = escolaridade;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNascimento(Calendar nascimento) {
		this.nascimento = nascimento;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setNumeroDap(String numeroDap) {
		this.numeroDap = numeroDap;
	}

	public void setNumeroInscricaoSefDf(String numeroInscricaoSefDf) {
		this.numeroInscricaoSefDf = numeroInscricaoSefDf;
	}

	public void setResidePropriedade(Confirmacao residePropriedade) {
		this.residePropriedade = residePropriedade;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public void setStatus(StatusValidoInvalido status) {
		this.status = status;
	}

	public void setTelefone1(String telefone1) {
		this.telefone1 = telefone1;
	}

	public void setTelefone2(String telefone2) {
		this.telefone2 = telefone2;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

}