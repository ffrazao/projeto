package br.gov.df.emater.aterwebsrv.modelo.dominio;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioData;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Coleta;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersao;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.CronogramaPagamento;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.FluxoCaixaAno;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralCronogramaPagamento;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralFluxoCaixa;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralPublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralReceitaDespesa;

public enum FluxoCaixaCodigo {

	DESPESA_AMORTIZACAO_DIVIDAS_EXISTENTE("Amortização dívidas existentes", 8, new FluxoCaixaCodigoDespesaAmortizacaoDividasExistente(), FluxoCaixaTipo.D, Confirmacao.S), DESPESA_ATIVIDADE_AGROPECUARIA("Atividades agropecuárias", 3,
			new FluxoCaixaCodigoReceitaDespesaAtividadeAgropecuaria("Despesa"), FluxoCaixaTipo.D, Confirmacao.S), DESPESA_IMPOSTO_TAXA("Impostos e taxas", 6, new Calculo(), FluxoCaixaTipo.D, Confirmacao.N), DESPESA_MANUTENCAO_BENFEITORIA_MAQUINA(
					"Manutenção de Benfeitorias e Máquinas", 5, new FluxoCaixaCodigoDespesaManutencaoBenfeitoriaMaquina(), FluxoCaixaTipo.D, Confirmacao.S), DESPESA_MAO_DE_OBRA("Mão de obra", 4, new FluxoCaixaCodigoDespesaMaoDeObra(), FluxoCaixaTipo.D,
							Confirmacao.S), DESPESA_OUTRO("Outras despesas", 9, new Calculo(), FluxoCaixaTipo.D, Confirmacao.N), DESPESA_REMUNERACAO_PRODUTOR("Remuneração do Produtor", 7, new Calculo(), FluxoCaixaTipo.D, Confirmacao.N), RECEITA_ATIVIDADE_AGROPECUARIA(
									"Atividades agropecuárias", 1, new FluxoCaixaCodigoReceitaDespesaAtividadeAgropecuaria("Receita"), FluxoCaixaTipo.R, Confirmacao.S), RECEITA_OUTRO("Outras receitas", 2, new Calculo(), FluxoCaixaTipo.R, Confirmacao.N);

	static class Calculo {

		ObjectMapper mapper = new ObjectMapper();

		void atualizarValoresAno(ProjetoCreditoRuralFluxoCaixa modelo, BigDecimal valor) {
			for (FluxoCaixaAno fca : modelo.getFluxoCaixaAnoList()) {
				fca.setValor(valor);
			}
		}

		ProjetoCreditoRuralFluxoCaixa calcular(ProjetoCreditoRural projetoCreditoRural, ProjetoCreditoRuralFluxoCaixa modelo) throws BoException {
			return modelo;
		}

		@SuppressWarnings("unchecked")
		HashMap<String, Object> captarUltimaColetaFormulario(Object diagnosticoObj, String nomeCampo, Integer numeroVersao) throws BoException {
			if (diagnosticoObj == null || !(diagnosticoObj instanceof List)) {
				return null;
			}
			List<Object[]> diagnosticoList = (List<Object[]>) diagnosticoObj;
			if (CollectionUtils.isEmpty(diagnosticoList)) {
				return null;
			}

			for (Object[] formulario : diagnosticoList) {
				// encontrar o formulario
				if (nomeCampo.equals((String) formulario[2])) {
					// validar as versões
					if (formulario[8] != null) {
						for (FormularioVersao versao : (List<FormularioVersao>) formulario[8]) {
							if (numeroVersao.equals(versao.getVersao())) {
								Coleta ultimaColeta = null;
								for (Coleta coleta : versao.getColetaList()) {
									if (ultimaColeta == null || coleta.getDataColeta().after(ultimaColeta.getDataColeta())) {
										ultimaColeta = coleta;
									}
								}
								if (ultimaColeta == null) {
									return null;
								} else {
									if (Confirmacao.N.equals(ultimaColeta.getFinalizada())) {
										throw new BoException("Há coletas de formulários não finalizadas nos registros de Beneficiário ou de Propriedade Rural deste cálculo, verifique!");
									}
									if (StringUtils.isEmpty(ultimaColeta.getValorString())) {
										return null;
									}
									TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
									};
									HashMap<String, Object> valor = null;
									try {
										valor = mapper.readValue(ultimaColeta.getValorString(), typeRef);
									} catch (Exception e) {
										throw new BoException("Erro ao ler dados em formato JSON", e);
									}
									return valor;
								}
							}
						}
					}
				}
			}
			return null;
		}

