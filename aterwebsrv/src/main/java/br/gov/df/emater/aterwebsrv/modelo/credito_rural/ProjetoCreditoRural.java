package br.gov.df.emater.aterwebsrv.modelo.credito_rural;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Atividade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ProjetoCreditoStatus;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(name = "projeto_credito", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class ProjetoCreditoRural extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	private String agencia;

	@ManyToOne
	@JoinColumn(name = "agente_financeiro_id")
	private AgenteFinanceiro agenteFinanceiro;

	@ManyToOne
	@JoinColumn(name = "atividade_id")
	private Atividade atividade;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	@Column(name = "custeio_data_contratacao")
	private Calendar custeioDataContratacao;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	@Column(name = "custeio_data_final_carencia")
	private Calendar custeioDataFinalCarencia;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	@Column(name = "custeio_data_primeira_parcela")
	private Calendar custeioDataPrimeiraParcela;

	@Column(name = "custeio_juro_anual")
	private BigDecimal custeioJuroAnual;

	@Column(name = "custeio_periodicidade")
	private Integer custeioPeriodicidade;

	@Column(name = "custeio_quantidade_parcela")
	private Integer custeioQuantidadeParcela;

	@Column(name = "custeio_total_juros")
	private BigDecimal custeioTotalJuros;

	@Column(name = "custeio_total_parcela")
	private Integer custeioTotalParcela;

	@Column(name = "custeio_valor_financiamento")
	private BigDecimal custeioValorFinanciamento;

	@Column(name = "garantia_real")
	@Lob
	private String garantiaReal;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	@Column(name = "investimento_data_contratacao")
	private Calendar investimentoDataContratacao;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	@Column(name = "investimento_data_final_carencia")
	private Calendar investimentoDataFinalCarencia;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	@Column(name = "investimento_data_primeira_parcela")
	private Calendar investimentoDataPrimeiraParcela;

	@Column(name = "investimento_juro_anual")
	private BigDecimal investimentoJuroAnual;

	@Column(name = "investimento_periodicidade")
	private Integer investimentoPeriodicidade;

	@Column(name = "investimento_quantidade_parcela")
	private Integer investimentoQuantidadeParcela;

	@Column(name = "investimento_total_juros")
	private BigDecimal investimentoTotalJuros;

	@Column(name = "investimento_total_parcela")
	private Integer investimentoTotalParcela;

	@Column(name = "investimento_valor_financiamento")
	private BigDecimal investimentoValorFinanciamento;

	@ManyToOne
	@JoinColumn(name = "linha_credito_id")
	private LinhaCredito linhaCredito;

	private String nome;

	@Column(name = "numero_cedula")
	private String numeroCedula;

	@ManyToOne
	@JoinColumn(name = "propriedade_rural_id")
	private PropriedadeRural propriedadeRural;

	@Enumerated(EnumType.STRING)
	private ProjetoCreditoStatus status;

	public ProjetoCreditoRural() {
		super();
	}

	public ProjetoCreditoRural(Integer id) {
		super(id);
	}

	public String getAgencia() {
		return agencia;
	}

	public AgenteFinanceiro getAgenteFinanceiro() {
		return agenteFinanceiro;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public Calendar getCusteioDataContratacao() {
		return custeioDataContratacao;
	}

	public Calendar getCusteioDataFinalCarencia() {
		return custeioDataFinalCarencia;
	}

	public Calendar getCusteioDataPrimeiraParcela() {
		return custeioDataPrimeiraParcela;
	}

	public BigDecimal getCusteioJuroAnual() {
		return custeioJuroAnual;
	}

	public Integer getCusteioPeriodicidade() {
		return custeioPeriodicidade;
	}

	public Integer getCusteioQuantidadeParcela() {
		return custeioQuantidadeParcela;
	}

	public BigDecimal getCusteioTotalJuros() {
		return custeioTotalJuros;
	}

	public Integer getCusteioTotalParcela() {
		return custeioTotalParcela;
	}

	public BigDecimal getCusteioValorFinanciamento() {
		return custeioValorFinanciamento;
	}

	public String getGarantiaReal() {
		return garantiaReal;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Calendar getInvestimentoDataContratacao() {
		return investimentoDataContratacao;
	}

	public Calendar getInvestimentoDataFinalCarencia() {
		return investimentoDataFinalCarencia;
	}

	public Calendar getInvestimentoDataPrimeiraParcela() {
		return investimentoDataPrimeiraParcela;
	}

	public BigDecimal getInvestimentoJuroAnual() {
		return investimentoJuroAnual;
	}

	public Integer getInvestimentoPeriodicidade() {
		return investimentoPeriodicidade;
	}

	public Integer getInvestimentoQuantidadeParcela() {
		return investimentoQuantidadeParcela;
	}

	public BigDecimal getInvestimentoTotalJuros() {
		return investimentoTotalJuros;
	}

	public Integer getInvestimentoTotalParcela() {
		return investimentoTotalParcela;
	}

	public BigDecimal getInvestimentoValorFinanciamento() {
		return investimentoValorFinanciamento;
	}

	public LinhaCredito getLinhaCredito() {
		return linhaCredito;
	}

	public String getNome() {
		return nome;
	}

	public String getNumeroCedula() {
		return numeroCedula;
	}

	public PropriedadeRural getPropriedadeRural() {
		return propriedadeRural;
	}

	public ProjetoCreditoStatus getStatus() {
		return status;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public void setAgenteFinanceiro(AgenteFinanceiro agenteFinanceiro) {
		this.agenteFinanceiro = agenteFinanceiro;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public void setCusteioDataContratacao(Calendar custeioDataContratacao) {
		this.custeioDataContratacao = custeioDataContratacao;
	}

	public void setCusteioDataFinalCarencia(Calendar custeioDataFinalCarencia) {
		this.custeioDataFinalCarencia = custeioDataFinalCarencia;
	}

	public void setCusteioDataPrimeiraParcela(Calendar custeioDataPrimeiraParcela) {
		this.custeioDataPrimeiraParcela = custeioDataPrimeiraParcela;
	}

	public void setCusteioJuroAnual(BigDecimal custeioJuroAnual) {
		this.custeioJuroAnual = custeioJuroAnual;
	}

	public void setCusteioPeriodicidade(Integer custeioPeriodicidade) {
		this.custeioPeriodicidade = custeioPeriodicidade;
	}

	public void setCusteioQuantidadeParcela(Integer custeioQuantidadeParcela) {
		this.custeioQuantidadeParcela = custeioQuantidadeParcela;
	}

	public void setCusteioTotalJuros(BigDecimal custeioTotalJuros) {
		this.custeioTotalJuros = custeioTotalJuros;
	}

	public void setCusteioTotalParcela(Integer custeioTotalParcela) {
		this.custeioTotalParcela = custeioTotalParcela;
	}

	public void setCusteioValorFinanciamento(BigDecimal custeioValorFinanciamento) {
		this.custeioValorFinanciamento = custeioValorFinanciamento;
	}

	public void setGarantiaReal(String garantiaReal) {
		this.garantiaReal = garantiaReal;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setInvestimentoDataContratacao(Calendar investimentoDataContratacao) {
		this.investimentoDataContratacao = investimentoDataContratacao;
	}
	public void setInvestimentoDataFinalCarencia(Calendar investimentoDataFinalCarencia) {
		this.investimentoDataFinalCarencia = investimentoDataFinalCarencia;
	}
	public void setInvestimentoDataPrimeiraParcela(Calendar investimentoDataPrimeiraParcela) {
		this.investimentoDataPrimeiraParcela = investimentoDataPrimeiraParcela;
	}
	public void setInvestimentoJuroAnual(BigDecimal investimentoJuroAnual) {
		this.investimentoJuroAnual = investimentoJuroAnual;
	}
	public void setInvestimentoPeriodicidade(Integer investimentoPeriodicidade) {
		this.investimentoPeriodicidade = investimentoPeriodicidade;
	}
	public void setInvestimentoQuantidadeParcela(Integer investimentoQuantidadeParcela) {
		this.investimentoQuantidadeParcela = investimentoQuantidadeParcela;
	}
	public void setInvestimentoTotalJuros(BigDecimal investimentoTotalJuros) {
		this.investimentoTotalJuros = investimentoTotalJuros;
	}
	public void setInvestimentoTotalParcela(Integer investimentoTotalParcela) {
		this.investimentoTotalParcela = investimentoTotalParcela;
	}
	public void setInvestimentoValorFinanciamento(BigDecimal investimentoValorFinanciamento) {
		this.investimentoValorFinanciamento = investimentoValorFinanciamento;
	}
	public void setLinhaCredito(LinhaCredito linhaCredito) {
		this.linhaCredito = linhaCredito;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setNumeroCedula(String numeroCedula) {
		this.numeroCedula = numeroCedula;
	}
	public void setPropriedadeRural(PropriedadeRural propriedadeRural) {
		this.propriedadeRural = propriedadeRural;
	}
	public void setStatus(ProjetoCreditoStatus status) {
		this.status = status;
	}

}