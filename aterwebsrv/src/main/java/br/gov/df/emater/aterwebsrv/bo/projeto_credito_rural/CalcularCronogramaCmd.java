package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo._SalvarCmd;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioFinanceiro;
import br.gov.df.emater.aterwebsrv.modelo.dto.ProjetoCreditoRuralCronogramaDto;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralCronogramaPagamento;

@Service("ProjetoCreditoRuralCalcularCronogramaCmd")
public class CalcularCronogramaCmd extends _SalvarCmd {

	public CalcularCronogramaCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ProjetoCreditoRural result = (ProjetoCreditoRural) contexto.getRequisicao();

		ProjetoCreditoRuralCronogramaDto cronograma = result.getCronograma();

		// validar a quantidade máxima de parcelas
		if (cronograma.getPeriodicidade() == null) {
			throw new BoException("O campo [%s] não foi informado", "Periodicidade");
		}
		if (cronograma.getDataContratacao() == null) {
			throw new BoException("O campo [%s] não foi informado", "Data da Contratação");
		}
		if (cronograma.getValorFinanciamento() == null) {
			throw new BoException("O campo [%s] não foi informado", "Valor do Financiamento");
		}
		if (cronograma.getTaxaJurosAnual() == null) {
			throw new BoException("O campo [%s] não foi informado", "Juros Anual (%)");
		}
		if (cronograma.getQuantidadeParcelas() == null) {
			throw new BoException("O campo [%s] não foi informado", "Quantidade de Parcelas");
		}
		if (cronograma.getDataFinalCarencia() == null) {
			throw new BoException("O campo [%s] não foi informado", "Data Final de Carência");
		}
		if (cronograma.getQuantidadeParcelas() == null || cronograma.getQuantidadeParcelas() < 1) {
			throw new BoException("Quantidade mínima de parcelas ultrapassada [1]");
		}
		if (cronograma.getPeriodicidade().ultrapassaQuantidadeMaximaParcelas(cronograma.getQuantidadeParcelas())) {
			throw new BoException("Quantidade máxima de parcelas ultrapassada [%d]", cronograma.getPeriodicidade().getMaxParcelas());
		}
		if (cronograma.getDataFinalCarencia().before(cronograma.getDataContratacao())) {
			throw new BoException("A data final da carência deve ser superior ou igual a data de contratação");
		}

		// calcular a data da primeira parcela
		Calendar dataPrimeiraParcela = new GregorianCalendar();
		dataPrimeiraParcela.setTime(cronograma.getDataFinalCarencia().getTime());
		dataPrimeiraParcela.add(Calendar.DATE, cronograma.getPeriodicidade().getTotalDiasPeriodo());
		cronograma.setDataPrimeiraParcela(dataPrimeiraParcela);
		cronograma.setValorTotalJuros(new BigDecimal("0"));
		cronograma.setValorTotalParcelas(new BigDecimal("0"));

		List<ProjetoCreditoRuralCronogramaPagamento> cronogramaPagamentoList = new ArrayList<>();

		ProjetoCreditoRuralCronogramaPagamento cronogramaPagamento;

		// ajustar a taxa de juros conforme a periodicidade dos juros anuais escolhida
		BigDecimal taxaJurosAnualAjustada = cronograma.getTaxaJurosAnual().multiply(new BigDecimal(Integer.toString(cronograma.getPeriodicidade().getTotalMesesPeriodo())).divide(new BigDecimal("12"), 10, RoundingMode.HALF_UP));

		// inserir os juros da carência
		Integer id = 0;
		BigDecimal diasCarencia = new BigDecimal(Long.toString(TimeUnit.DAYS.convert(cronograma.getDataFinalCarencia().getTimeInMillis() - cronograma.getDataContratacao().getTimeInMillis(), TimeUnit.MILLISECONDS)));
		BigDecimal jurosCarencia = new BigDecimal("0");