		FluxoCaixaAno getFluxoCaixaAno(List<FluxoCaixaAno> origemList, Integer ano) {
			FluxoCaixaAno result = null;
			for (FluxoCaixaAno origem : origemList) {
				if (origem.getAno().equals(ano)) {
					result = origem;
					break;
				}
			}
			return result;
		}

	}

	public final static int MAX_ANOS = 10;

	private Calculo calculo;

	private String descricao;

	private Integer ordem;

	private Confirmacao somenteLeitura;

	private FluxoCaixaTipo tipo;

	private FluxoCaixaCodigo(String descricao, Integer ordem, Calculo calculo, FluxoCaixaTipo tipo, Confirmacao somenteLeitura) {
		this.descricao = descricao;
		this.ordem = ordem;
		this.calculo = calculo;
		this.somenteLeitura = somenteLeitura;
		this.tipo = tipo;
	}

	public ProjetoCreditoRuralFluxoCaixa calcular(ProjetoCreditoRural projetoCreditoRural) throws BoException {
		ProjetoCreditoRuralFluxoCaixa result = null;
		// recuperar os dados caso existam
		if (projetoCreditoRural.getFluxoCaixaList() != null) {
			for (ProjetoCreditoRuralFluxoCaixa anterior : projetoCreditoRural.getFluxoCaixaList()) {
				if (this.equals(anterior.getCodigo())) {
					result = anterior;
					break;
				}
			}
		}
		if (result == null) {
			result = new ProjetoCreditoRuralFluxoCaixa();
			result.setCodigo(this);
			result.setDescricao(this.toString());
			result.setOrdem(this.getOrdem());
			result.setTipo(this.getTipo());
			result.setFluxoCaixaAnoList(new ArrayList<>());
			for (int ano = 1; ano <= FluxoCaixaCodigo.MAX_ANOS; ano++) {
				FluxoCaixaAno fluxoCaixaAno = new FluxoCaixaAno();
				fluxoCaixaAno.setId(ano * (-1));
				fluxoCaixaAno.setAno(ano);
				fluxoCaixaAno.setValor(new BigDecimal("0"));
				result.getFluxoCaixaAnoList().add(fluxoCaixaAno);
			}
		}
		result = this.calculo.calcular(projetoCreditoRural, result);
		return result;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	public Confirmacao getSomenteLeitura() {
		return somenteLeitura;
	}

	public FluxoCaixaTipo getTipo() {
		return tipo;
	}

	@Override
	public String toString() {
		return this.descricao;
	}
}

class FluxoCaixaCodigoDespesaAmortizacaoDividasExistente extends FluxoCaixaCodigo.Calculo {

	@Override
	@SuppressWarnings("unchecked")
	public ProjetoCreditoRuralFluxoCaixa calcular(ProjetoCreditoRural projetoCreditoRural, ProjetoCreditoRuralFluxoCaixa modelo) throws BoException {
		// zerar os valores
		atualizarValoresAno(modelo, new BigDecimal("0"));

		HashMap<String, Object> form = captarUltimaColetaFormulario(projetoCreditoRural.getPublicoAlvo().getPessoa().getDiagnosticoList(), "patrimonioDivida", 1);
		if (form == null || form.get("dividaExistenteList") == null || !(form.get("dividaExistenteList") instanceof List)) {
			return modelo;
		}
		List<Map<String, Object>> lista = (List<Map<String, Object>>) form.get("dividaExistenteList");

		if (CollectionUtils.isEmpty(lista)) {
			return modelo;
		}
		// coletar as datas do financiamento
		List<Integer> reguaPrincipal = null;
		reguaPrincipal = calcularReguaFinanciamento(projetoCreditoRural.getCronogramaPagamentoInvestimentoList(), reguaPrincipal);
		reguaPrincipal = calcularReguaFinanciamento(projetoCreditoRural.getCronogramaPagamentoCusteioList(), reguaPrincipal);

		for (Map<String, Object> item : lista) {
			String dataStr[] = new String[2];
			Calendar data[] = new Calendar[2];
			dataStr[0] = (String) item.get("contratacao");
			dataStr[1] = (String) item.get("vencimento");
			try {
				data[0] = UtilitarioData.getInstance().ajustaInicioDia((Calendar) UtilitarioData.getInstance().stringParaData(dataStr[0]));
				data[1] = UtilitarioData.getInstance().ajustaInicioDia((Calendar) UtilitarioData.getInstance().stringParaData(dataStr[1]));
			} catch (ParseException e) {
				continue;
			}
			BigDecimal amortizacaoAnual = new BigDecimal((String) item.get("amortizacaoAnual"));

			List<Integer> reguaFinanciamento = criarReguaFinanciamento(data[0], data[1].get(Calendar.YEAR) - data[0].get(Calendar.YEAR));

			for (int i = 1; i <= reguaPrincipal.size(); i++) {
				for (int j = 1; j <= reguaFinanciamento.size(); j++) {
					if (reguaPrincipal.get(i) == reguaFinanciamento.get(j)) {
						acumulaValor(modelo, i, amortizacaoAnual);
						break;
					}
				}
			}
		}

		return modelo;
	}

