package br.gov.df.emater.aterwebsrv.modelo.dominio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Coleta;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersao;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.FluxoCaixaAno;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
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

		ProjetoCreditoRuralFluxoCaixa calcular(ProjetoCreditoRural projetoCreditoRural, ProjetoCreditoRuralFluxoCaixa modelo) throws BoException {
			return modelo;
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
		// tentar recuperar o ID
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
}

class FluxoCaixaCodigoDespesaManutencaoBenfeitoriaMaquina extends FluxoCaixaCodigo.Calculo {

	private ObjectMapper mapper = new ObjectMapper();

	@SuppressWarnings("unchecked")
	private ProjetoCreditoRuralFluxoCaixa calcularBenfeitoriaPropriedade(Object diagnosticoObj, ProjetoCreditoRuralFluxoCaixa modelo) throws BoException {
		if (diagnosticoObj == null || !(diagnosticoObj instanceof List)) {
			return modelo;
		}

		List<Object[]> diagnosticoList = (List<Object[]>) diagnosticoObj;
		if (CollectionUtils.isEmpty(diagnosticoList)) {
			return modelo;
		}
		for (Object[] formulario : diagnosticoList) {
			// encontrar o formulario
			if ("avaliacaoDaPropriedade".equals((String) formulario[2])) {
				// validar as versões
				if (formulario[8] != null) {
					for (FormularioVersao versao : (List<FormularioVersao>) formulario[8]) {
						if (Integer.valueOf(1).equals(versao.getVersao())) {
							Coleta ultimaColeta = null;
							for (Coleta coleta : versao.getColetaList()) {
								if (ultimaColeta == null || coleta.getDataColeta().after(ultimaColeta.getDataColeta())) {
									ultimaColeta = coleta;
								}
							}
							if (ultimaColeta == null) {
								return modelo;
							} else {
								if (Confirmacao.N.equals(ultimaColeta.getFinalizada())) {
									throw new BoException("Erro ao calcular despesas manutenção benfeitorias máquinas da propriedade rural, a última coleta ainda não foi finalizada!");
								}
								TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
								};
								HashMap<String, Object> valor = null;
								try {
									valor = mapper.readValue(ultimaColeta.getValorString(), typeRef);
								} catch (Exception e) {
									throw new BoException("Erro ao calcular despesas manutenção benfeitorias máquinas da propriedade rural", e);
								}
								if (valor.get("benfeitoriaList") == null) {
									return modelo;
								}
								try {
									valor = mapper.readValue((String) valor.get("benfeitoriaList"), typeRef);
								} catch (Exception e) {
									throw new BoException("Erro ao calcular despesas manutenção benfeitorias máquinas da propriedade rural", e);
								}
								
								BigDecimal trabalhadorPermanente = new BigDecimal(valor.get("trabalhadorPermanente").toString());
								BigDecimal salarioMensal = new BigDecimal(valor.get("salarioMensal").toString());
								BigDecimal total = new BigDecimal("13.3").multiply(trabalhadorPermanente).multiply(salarioMensal);
								modelo.getFluxoCaixaAnoList().get(0).setValor(modelo.getFluxoCaixaAnoList().get(0).getValor().add(total));
							}
						}
					}
				}
			}
		}
		return modelo;
	}
	
	@SuppressWarnings("unchecked")
	private ProjetoCreditoRuralFluxoCaixa calcularMaquinaEquipamento(Object diagnosticoObj, ProjetoCreditoRuralFluxoCaixa modelo) throws BoException {
		if (diagnosticoObj == null || !(diagnosticoObj instanceof List)) {
			return modelo;
		}

		List<Object[]> diagnosticoList = (List<Object[]>) diagnosticoObj;
		if (CollectionUtils.isEmpty(diagnosticoList)) {
			return modelo;
		}
		for (Object[] formulario : diagnosticoList) {
			// encontrar o formulario
			if ("patrimonioDivida".equals((String) formulario[2])) {
				// validar as versões
				if (formulario[8] != null) {
					for (FormularioVersao versao : (List<FormularioVersao>) formulario[8]) {
						if (Integer.valueOf(1).equals(versao.getVersao())) {
							Coleta ultimaColeta = null;
							for (Coleta coleta : versao.getColetaList()) {
								if (ultimaColeta == null || coleta.getDataColeta().after(ultimaColeta.getDataColeta())) {
									ultimaColeta = coleta;
								}
							}
							if (ultimaColeta == null) {
								return modelo;
							} else {
								if (Confirmacao.N.equals(ultimaColeta.getFinalizada())) {
									throw new BoException("Erro ao calcular patrimônio do Beneficiário, a última coleta ainda não foi finalizada!");
								}
								TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
								};
								HashMap<String, Object> valor = null;
								try {
									valor = mapper.readValue(ultimaColeta.getValorString(), typeRef);
								} catch (Exception e) {
									throw new BoException("Erro ao calcular patrimônio do Beneficiário", e);
								}
								if (valor.get("maquinaEquipamentoList") == null || !(valor.get("maquinaEquipamentoList") instanceof List)) {
									return modelo;
								}
								List<Map<String, Object>> maquinaEquipamentoList = (List<Map<String, Object>>) valor.get("maquinaEquipamentoList");
								if (CollectionUtils.isEmpty(maquinaEquipamentoList)) {
									return modelo;
								}
								BigDecimal total = new BigDecimal("0");
								for (Map<String,Object> maquinaEquipamento: maquinaEquipamentoList) {
									BigDecimal quantidade = new BigDecimal(maquinaEquipamento.get("quantidade").toString());
									BigDecimal valorUnitario = new BigDecimal(maquinaEquipamento.get("valorUnitario").toString());

									total = total.add(quantidade.multiply(valorUnitario));
								}
								modelo.getFluxoCaixaAnoList().get(0).setValor(total.multiply(new BigDecimal("0.03")));
							}
						}
					}
				}
			}
		}
		return modelo;
	}

	
	@Override
	public ProjetoCreditoRuralFluxoCaixa calcular(ProjetoCreditoRural projetoCreditoRural, ProjetoCreditoRuralFluxoCaixa modelo) throws BoException {
		// zerar os valores
		for (FluxoCaixaAno fca : modelo.getFluxoCaixaAnoList()) {
			fca.setValor(new BigDecimal("0"));
		}
		
		// calcular máquinas e equipamentos
		modelo = calcularMaquinaEquipamento(projetoCreditoRural.getPublicoAlvo().getPessoa().getDiagnosticoList(), modelo);

		// calcular benfeitorias nas propriedades
		for (ProjetoCreditoRuralPublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural: projetoCreditoRural.getPublicoAlvoPropriedadeRuralList()) {
			if (publicoAlvoPropriedadeRural.getPublicoAlvoPropriedadeRural().getPropriedadeRural().getDiagnosticoList() != null) {
				modelo = calcularBenfeitoriaPropriedade(publicoAlvoPropriedadeRural.getPublicoAlvoPropriedadeRural().getPropriedadeRural().getDiagnosticoList(), modelo);
			}
		}
		for (FluxoCaixaAno fca : modelo.getFluxoCaixaAnoList()) {
			fca.setValor(modelo.getFluxoCaixaAnoList().get(0).getValor());
		}
		return modelo;
	}

}

