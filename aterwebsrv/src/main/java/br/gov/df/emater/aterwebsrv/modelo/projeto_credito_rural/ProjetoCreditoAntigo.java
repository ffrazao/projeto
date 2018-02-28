package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(name = "antigo_projeto_credito", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class ProjetoCreditoAntigo extends EntidadeBase {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "idund")
	private String idUnidade;
	
	@Column(name = "idben")
	private String idBeneficiario;
	
	@Column(name = "idprp")
	private String idPropriedade;
	
	@Column(name = "unidade")
	private String unidade;
	
	@Column(name = "nomeund")
	private String nomeUnidade;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "bfnome")
	private String nomeBeneficiario;
	
	@Column(name = "bfcpf")
	private String cpfBeneficiario;
	
	@Column(name = "ppnome")
	private String nomePropriedade;
	
	@Column(name = "crednum")
	private String numeroDoCredito;
	
	private String agencia;

	@Column(name = "banco")
	private String banco;
	
	@Column(name = "linhacred")
	private String linhaDeCredito;
	
	@Column(name = "fininvorc")
	private String financiamentoInvestimentoOrcado;
	
	@Column(name = "fininvprop")
	private String financiamentoInvestimentoPropriedade;
	
	@Column(name = "fininvest")
	private String financiamentoInvestimento;
	
	@Column(name = "fintotorc")
	private String financiamentoTotalOrcado;
	
	@Column(name = "fincusprop")
	private String financiamentoCusteioPropriedade;
	
	@Column(name = "fincusorc")
	private String financiamentoCusteioOrcado;
	
	@Column(name = "fincusteio")
	private String financiamentoCusteio;
	
	@Column(name = "fintotprop")
	private String financiamentoTotalPropriedade;
	
	@Column(name = "fintot")
	private String financiamentoTotal;
	
	@Column(name = "CHAVE_SISATER_PESSOA")
	private String chavePessoa;
	
	@Column(name = "CHAVE_SISATER_PROPRIEDADE")
	private String chavePropriedade;
	
	@Transient
	private List<ProjetoCreditoAntigoCusteio> projetoCreditoAntigoCusteioList;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	@Column(name = "creddat")
	private Calendar contratacao;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	@Column(name = "dtvencimento")
	private Calendar vencimento;



	public ProjetoCreditoAntigo() {
		super();
	}



	public ProjetoCreditoAntigo(Integer id, String idUnidade, String idBeneficiario, String idPropriedade,
			String unidade, String nomeUnidade, String status, String nomeBeneficiario, String cpfBeneficiario,
			String nomePropriedade, String numeroDoCredito, String agencia, String banco, String linhaDeCredito,
			String financiamentoInvestimentoOrcado, String financiamentoInvestimentoPropriedade,
			String financiamentoInvestimento, String financiamentoTotalOrcado, String financiamentoCusteioPropriedade,
			String financiamentoCusteioOrcado, String financiamentoCusteio, String financiamentoTotalPropriedade,
			String financiamentoTotal, String chavePessoa, String chavePropriedade, Calendar contratacao,
			Calendar vencimento, String numeroCedula) {
		super();
		this.id = id;
		this.idUnidade = idUnidade;
		this.idBeneficiario = idBeneficiario;
		this.idPropriedade = idPropriedade;
		this.unidade = unidade;
		this.nomeUnidade = nomeUnidade;
		this.status = status;
		this.nomeBeneficiario = nomeBeneficiario;
		this.cpfBeneficiario = cpfBeneficiario;
		this.nomePropriedade = nomePropriedade;
		this.numeroDoCredito = numeroDoCredito;
		this.agencia = agencia;
		this.banco = banco;
		this.linhaDeCredito = linhaDeCredito;
		this.financiamentoInvestimentoOrcado = financiamentoInvestimentoOrcado;
		this.financiamentoInvestimentoPropriedade = financiamentoInvestimentoPropriedade;
		this.financiamentoInvestimento = financiamentoInvestimento;
		this.financiamentoTotalOrcado = financiamentoTotalOrcado;
		this.financiamentoCusteioPropriedade = financiamentoCusteioPropriedade;
		this.financiamentoCusteioOrcado = financiamentoCusteioOrcado;
		this.financiamentoCusteio = financiamentoCusteio;
		this.financiamentoTotalPropriedade = financiamentoTotalPropriedade;
		this.financiamentoTotal = financiamentoTotal;
		this.chavePessoa = chavePessoa;
		this.chavePropriedade = chavePropriedade;
		this.contratacao = contratacao;
		this.vencimento = vencimento;
	}
	
	

	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getIdUnidade() {
		return idUnidade;
	}

	public void setIdUnidade(String idUnidade) {
		this.idUnidade = idUnidade;
	}

	public String getIdBeneficiario() {
		return idBeneficiario;
	}

	public void setIdBeneficiario(String idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}

	public String getIdPropriedade() {
		return idPropriedade;
	}

	public void setIdPropriedade(String idPropriedade) {
		this.idPropriedade = idPropriedade;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getNomeUnidade() {
		return nomeUnidade;
	}

	public void setNomeUnidade(String nomeUnidade) {
		this.nomeUnidade = nomeUnidade;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNomeBeneficiario() {
		return nomeBeneficiario;
	}

	public void setNomeBeneficiario(String nomeBeneficiario) {
		this.nomeBeneficiario = nomeBeneficiario;
	}

	public String getCpfBeneficiario() {
		return cpfBeneficiario;
	}

	public void setCpfBeneficiario(String cpfBeneficiario) {
		this.cpfBeneficiario = cpfBeneficiario;
	}

	public String getNomePropriedade() {
		return nomePropriedade;
	}

	public void setNomePropriedade(String nomePropriedade) {
		this.nomePropriedade = nomePropriedade;
	}

	public String getNumeroDoCredito() {
		return numeroDoCredito;
	}

	public void setNumeroDoCredito(String numeroDoCredito) {
		this.numeroDoCredito = numeroDoCredito;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getLinhaDeCredito() {
		return linhaDeCredito;
	}

	public void setLinhaDeCredito(String linhaDeCredito) {
		this.linhaDeCredito = linhaDeCredito;
	}

	public String getFinanciamentoInvestimentoOrcado() {
		return financiamentoInvestimentoOrcado;
	}

	public void setFinanciamentoInvestimentoOrcado(String financiamentoInvestimentoOrcado) {
		this.financiamentoInvestimentoOrcado = financiamentoInvestimentoOrcado;
	}

	public String getFinanciamentoInvestimentoPropriedade() {
		return financiamentoInvestimentoPropriedade;
	}

	public void setFinanciamentoInvestimentoPropriedade(String financiamentoInvestimentoPropriedade) {
		this.financiamentoInvestimentoPropriedade = financiamentoInvestimentoPropriedade;
	}

	public String getFinanciamentoInvestimento() {
		return financiamentoInvestimento;
	}

	public void setFinanciamentoInvestimento(String financiamentoInvestimento) {
		this.financiamentoInvestimento = financiamentoInvestimento;
	}

	public String getFinanciamentoTotalOrcado() {
		return financiamentoTotalOrcado;
	}

	public void setFinanciamentoTotalOrcado(String financiamentoTotalOrcado) {
		this.financiamentoTotalOrcado = financiamentoTotalOrcado;
	}

	public String getFinanciamentoCusteioPropriedade() {
		return financiamentoCusteioPropriedade;
	}

	public void setFinanciamentoCusteioPropriedade(String financiamentoCusteioPropriedade) {
		this.financiamentoCusteioPropriedade = financiamentoCusteioPropriedade;
	}

	public String getFinanciamentoCusteioOrcado() {
		return financiamentoCusteioOrcado;
	}

	public void setFinanciamentoCusteioOrcado(String financiamentoCusteioOrcado) {
		this.financiamentoCusteioOrcado = financiamentoCusteioOrcado;
	}

	public String getFinanciamentoCusteio() {
		return financiamentoCusteio;
	}

	public void setFinanciamentoCusteio(String financiamentoCusteio) {
		this.financiamentoCusteio = financiamentoCusteio;
	}

	public String getFinanciamentoTotalPropriedade() {
		return financiamentoTotalPropriedade;
	}

	public void setFinanciamentoTotalPropriedade(String financiamentoTotalPropriedade) {
		this.financiamentoTotalPropriedade = financiamentoTotalPropriedade;
	}

	public String getFinanciamentoTotal() {
		return financiamentoTotal;
	}

	public void setFinanciamentoTotal(String financiamentoTotal) {
		this.financiamentoTotal = financiamentoTotal;
	}

	public String getChavePessoa() {
		return chavePessoa;
	}

	public void setChavePessoa(String chavePessoa) {
		this.chavePessoa = chavePessoa;
	}

	public String getChavePropriedade() {
		return chavePropriedade;
	}

	public void setChavePropriedade(String chavePropriedade) {
		this.chavePropriedade = chavePropriedade;
	}

	public Calendar getContratacao() {
		return contratacao;
	}

	public void setContratacao(Calendar contratacao) {
		this.contratacao = contratacao;
	}

	public Calendar getVencimento() {
		return vencimento;
	}

	public void setVencimento(Calendar vencimento) {
		this.vencimento = vencimento;
	}



	public List<ProjetoCreditoAntigoCusteio> getProjetoCreditoAntigoCusteioList() {
		return projetoCreditoAntigoCusteioList;
	}



	public void setProjetoCreditoAntigoCusteioList(List<ProjetoCreditoAntigoCusteio> projetoCreditoAntigoCusteioList) {
		this.projetoCreditoAntigoCusteioList = projetoCreditoAntigoCusteioList;
	}
	
	

}