	private void acumulaValor(ProjetoCreditoRuralFluxoCaixa modelo, Integer ano, BigDecimal valor) {
		for (FluxoCaixaAno fca : modelo.getFluxoCaixaAnoList()) {
			if (fca.getAno().equals(ano)) {
				fca.setValor(fca.getValor().add(valor));
				break;
			}
		}
	}

	private List<Integer> criarReguaFinanciamento(Calendar inicio, Integer totalAnos) {
		List<Integer> result = new ArrayList<>();
		for (int ano = inicio.get(Calendar.YEAR); ano <= inicio.get(Calendar.YEAR) + totalAnos; ano++) {
			result.add(ano);
		}
		return result;
	}

	private List<Integer> calcularReguaFinanciamento(List<ProjetoCreditoRuralCronogramaPagamento> cronogramaPagamentoInvestimentoList, List<Integer> anoFinanciamento) {
		if (!CollectionUtils.isEmpty(cronogramaPagamentoInvestimentoList)) {
			Calendar inicioFinanciamento = null;
			Integer totalAnos = null;
			for (ProjetoCreditoRuralCronogramaPagamento cronograma : cronogramaPagamentoInvestimentoList) {
				if (inicioFinanciamento == null || inicioFinanciamento.before(cronograma.getDataContratacao())) {
					inicioFinanciamento = new GregorianCalendar();
					inicioFinanciamento.setTime(cronograma.getDataContratacao().getTime());
				}
				Integer ano = null;
				for (CronogramaPagamento parcela : cronograma.getCronogramaPagamentoList()) {
					if (ano == null || parcela.getAno() > ano) {
						ano = parcela.getAno();
					}
				}
				if (totalAnos == null || ano > totalAnos) {
					totalAnos = ano;
				}
			}
			anoFinanciamento = criarReguaFinanciamento(inicioFinanciamento, totalAnos);
		}
		return anoFinanciamento;
	}

}

class FluxoCaixaCodigoDespesaManutencaoBenfeitoriaMaquina extends FluxoCaixaCodigo.Calculo {

	@Override
	public ProjetoCreditoRuralFluxoCaixa calcular(ProjetoCreditoRural projetoCreditoRural, ProjetoCreditoRuralFluxoCaixa modelo) throws BoException {
		// zerar os valores
		atualizarValoresAno(modelo, new BigDecimal("0"));

		// calcular máquinas e equipamentos
		modelo = calcularMaquinaEquipamento(projetoCreditoRural.getPublicoAlvo().getPessoa().getDiagnosticoList(), modelo);

		// calcular benfeitorias nas propriedades
		for (ProjetoCreditoRuralPublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural : projetoCreditoRural.getPublicoAlvoPropriedadeRuralList()) {
			modelo = calcularBenfeitoriaPropriedade(publicoAlvoPropriedadeRural.getPublicoAlvoPropriedadeRural().getPropriedadeRural().getDiagnosticoList(), modelo);
		}
		// atualiza todos os valores
		atualizarValoresAno(modelo, modelo.getFluxoCaixaAnoList().get(0).getValor());

		return modelo;
	}

