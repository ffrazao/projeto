package br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.Dto;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralFinanciamento;

public class SupervisaoCreditoRelDto implements Dto {
	
	private static final long serialVersionUID = 1L;
	
	private String proponenteNome;
	
	private String proponenteCpfCnpj;
	
	private List<String> propriedadeNomeList;
	
	private String numeroCedula;
	
	private String agenteFinanceiro;
	
	private String agencia;
	
	private String linhaCredito;
	
	private String unidadeOrganizacional;
	
	private String responsavelTecnicoNome;
	
	private Calendar elaboracao;
	
	private Calendar contratacao;
	
	private Calendar vencimento;
	
	private BigDecimal custeioOrcado;
	
	private BigDecimal custeioProprio;
	
	private BigDecimal custeioFinanciado;
	
	private BigDecimal investimentoOrcado;
	
	private BigDecimal investimentoProprio;
	
	private BigDecimal investimentoFinanciado;
	
	private String finalidade;
	
	private String status;
	
	private List<ProjetoCreditoRuralFinanciamento> custeioList;
	
	private List<ProjetoCreditoRuralFinanciamento> investimentoList;
	
	private Integer numeroSupervisao;
	
	private Calendar dataPrevista;
	
	private Calendar dataRealizacao;
	
	private String observacao;
	
	private String recomendacao;
	
	private String liberacao;
	
	private Calendar dataEmissao;

	public String getProponenteNome() {
		return proponenteNome;
	}

	public void setProponenteNome(String proponenteNome) {
		this.proponenteNome = proponenteNome;
	}

	public String getProponenteCpfCnpj() {
		return proponenteCpfCnpj;
	}

	public void setProponenteCpfCnpj(String proponenteCpfCnpj) {
		this.proponenteCpfCnpj = proponenteCpfCnpj;
	}

	public List<String> getPropriedadeNomeList() {
		return propriedadeNomeList;
	}

	public void setPropriedadeNomeList(List<String> propriedadeNomeList) {
		this.propriedadeNomeList = propriedadeNomeList;
	}

	public String getNumeroCedula() {
		return numeroCedula;
	}

	public void setNumeroCedula(String numeroCedula) {
		this.numeroCedula = numeroCedula;
	}

	public String getAgenteFinanceiro() {
		return agenteFinanceiro;
	}

	public void setAgenteFinanceiro(String agenteFinanceiro) {
		this.agenteFinanceiro = agenteFinanceiro;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getLinhaCredito() {
		return linhaCredito;
	}

	public void setLinhaCredito(String linhaCredito) {
		this.linhaCredito = linhaCredito;
	}

	public String getUnidadeOrganizacional() {
		return unidadeOrganizacional;
	}

	public void setUnidadeOrganizacional(String unidadeOrganizacional) {
		this.unidadeOrganizacional = unidadeOrganizacional;
	}

	public String getResponsavelTecnicoNome() {
		return responsavelTecnicoNome;
	}

	public void setResponsavelTecnicoNome(String responsavelTecnicoNome) {
		this.responsavelTecnicoNome = responsavelTecnicoNome;
	}

	public Calendar getElaboracao() {
		return elaboracao;
	}

	public void setElaboracao(Calendar elaboracao) {
		this.elaboracao = elaboracao;
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

	public BigDecimal getCusteioOrcado() {
		return custeioOrcado;
	}

	public void setCusteioOrcado(BigDecimal custeioOrcado) {
		this.custeioOrcado = custeioOrcado;
	}

	public BigDecimal getCusteioProprio() {
		return custeioProprio;
	}

	public void setCusteioProprio(BigDecimal custeioProprio) {
		this.custeioProprio = custeioProprio;
	}

	public BigDecimal getCusteioFinanciado() {
		return custeioFinanciado;
	}

	public void setCusteioFinanciado(BigDecimal custeioFinanciado) {
		this.custeioFinanciado = custeioFinanciado;
	}

	public BigDecimal getInvestimentoOrcado() {
		return investimentoOrcado;
	}

	public void setInvestimentoOrcado(BigDecimal investimentoOrcado) {
		this.investimentoOrcado = investimentoOrcado;
	}

	public BigDecimal getInvestimentoProprio() {
		return investimentoProprio;
	}

	public void setInvestimentoProprio(BigDecimal investimentoProprio) {
		this.investimentoProprio = investimentoProprio;
	}

	public BigDecimal getInvestimentoFinanciado() {
		return investimentoFinanciado;
	}

	public void setInvestimentoFinanciado(BigDecimal investimentoFinanciado) {
		this.investimentoFinanciado = investimentoFinanciado;
	}

	public String getFinalidade() {
		return finalidade;
	}

	public void setFinalidade(String finalidade) {
		this.finalidade = finalidade;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ProjetoCreditoRuralFinanciamento> getCusteioList() {
		return custeioList;
	}

	public void setCusteioList(List<ProjetoCreditoRuralFinanciamento> custeioList) {
		this.custeioList = custeioList;
	}

	public List<ProjetoCreditoRuralFinanciamento> getInvestimentoList() {
		return investimentoList;
	}

	public void setInvestimentoList(List<ProjetoCreditoRuralFinanciamento> investimentoList) {
		this.investimentoList = investimentoList;
	}

	public Integer getNumeroSupervisao() {
		return numeroSupervisao;
	}

	public void setNumeroSupervisao(Integer numeroSupervisao) {
		this.numeroSupervisao = numeroSupervisao;
	}

	public Calendar getDataPrevista() {
		return dataPrevista;
	}

	public void setDataPrevista(Calendar dataPrevista) {
		this.dataPrevista = dataPrevista;
	}

	public Calendar getDataRealizacao() {
		return dataRealizacao;
	}

	public void setDataRealizacao(Calendar dataRealizacao) {
		this.dataRealizacao = dataRealizacao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getRecomendacao() {
		return recomendacao;
	}

	public void setRecomendacao(String recomendacao) {
		this.recomendacao = recomendacao;
	}

	public String getLiberacao() {
		return liberacao;
	}

	public void setLiberacao(String liberacao) {
		this.liberacao = liberacao;
	}

	public Calendar getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Calendar dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

}