		if (diasCarencia.compareTo(new BigDecimal("1")) >= 0) {
			jurosCarencia = cronograma.getValorFinanciamento().multiply(taxaJurosAnualAjustada.multiply(diasCarencia.divide(new BigDecimal("365"), 10, RoundingMode.HALF_UP)).divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP));

			cronogramaPagamento = new ProjetoCreditoRuralCronogramaPagamento();
			cronogramaPagamento.setId(--id);
			cronogramaPagamento.setTipo(cronograma.getTipo());
			cronogramaPagamento.setParcela(0);
			cronogramaPagamento.setSaldo(cronograma.getValorFinanciamento());
			cronogramaPagamento.setPrestacao(new BigDecimal("0"));
			cronogramaPagamento.setJuros(jurosCarencia);
			cronogramaPagamento.setPrincipal(new BigDecimal("0"));

			cronograma.setValorTotalJuros(cronograma.getValorTotalJuros().add(cronogramaPagamento.getJuros()));

			cronogramaPagamentoList.add(cronogramaPagamento);
		}

		// calcular as demais prestações
		List<Map<String, Object>> tabelaPriceList = UtilitarioFinanceiro.getInstance().tabelaPriceCalculaParcelas(cronograma.getValorFinanciamento().add(jurosCarencia), taxaJurosAnualAjustada, cronograma.getQuantidadeParcelas());
		int ano = 1, epoca = 0;
		for (int i = 0; i < tabelaPriceList.size(); i++) {
			Map<String, Object> tabelaPrice = tabelaPriceList.get(i);

			cronogramaPagamento = new ProjetoCreditoRuralCronogramaPagamento();
			cronogramaPagamento.setId(--id);
			if (i == 0) {
				cronogramaPagamento.setAno(ano);
			}
			if ((++epoca) <= cronograma.getPeriodicidade().getMaxEpoca()) {
				cronogramaPagamento.setEpoca(epoca);
			} else {
				epoca = 1;
				cronogramaPagamento.setAno(++ano);
				cronogramaPagamento.setEpoca(epoca);
			}
			cronogramaPagamento.setTipo(cronograma.getTipo());
			cronogramaPagamento.setParcela((Integer) tabelaPrice.get("parcela"));
			cronogramaPagamento.setSaldo((BigDecimal) tabelaPrice.get("saldoDevedorAnterior"));
			cronogramaPagamento.setPrestacao((BigDecimal) tabelaPrice.get("valorParcela"));
			cronogramaPagamento.setJuros((BigDecimal) tabelaPrice.get("juros"));
			cronogramaPagamento.setPrincipal((BigDecimal) tabelaPrice.get("amortizacao"));

			cronograma.setValorTotalJuros(cronograma.getValorTotalJuros().add(cronogramaPagamento.getJuros()));
			cronograma.setValorTotalParcelas(cronograma.getValorTotalParcelas().add(cronogramaPagamento.getPrestacao()));

			cronogramaPagamentoList.add(cronogramaPagamento);
		}

		switch (cronograma.getTipo()) {
		case I:
			result.setInvestimentoDataPrimeiraParcela(cronograma.getDataPrimeiraParcela());
			result.setInvestimentoValorTotalJuros(cronograma.getValorTotalJuros());
			result.setInvestimentoValorTotalParcelas(cronograma.getValorTotalParcelas());
			result.setCronogramaInvestimentoList(cronogramaPagamentoList);
			break;
		case C:
			result.setCusteioDataPrimeiraParcela(cronograma.getDataPrimeiraParcela());
			result.setCusteioValorTotalJuros(cronograma.getValorTotalJuros());
			result.setCusteioValorTotalParcelas(cronograma.getValorTotalParcelas());
			result.setCronogramaCusteioList(cronogramaPagamentoList);
			break;
		}

		contexto.setResposta(result);

		return false;
	}

	// public boolean executarVelho(_Contexto contexto) throws Exception {
	// ProjetoCreditoRural result = (ProjetoCreditoRural)
	// contexto.getRequisicao();
	//
	// ProjetoCreditoRuralCronogramaDto cronograma = result.getCronograma();
	//
	// // validar a quantidade máxima de parcelas
	// if (cronograma.getPeriodicidade() == null) {
	// throw new BoException("O campo [%s] não foi informado", "Periodicidade");
	// }
	// if (cronograma.getDataContratacao() == null) {
	// throw new BoException("O campo [%s] não foi informado", "Data da
	// Contratação");
	// }
	// if (cronograma.getValorFinanciamento() == null) {
	// throw new BoException("O campo [%s] não foi informado", "Valor do
	// Financiamento");
	// }
	// if (cronograma.getTaxaJurosAnual() == null) {
	// throw new BoException("O campo [%s] não foi informado", "Juros Anual
	// (%)");
	// }
	// if (cronograma.getQuantidadeParcelas() == null) {
	// throw new BoException("O campo [%s] não foi informado", "Quantidade de
	// Parcelas");
	// }
	// if (cronograma.getDataFinalCarencia() == null) {
	// throw new BoException("O campo [%s] não foi informado", "Data Final de
	// Carência");
	// }
	//
	// if (cronograma.getQuantidadeParcelas() == null ||
	// cronograma.getQuantidadeParcelas() < 1) {
	// throw new BoException("Quantidade mínima de parcelas ultrapassada [1]");
	// }
	//
	// if
	// (cronograma.getPeriodicidade().ultrapassaQuantidadeMaximaParcelas(cronograma.getQuantidadeParcelas()))
	// {
	// throw new BoException("Quantidade máxima de parcelas ultrapassada [%d]",
	// cronograma.getPeriodicidade().getMaxParcelas());
	// }
	//
	// if
	// (cronograma.getDataFinalCarencia().before(cronograma.getDataContratacao()))
	// {
	// throw new BoException("A data final da carência deve ser superior ou
	// igual a data de contratação");
	// }
	//
	// // calcular a data da primeira parcela
	// Calendar dataPrimeiraParcela = new GregorianCalendar();
	// dataPrimeiraParcela.setTime(cronograma.getDataFinalCarencia().getTime());
	// dataPrimeiraParcela.add(Calendar.DATE,
	// cronograma.getPeriodicidade().getTotalDiasPeriodo());
	// cronograma.setDataPrimeiraParcela(dataPrimeiraParcela);
	//
	// cronograma.setValorTotalJuros(new BigDecimal("0"));
	// cronograma.setValorTotalParcelas(new BigDecimal("0"));
	//
	// Long diasCarencia =
	// TimeUnit.DAYS.convert(cronograma.getDataFinalCarencia().getTimeInMillis()
	// - cronograma.getDataContratacao().getTimeInMillis(),
	// TimeUnit.MILLISECONDS);
	//
	// BigDecimal Fexp1 = new BigDecimal(diasCarencia.toString()).divide(new
	// BigDecimal("30").multiply(new
	// BigDecimal(cronograma.getPeriodicidade().getTotalMesesPeriodo().toString())),
	// 10, RoundingMode.HALF_UP);
	//
	// BigDecimal Fexp1_1 = cronograma.getTaxaJurosAnual().divide(new
	// BigDecimal("1200"), 10, RoundingMode.HALF_UP).multiply(new
	// BigDecimal(cronograma.getPeriodicidade().getTotalMesesPeriodo()));
	//
	// BigDecimal Fexp2 = new BigDecimal("1").add(Fexp1_1);
	//
	// BigDecimal Fexp3 = Fexp2.pow(Fexp1.intValue());
	//
	// BigDecimal Form1 = Fexp2.pow(cronograma.getQuantidadeParcelas());
	//
	// BigDecimal Form2 = Form1.multiply(Fexp1_1);
	//
	// BigDecimal Form3 = Form1.subtract(new BigDecimal("1"));
	//
	// BigDecimal Form4 = Form2.divide(Form3, 10, RoundingMode.HALF_UP);
	//
	// BigDecimal CoefJur = Form2.divide(Form4, 10, RoundingMode.HALF_UP);
	//
	// BigDecimal CDSCRED00FINCOEFJUR = Form2.divide(CoefJur, 10,
	// RoundingMode.HALF_UP);
	//
	// BigDecimal Juros = cronograma.getValorFinanciamento().multiply(Fexp1_1);
	// Juros =
	// Juros.add(Fexp3.multiply(cronograma.getValorFinanciamento()).subtract(cronograma.getValorFinanciamento()).multiply(new
	// BigDecimal("1").add(Fexp1_1)));
	//
	// BigDecimal Parcela =
	// cronograma.getValorFinanciamento().multiply(Fexp3).multiply(CDSCRED00FINCOEFJUR);
	//
	// BigDecimal saldo = cronograma.getValorFinanciamento();
	//
	// int Contador = 1;
	// int ContAno = 1;
	// int ContEpoca = 0;
	// List<ProjetoCreditoRuralCronogramaPagamento> cronogramaPagamentoList =
	// new ArrayList<>();
	// do {
	// ProjetoCreditoRuralCronogramaPagamento cronogramaPagamento = new
	// ProjetoCreditoRuralCronogramaPagamento();
	//
	// cronogramaPagamento.setId(Contador * -1);
	// cronogramaPagamento.setTipo(cronograma.getTipo());
	// cronogramaPagamento.setParcela(Contador);
	//
	// if (Contador == 1) {
	// cronogramaPagamento.setAno(ContAno);
	// }
	//
	// if ((++ContEpoca) <= cronograma.getPeriodicidade().getMaxEpoca()) {
	// cronogramaPagamento.setEpoca(ContEpoca);
	// } else {
	// ContEpoca = 1;
	// cronogramaPagamento.setAno(++ContAno);
	// cronogramaPagamento.setEpoca(ContEpoca);
	// }
	// cronogramaPagamento.setPrestacao(Parcela);
	// cronogramaPagamento.setSaldo(saldo);
	// cronogramaPagamento.setJuros(cronogramaPagamento.getSaldo().multiply(Fexp1_1));
	// cronogramaPagamento.setPrincipal(cronogramaPagamento.getPrestacao().subtract(cronogramaPagamento.getJuros()));
	//
	//
	// cronograma.setValorTotalJuros(cronograma.getValorTotalJuros().add(cronogramaPagamento.getJuros()));
	// cronograma.setValorTotalParcelas(cronograma.getValorTotalParcelas().add(cronogramaPagamento.getPrestacao()));
	//
	// saldo = saldo.subtract(cronogramaPagamento.getPrincipal());
	//
	// cronogramaPagamentoList.add(cronogramaPagamento);
	// } while (++Contador <= cronograma.getQuantidadeParcelas());
	//
	// // List<Map<String, Object>> tabelaPrice =
	// //
	// UtilitarioFinanceiro.getInstance().tabelaPriceCalculaParcelas(cronograma.getValorFinanciamento(),
	// // cronograma.getTaxaJurosAnual(), cronograma.getQuantidadeParcelas());
	//
	// switch (cronograma.getTipo()) {
	// case I:
	// result.setInvestimentoDataPrimeiraParcela(cronograma.getDataPrimeiraParcela());
	// result.setInvestimentoValorTotalJuros(cronograma.getValorTotalJuros());
	// result.setInvestimentoValorTotalParcelas(cronograma.getValorTotalParcelas());
	// result.setCronogramaInvestimentoList(cronogramaPagamentoList);
	// break;
	// case C:
	// result.setCusteioDataPrimeiraParcela(cronograma.getDataPrimeiraParcela());
	// result.setCusteioValorTotalJuros(cronograma.getValorTotalJuros());
	// result.setCusteioValorTotalParcelas(cronograma.getValorTotalParcelas());
	// result.setCronogramaCusteioList(cronogramaPagamentoList);
	// break;
	// }
	//
	// contexto.setResposta(result);
	//
	// return false;
	// }

}