	@SuppressWarnings("unchecked")
	private ProjetoCreditoRuralFluxoCaixa calcularBenfeitoriaPropriedade(Object diagnosticoObj, ProjetoCreditoRuralFluxoCaixa modelo) throws BoException {
		if (diagnosticoObj == null) {
			return modelo;
		}
		HashMap<String, Object> form = captarUltimaColetaFormulario(diagnosticoObj, "avaliacaoDaPropriedade", 1);
		if (form == null || form.get("benfeitoriaList") == null || !(form.get("benfeitoriaList") instanceof List)) {
			return modelo;
		}
		modelo.getFluxoCaixaAnoList().get(0).setValor(modelo.getFluxoCaixaAnoList().get(0).getValor().add(totalizar((List<Map<String, Object>>) form.get("benfeitoriaList")).multiply(new BigDecimal("0.02"))));

		return modelo;
	}

	@SuppressWarnings("unchecked")
	private ProjetoCreditoRuralFluxoCaixa calcularMaquinaEquipamento(Object diagnosticoObj, ProjetoCreditoRuralFluxoCaixa modelo) throws BoException {
		HashMap<String, Object> form = captarUltimaColetaFormulario(diagnosticoObj, "patrimonioDivida", 1);
		if (form == null || form.get("maquinaEquipamentoList") == null || !(form.get("maquinaEquipamentoList") instanceof List)) {
			return modelo;
		}
		modelo.getFluxoCaixaAnoList().get(0).setValor(modelo.getFluxoCaixaAnoList().get(0).getValor().add(totalizar((List<Map<String, Object>>) form.get("maquinaEquipamentoList")).multiply(new BigDecimal("0.03"))));
		return modelo;
	}

	private BigDecimal totalizar(List<Map<String, Object>> lista) {
		BigDecimal result = new BigDecimal("0");
		if (CollectionUtils.isEmpty(lista)) {
			return result;
		}
		for (Map<String, Object> item : lista) {
			result = result.add(new BigDecimal(item.get("quantidade").toString()).multiply(new BigDecimal(item.get("valorUnitario").toString())));
		}
		return result;
	}

}

class FluxoCaixaCodigoDespesaMaoDeObra extends FluxoCaixaCodigo.Calculo {

	@Override
	public ProjetoCreditoRuralFluxoCaixa calcular(ProjetoCreditoRural projetoCreditoRural, ProjetoCreditoRuralFluxoCaixa modelo) throws BoException {
		HashMap<String, Object> form = captarUltimaColetaFormulario(projetoCreditoRural.getPublicoAlvo().getPessoa().getDiagnosticoList(), "beneficioSocialForcaTrabalho", 1);
		if (form.get("trabalhadorPermanente") == null || form.get("salarioMensal") == null) {
			throw new BoException("Erro ao calcular mão de obra do Beneficiário, informações incompletas!");
		}
		atualizarValoresAno(modelo, new BigDecimal("13.3").multiply(new BigDecimal(form.get("trabalhadorPermanente").toString())).multiply(new BigDecimal(form.get("salarioMensal").toString())));

		return modelo;
	}

}

class FluxoCaixaCodigoReceitaDespesaAtividadeAgropecuaria extends FluxoCaixaCodigo.Calculo {

	private String nomeLista;

	FluxoCaixaCodigoReceitaDespesaAtividadeAgropecuaria(String nomeLista) {
		this.nomeLista = nomeLista;
	}

	@Override
	@SuppressWarnings("unchecked")
	public ProjetoCreditoRuralFluxoCaixa calcular(ProjetoCreditoRural projetoCreditoRural, ProjetoCreditoRuralFluxoCaixa modelo) throws BoException {
		List<ProjetoCreditoRuralReceitaDespesa> lista = null;
		try {
			lista = ((List<ProjetoCreditoRuralReceitaDespesa>) ProjetoCreditoRural.class.getMethod(String.format("get%sList", nomeLista)).invoke(projetoCreditoRural));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		if (lista == null) {
			throw new BoException("Objeto Inválido! Lista de %ss não informada!", this.nomeLista);
		}
		for (FluxoCaixaAno fluxoCaixaAno : modelo.getFluxoCaixaAnoList()) {
			fluxoCaixaAno.setValor(new BigDecimal("0"));
			// contabilizar todos os registros do mesmo ano
			for (ProjetoCreditoRuralReceitaDespesa r : lista) {
				if (fluxoCaixaAno.getAno().equals(r.getAno())) {
					fluxoCaixaAno.setValor(fluxoCaixaAno.getValor().add(r.getValorTotal()));
				}
			}
		}
		return modelo;
	}

}