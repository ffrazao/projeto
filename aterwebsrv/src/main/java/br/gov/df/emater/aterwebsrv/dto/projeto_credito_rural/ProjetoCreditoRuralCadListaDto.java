package br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import br.gov.df.emater.aterwebsrv.dto.CadListaDtoCustom;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ProjetoCreditoRuralStatus;

public class ProjetoCreditoRuralCadListaDto extends CadListaDtoCustom {

	private static final long serialVersionUID = 1L;

	private String agencia;

	private String agenteFiananceiroNome;

	private Integer agenteFinanceiroId;

	private String atividadeCodigo;

	private Integer atividadeId;

	private String beneficiarioNome;

	private BigInteger custeioTotal;

	private Calendar inicio;

	private BigInteger investimentoTotal;

	private Integer linhaCreditoId;

	private String linhaCreditoNome;

	private String numeroCedula;

	private Integer projetoCreditoRuralId;

	private ProjetoCreditoRuralStatus status;

	public String getAgencia() {
		return agencia;
	}

	public String getAgenteFiananceiroNome() {
		return agenteFiananceiroNome;
	}

	public Integer getAgenteFinanceiroId() {
		return agenteFinanceiroId;
	}

	public String getAtividadeCodigo() {
		return atividadeCodigo;
	}

	public Integer getAtividadeId() {
		return atividadeId;
	}

	public String getBeneficiarioNome() {
		return beneficiarioNome;
	}

	public BigInteger getCusteioTotal() {
		return custeioTotal;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public BigInteger getInvestimentoTotal() {
		return investimentoTotal;
	}

	public Integer getLinhaCreditoId() {
		return linhaCreditoId;
	}

	public String getLinhaCreditoNome() {
		return linhaCreditoNome;
	}

	public String getNumeroCedula() {
		return numeroCedula;
	}

	public Integer getProjetoCreditoRuralId() {
		return projetoCreditoRuralId;
	}

	public ProjetoCreditoRuralStatus getStatus() {
		return status;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public void setAgenteFiananceiroNome(String agenteFiananceiroNome) {
		this.agenteFiananceiroNome = agenteFiananceiroNome;
	}

	public void setAgenteFinanceiroId(Integer agenteFinanceiroId) {
		this.agenteFinanceiroId = agenteFinanceiroId;
	}

	public void setAtividadeCodigo(String atividadeCodigo) {
		this.atividadeCodigo = atividadeCodigo;
	}

	public void setAtividadeId(Integer atividadeId) {
		this.atividadeId = atividadeId;
	}

	public void setBeneficiarioNome(String beneficiarioNome) {
		this.beneficiarioNome = beneficiarioNome;
	}

	public void setCusteioTotal(BigInteger custeioTotal) {
		this.custeioTotal = custeioTotal;
	}

	public void setInicio(Object inicio) {
		Calendar result = new GregorianCalendar();
		result.setTimeInMillis(((Timestamp) inicio).getTime());
		this.inicio = result;
	}

	public void setInvestimentoTotal(BigInteger investimentoTotal) {
		this.investimentoTotal = investimentoTotal;
	}

	public void setLinhaCreditoId(Integer linhaCreditoId) {
		this.linhaCreditoId = linhaCreditoId;
	}

	public void setLinhaCreditoNome(String linhaCreditoNome) {
		this.linhaCreditoNome = linhaCreditoNome;
	}

	public void setNumeroCedula(String numeroCedula) {
		this.numeroCedula = numeroCedula;
	}

	public void setProjetoCreditoRuralId(Integer projetoCreditoRuralId) {
		this.projetoCreditoRuralId = projetoCreditoRuralId;
	}

	public void setStatus(String status) {
		this.status = ProjetoCreditoRuralStatus.valueOf(status);
	}

}