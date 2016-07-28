package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

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
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Atividade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FinanciamentoTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ProjetoCreditoRuralStatus;
import br.gov.df.emater.aterwebsrv.modelo.dto.ProjetoCreditoRuralCronogramaDto;

@Entity
@Table(name = "projeto_credito_rural", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
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
	private List<ProjetoCreditoRuralCronogramaPagamento> cronogramaPagamentoCusteioList;

	@OneToMany(mappedBy = "projetoCreditoRural")
	@Where(clause = "tipo = 'I'")
	private List<ProjetoCreditoRuralCronogramaPagamento> cronogramaPagamentoInvestimentoList;

	@OneToMany(mappedBy = "projetoCreditoRural")
	@Where(clause = "tipo = 'C'")
	private List<ProjetoCreditoRuralFinanciamento> custeioList;

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

	@OneToMany(mappedBy = "projetoCreditoRural")
	@Where(clause = "tipo = 'I'")
	private List<ProjetoCreditoRuralFinanciamento> investimentoList;

	@ManyToOne
	@JoinColumn(name = "linha_credito_id")
	private LinhaCredito linhaCredito;

	private String nome;

	@Column(name = "numero_cedula")
	private String numeroCedula;

	@OneToMany(mappedBy = "projetoCreditoRural")
	private List<ProjetoCreditoRuralParecerTecnico> parecerTecnicoList;

	@OneToMany(mappedBy = "projetoCreditoRural")
	private List<ProjetoCreditoRuralPropriedadeRural> propriedadeRuralList;

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
			// result.setPeriodicidade(this.custeioPeriodicidade);
			// result.setDataContratacao(this.custeioDataContratacao);
			// result.setValorFinanciamento(this.custeioValorFinanciamento);
			// result.setTaxaJurosAnual(this.custeioTaxaJurosAnual);
			// result.setQuantidadeParcelas(this.custeioQuantidadeParcelas);
			// result.setDataFinalCarencia(this.custeioDataFinalCarencia);
			// result.setDataPrimeiraParcela(this.custeioDataPrimeiraParcela);
			// result.setValorTotalJuros(this.custeioValorTotalJuros);
			// result.setValorTotalParcelas(this.custeioValorTotalParcelas);
			break;
		case I:
			// result.setPeriodicidade(this.investimentoPeriodicidade);
			// result.setDataContratacao(this.investimentoDataContratacao);
			// result.setValorFinanciamento(this.investimentoValorFinanciamento);
			// result.setTaxaJurosAnual(this.investimentoTaxaJurosAnual);
			// result.setQuantidadeParcelas(this.investimentoQuantidadeParcelas);
			// result.setDataFinalCarencia(this.investimentoDataFinalCarencia);
			// result.setDataPrimeiraParcela(this.investimentoDataPrimeiraParcela);
			// result.setValorTotalJuros(this.investimentoValorTotalJuros);
			// result.setValorTotalParcelas(this.investimentoValorTotalParcelas);
			break;
		default:
			throw new BoException("Tipo de cronograma de projeto de crédito rural inválido");
		}
		return result;
	}

	public List<ProjetoCreditoRuralCronogramaPagamento> getCronogramaPagamentoCusteioList() {
		return cronogramaPagamentoCusteioList;
	}

	public List<ProjetoCreditoRuralCronogramaPagamento> getCronogramaPagamentoInvestimentoList() {
		return cronogramaPagamentoInvestimentoList;
	}

	public List<ProjetoCreditoRuralFinanciamento> getCusteioList() {
		return custeioList;
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

	public List<ProjetoCreditoRuralFinanciamento> getInvestimentoList() {
		return investimentoList;
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

	public List<ProjetoCreditoRuralPropriedadeRural> getPropriedadeRuralList() {
		return propriedadeRuralList;
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

	public void setCronogramaPagamentoCusteioList(List<ProjetoCreditoRuralCronogramaPagamento> cronogramaPagamentoCusteioList) {
		this.cronogramaPagamentoCusteioList = cronogramaPagamentoCusteioList;
	}

	public void setCronogramaPagamentoInvestimentoList(List<ProjetoCreditoRuralCronogramaPagamento> cronogramaPagamentoInvestimentoList) {
		this.cronogramaPagamentoInvestimentoList = cronogramaPagamentoInvestimentoList;
	}

	public void setCusteioList(List<ProjetoCreditoRuralFinanciamento> custeioList) {
		this.custeioList = custeioList;
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

	public void setInvestimentoList(List<ProjetoCreditoRuralFinanciamento> investimentoList) {
		this.investimentoList = investimentoList;
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

	public void setPropriedadeRuralList(List<ProjetoCreditoRuralPropriedadeRural> propriedadeRuralList) {
		this.propriedadeRuralList = propriedadeRuralList;
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