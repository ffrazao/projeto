package br.gov.df.emater.aterwebsrv.modelo.dominio;

import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioFinanceiro;

public enum ProjetoCreditoRuralPeriodicidade {

	A("Anual", 4, 12, 365, UtilitarioFinanceiro.Periodicidade.AO_ANO), M("Mensal", 1, 1, 30, UtilitarioFinanceiro.Periodicidade.AO_MES), S("Semestral", 3, 6, 182, UtilitarioFinanceiro.Periodicidade.AO_SEMESTRE), T("Trimestral", 2, 3, 91,
			UtilitarioFinanceiro.Periodicidade.AO_TRIMESTRE);

	public final static int _QUANTIDADE_MAXIMA_PARCELA_EM_MESES = 120;

	private String descricao;

	private Integer maxEpoca;

	private Integer maxParcelas;

	private Integer ordem;

	private Integer totalDiasPeriodo;

	private Integer totalMesesPeriodo;

	private UtilitarioFinanceiro.Periodicidade utilitarioFinanceiroPeriodicidade;

	private ProjetoCreditoRuralPeriodicidade(String descricao, Integer ordem, Integer totalMesesPeriodo, Integer totalDiasPeriodo, UtilitarioFinanceiro.Periodicidade utilitarioFinanceiroPeriodicidade) {
		this.descricao = descricao;
		this.ordem = ordem;
		this.totalMesesPeriodo = totalMesesPeriodo;
		this.totalDiasPeriodo = totalDiasPeriodo;
		this.maxParcelas = _QUANTIDADE_MAXIMA_PARCELA_EM_MESES / this.totalMesesPeriodo;
		this.maxEpoca = 12 / this.totalMesesPeriodo;

		this.utilitarioFinanceiroPeriodicidade = utilitarioFinanceiroPeriodicidade;
	}

	public Integer getMaxEpoca() {
		return maxEpoca;
	}

	public Integer getMaxParcelas() {
		return maxParcelas;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	public Integer getTotalDiasPeriodo() {
		return totalDiasPeriodo;
	}

	public Integer getTotalMesesPeriodo() {
		return totalMesesPeriodo;
	}

	public UtilitarioFinanceiro.Periodicidade getUtilitarioFinanceiroPeriodicidade() {
		return utilitarioFinanceiroPeriodicidade;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

	public boolean ultrapassaQuantidadeMaximaParcelas(int totalParcelas) {
		return totalParcelas > this.maxParcelas;
	}

}