class FluxoCaixaCodigoDespesaMaoDeObra extends FluxoCaixaCodigo.Calculo {

	private ObjectMapper mapper = new ObjectMapper();

	@Override
	@SuppressWarnings("unchecked")
	public ProjetoCreditoRuralFluxoCaixa calcular(ProjetoCreditoRural projetoCreditoRural, ProjetoCreditoRuralFluxoCaixa modelo) throws BoException {
		Object diagnosticoObj = projetoCreditoRural.getPublicoAlvo().getPessoa().getDiagnosticoList();
		if (1==1 || diagnosticoObj == null || !(diagnosticoObj instanceof List)) {
			return modelo;
		}

		List<Object[]> diagnosticoList = (List<Object[]>) diagnosticoObj;
		if (CollectionUtils.isEmpty(diagnosticoList)) {
			return modelo;
		}
		for (Object[] formulario : diagnosticoList) {
			// encontrar o formulario
			if ("beneficioSocialForcaTrabalho".equals((String) formulario[2])) {
				// validar as versões
				if (formulario[8] != null) {
					for (FormularioVersao versao : (List<FormularioVersao>) formulario[8]) {
						if (Integer.valueOf(1).equals(versao.getVersao())) {
							Coleta ultimaColeta = null;
							for (Coleta coleta : versao.getColetaList()) {
								if (ultimaColeta == null || coleta.getDataColeta().after(ultimaColeta.getDataColeta())) {
									ultimaColeta = coleta;
								}
							}
							if (ultimaColeta == null) {
								return modelo;
							} else {
								if (Confirmacao.N.equals(ultimaColeta.getFinalizada())) {
									throw new BoException("Erro ao calcular mão de obra do Beneficiário, a última coleta ainda não foi finalizada!");
								}
								TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
								};
								HashMap<String, Object> valor = null;
								try {
									valor = mapper.readValue(ultimaColeta.getValorString(), typeRef);
								} catch (Exception e) {
									throw new BoException("Erro ao calcular mão de obra do Beneficiário", e);
								}
								if (valor.get("trabalhadorPermanente") == null || valor.get("salarioMensal") == null) {
									throw new BoException("Erro ao calcular mão de obra do Beneficiário, informações incompletas!");
								}
								BigDecimal trabalhadorPermanente = new BigDecimal(valor.get("trabalhadorPermanente").toString());
								BigDecimal salarioMensal = new BigDecimal(valor.get("salarioMensal").toString());
								BigDecimal total = new BigDecimal("13.3").multiply(trabalhadorPermanente).multiply(salarioMensal);
								for (FluxoCaixaAno fca : modelo.getFluxoCaixaAnoList()) {
									fca.setValor(total);
								}
							}
						}
					}
				}
			}
		}
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
		for (int ano = 1; ano <= FluxoCaixaCodigo.MAX_ANOS; ano++) {
			FluxoCaixaAno fluxoCaixaAno = getFluxoCaixaAno(modelo.getFluxoCaixaAnoList(), ano);
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