package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import java.math.BigDecimal;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Atividade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FinanciamentoTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ProjetoCreditoRuralPeriodicidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ProjetoCreditoRuralStatus;
import br.gov.df.emater.aterwebsrv.modelo.dto.ProjetoCreditoRuralCronogramaDto;
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

	@Transient
	private FinanciamentoTipo calculoTipo;

	@OneToMany(mappedBy = "projetoCreditoRural")
	@Where(clause = "tipo = 'C'")
	private List<ProjetoCreditoRuralCronogramaPagamento> cronogramaCusteioList;

	@OneToMany(mappedBy = "projetoCreditoRural")
	@Where(clause = "tipo = 'I'")
	private List<ProjetoCreditoRuralCronogramaPagamento> cronogramaInvestimentoList;

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

	@OneToMany(mappedBy = "projetoCreditoRural")
	@Where(clause = "tipo = 'C'")
	private List<ProjetoCreditoRuralFinanciamento> custeioList;

	@Column(name = "custeio_periodicidade")
	@Enumerated(EnumType.STRING)
	private ProjetoCreditoRuralPeriodicidade custeioPeriodicidade;

	@Column(name = "custeio_quantidade_parcelas")
	private Integer custeioQuantidadeParcelas;

	@Column(name = "custeio_taxa_juros_anual")
	private BigDecimal custeioTaxaJurosAnual;

	@Column(name = "custeio_valor_financiamento")
	private BigDecimal custeioValorFinanciamento;

	@Column(name = "custeio_valor_total_juros")
	private BigDecimal custeioValorTotalJuros;

	@Column(name = "custeio_valor_total_prestacoes")
	private BigDecimal custeioValorTotalPrestacoes;

	@OneToMany(mappedBy = "projetoCreditoRural")
	@Where(clause = "tipo = 'D'")
	private List<ProjetoCreditoRuralReceitaDespesa> despesaList;

	@OneToMany(mappedBy = "projetoCreditoRural")
	private List<ProjetoCreditoRuralFluxoCaixa> fluxoCaixaList;

	@OneToMany(mappedBy = "projetoCreditoRural")
	private List<ProjetoCreditoRuralGarantia> garantiaList;

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

	@OneToMany(mappedBy = "projetoCreditoRural")
	@Where(clause = "tipo = 'I'")
	private List<ProjetoCreditoRuralFinanciamento> investimentoList;

	@Column(name = "investimento_periodicidade")
	@Enumerated(EnumType.STRING)
	private ProjetoCreditoRuralPeriodicidade investimentoPeriodicidade;

	@Column(name = "investimento_quantidade_parcela")
	private Integer investimentoQuantidadeParcelas;

	@Column(name = "investimento_taxa_juros_anual")
	private BigDecimal investimentoTaxaJurosAnual;

	@Column(name = "investimento_valor_financiamento")
	private BigDecimal investimentoValorFinanciamento;

	@Column(name = "investimento_valor_total_juros")
	private BigDecimal investimentoValorTotalJuros;

	@Column(name = "investimento_valor_total_prestacoes")
	private BigDecimal investimentoValorTotalPrestacoes;

	@ManyToOne
	@JoinColumn(name = "linha_credito_id")
	private LinhaCredito linhaCredito;

	private String nome;

	@Column(name = "numero_cedula")
	private String numeroCedula;

	@OneToMany(mappedBy = "projetoCreditoRural")
	private List<ProjetoCreditoRuralParecerTecnico> parecerTecnicoList;

	@ManyToOne
	@JoinColumn(name = "propriedade_rural_id")
	private PropriedadeRural propriedadeRural;

	@OneToMany(mappedBy = "projetoCreditoRural")
	@Where(clause = "tipo = 'R'")
	private List<ProjetoCreditoRuralReceitaDespesa> receitaList;

	@Enumerated(EnumType.STRING)
	private ProjetoCreditoRuralStatus status;

	@OneToMany(mappedBy = "projetoCreditoRural")
	private List<ProjetoCreditoRuralHistoricoReceita> trienioList;

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

	public FinanciamentoTipo getCalculoTipo() {
		return calculoTipo;
	}

	public ProjetoCreditoRuralCronogramaDto getCronograma() throws BoException {
		ProjetoCreditoRuralCronogramaDto result = new ProjetoCreditoRuralCronogramaDto();
		result.setTipo(getCalculoTipo());

		switch (result.getTipo()) {
		case C:
			result.setPeriodicidade(this.custeioPeriodicidade);
			result.setDataContratacao(this.custeioDataContratacao);
			result.setValorFinanciamento(this.custeioValorFinanciamento);
			result.setTaxaJurosAnual(this.custeioTaxaJurosAnual);
			result.setQuantidadeParcelas(this.custeioQuantidadeParcelas);
			result.setDataFinalCarencia(this.custeioDataFinalCarencia);
			result.setDataPrimeiraParcela(this.custeioDataPrimeiraParcela);
			//result.setValorTotalJuros(this.custeioValorTotalJuros);
			//result.setValorTotalParcelas(this.custeioValorTotalParcelas);
			break;
		case I:
			result.setPeriodicidade(this.investimentoPeriodicidade);
			result.setDataContratacao(this.investimentoDataContratacao);
			result.setValorFinanciamento(this.investimentoValorFinanciamento);
			result.setTaxaJurosAnual(this.investimentoTaxaJurosAnual);
			result.setQuantidadeParcelas(this.investimentoQuantidadeParcelas);
			result.setDataFinalCarencia(this.investimentoDataFinalCarencia);
			result.setDataPrimeiraParcela(this.investimentoDataPrimeiraParcela);
			//result.setValorTotalJuros(this.investimentoValorTotalJuros);
			//result.setValorTotalParcelas(this.investimentoValorTotalParcelas);
			break;
		default:
			throw new BoException("Tipo de cronograma de projeto de crédito rural inválido");
		}
		return result;
	}

	public List<ProjetoCreditoRuralCronogramaPagamento> getCronogramaCusteioList() {
		return cronogramaCusteioList;
	}

	public List<ProjetoCreditoRuralCronogramaPagamento> getCronogramaInvestimentoList() {
		return cronogramaInvestimentoList;
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

	public List<ProjetoCreditoRuralFinanciamento> getCusteioList() {
		return custeioList;
	}

	public ProjetoCreditoRuralPeriodicidade getCusteioPeriodicidade() {
		return custeioPeriodicidade;
	}

	public Integer getCusteioQuantidadeParcelas() {
		return custeioQuantidadeParcelas;
	}

	public BigDecimal getCusteioTaxaJurosAnual() {
		return custeioTaxaJurosAnual;
	}

	public BigDecimal getCusteioValorFinanciamento() {
		return custeioValorFinanciamento;
	}

	public BigDecimal getCusteioValorTotalJuros() {
		return custeioValorTotalJuros;
	}

	public BigDecimal getCusteioValorTotalPrestacoes() {
		return custeioValorTotalPrestacoes;
	}

	public List<ProjetoCreditoRuralReceitaDespesa> getDespesaList() {
		return despesaList;
	}

	public List<ProjetoCreditoRuralFluxoCaixa> getFluxoCaixaList() {
		return fluxoCaixaList;
	}

	public List<ProjetoCreditoRuralGarantia> getGarantiaList() {
		return garantiaList;
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

	public List<ProjetoCreditoRuralFinanciamento> getInvestimentoList() {
		return investimentoList;
	}

	public ProjetoCreditoRuralPeriodicidade getInvestimentoPeriodicidade() {
		return investimentoPeriodicidade;
	}

	public Integer getInvestimentoQuantidadeParcelas() {
		return investimentoQuantidadeParcelas;
	}

	public BigDecimal getInvestimentoTaxaJurosAnual() {
		return investimentoTaxaJurosAnual;
	}

	public BigDecimal getInvestimentoValorFinanciamento() {
		return investimentoValorFinanciamento;
	}

	public BigDecimal getInvestimentoValorTotalJuros() {
		return investimentoValorTotalJuros;
	}

	public BigDecimal getInvestimentoValorTotalPrestacoes() {
		return investimentoValorTotalPrestacoes;
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

	public List<ProjetoCreditoRuralParecerTecnico> getParecerTecnicoList() {
		return parecerTecnicoList;
	}

	public PropriedadeRural getPropriedadeRural() {
		return propriedadeRural;
	}

	public List<ProjetoCreditoRuralReceitaDespesa> getReceitaList() {
		return receitaList;
	}

	public ProjetoCreditoRuralStatus getStatus() {
		return status;
	}

	public List<ProjetoCreditoRuralHistoricoReceita> getTrienioList() {
		return trienioList;
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

	public void setCalculoTipo(FinanciamentoTipo calculoTipo) {
		this.calculoTipo = calculoTipo;
	}

	public void setCronogramaCusteioList(List<ProjetoCreditoRuralCronogramaPagamento> cronogramaCusteioList) {
		this.cronogramaCusteioList = cronogramaCusteioList;
	}

	public void setCronogramaInvestimentoList(List<ProjetoCreditoRuralCronogramaPagamento> cronogramaInvestimentoList) {
		this.cronogramaInvestimentoList = cronogramaInvestimentoList;
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

	public void setCusteioList(List<ProjetoCreditoRuralFinanciamento> custeioList) {
		this.custeioList = custeioList;
	}

	public void setCusteioPeriodicidade(ProjetoCreditoRuralPeriodicidade custeioPeriodicidade) {
		this.custeioPeriodicidade = custeioPeriodicidade;
	}

	public void setCusteioQuantidadeParcelas(Integer custeioQuantidadeParcelas) {
		this.custeioQuantidadeParcelas = custeioQuantidadeParcelas;
	}

	public void setCusteioTaxaJurosAnual(BigDecimal custeioTaxaJurosAnual) {
		this.custeioTaxaJurosAnual = custeioTaxaJurosAnual;
	}

	public void setCusteioValorFinanciamento(BigDecimal custeioValorFinanciamento) {
		this.custeioValorFinanciamento = custeioValorFinanciamento;
	}

	public void setCusteioValorTotalJuros(BigDecimal custeioValorTotalJuros) {
		this.custeioValorTotalJuros = custeioValorTotalJuros;
	}

	public void setCusteioValorTotalPrestacoes(BigDecimal custeioValorTotalPrestacoes) {
		this.custeioValorTotalPrestacoes = custeioValorTotalPrestacoes;
	}

	public void setDespesaList(List<ProjetoCreditoRuralReceitaDespesa> despesaList) {
		this.despesaList = despesaList;
	}

	public void setFluxoCaixaList(List<ProjetoCreditoRuralFluxoCaixa> fluxoCaixaList) {
		this.fluxoCaixaList = fluxoCaixaList;
	}

	public void setGarantiaList(List<ProjetoCreditoRuralGarantia> garantiaList) {
		this.garantiaList = garantiaList;
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

	public void setInvestimentoList(List<ProjetoCreditoRuralFinanciamento> investimentoList) {
		this.investimentoList = investimentoList;
	}

	public void setInvestimentoPeriodicidade(ProjetoCreditoRuralPeriodicidade investimentoPeriodicidade) {
		this.investimentoPeriodicidade = investimentoPeriodicidade;
	}

	public void setInvestimentoQuantidadeParcelas(Integer investimentoQuantidadeParcelas) {
		this.investimentoQuantidadeParcelas = investimentoQuantidadeParcelas;
	}

	public void setInvestimentoTaxaJurosAnual(BigDecimal investimentoTaxaJurosAnual) {
		this.investimentoTaxaJurosAnual = investimentoTaxaJurosAnual;
	}

	public void setInvestimentoValorFinanciamento(BigDecimal investimentoValorFinanciamento) {
		this.investimentoValorFinanciamento = investimentoValorFinanciamento;
	}

	public void setInvestimentoValorTotalJuros(BigDecimal investimentoValorTotalJuros) {
		this.investimentoValorTotalJuros = investimentoValorTotalJuros;
	}

	public void setInvestimentoValorTotalPrestacoes(BigDecimal investimentoValorTotalPrestacoes) {
		this.investimentoValorTotalPrestacoes = investimentoValorTotalPrestacoes;
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

	public void setParecerTecnicoList(List<ProjetoCreditoRuralParecerTecnico> parecerTecnicoList) {
		this.parecerTecnicoList = parecerTecnicoList;
	}

	public void setPropriedadeRural(PropriedadeRural propriedadeRural) {
		this.propriedadeRural = propriedadeRural;
	}

	public void setReceitaList(List<ProjetoCreditoRuralReceitaDespesa> receitaList) {
		this.receitaList = receitaList;
	}

	public void setStatus(ProjetoCreditoRuralStatus status) {
		this.status = status;
	}

	public void setTrienioList(List<ProjetoCreditoRuralHistoricoReceita> trienioList) {
		this.trienioList = trienioList;
	}

}