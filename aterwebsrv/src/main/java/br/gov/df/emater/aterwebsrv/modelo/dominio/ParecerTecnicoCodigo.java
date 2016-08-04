package br.gov.df.emater.aterwebsrv.modelo.dominio;

import java.math.BigDecimal;
import java.util.Calendar;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.FluxoCaixaAno;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralFluxoCaixa;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralParecerTecnico;

public enum ParecerTecnicoCodigo {

	CARACTERISTICA_INFRAESTRUTURA_IMOVEL("Características e Infra-Estrutura do Imóvel", 4, "Se custeio agrícola, informar o georeferenciamento da poligonal da área financiada", new Relatorio()), 
	CONCLUSAO_EMPREENDIMENTO_PROPOSTO("Conclusão do empreencimento proposto", 5, "Viabilidade técnica e econômica, oportunidade e parecer", new Relatorio()), 
	DIAGNOSTICO_SITUACAO_ATUAL("Diagnóstico da Situação Atual", 1, "Perfil administrativo, experiência na atividade, tecnologia utilizada, ...", new Relatorio()), 
	FLUXO_CAIXA("Fluxo Caixa", 6, "a ser definido", new ParecerTecnicoCodigoFluxoCaixa()), 
	INFORMACAO_COMERCIALIZACAO("Informações sobre a comercialização", 2, "Local, frequência, dificuldades, ...", new Relatorio()), 
	JUSTIFICATIVA_TECNICA("Justificativa Técnica", 3, "Tecnologia a ser implementada, utilização anual do bem financiado, carência, se inversões agrícolas informar se a cultura é convencional ou orgânica, irrigada ou sequeiro", new Relatorio());

	static class Relatorio {

		ProjetoCreditoRuralParecerTecnico gerar(ProjetoCreditoRural projetoCreditoRural, ProjetoCreditoRuralParecerTecnico modelo) throws BoException {
			return modelo;
		}

	}

	private String descricao;

	private String explicacao;

	private Integer ordem;

	private Relatorio relatorio;

	private ParecerTecnicoCodigo(String descricao, Integer ordem, String explicacao, Relatorio relatorio) {
		this.descricao = descricao;
		this.ordem = ordem;
		this.explicacao = explicacao;
		this.relatorio = relatorio;
	}

	public ProjetoCreditoRuralParecerTecnico gerar(ProjetoCreditoRural projetoCreditoRural) throws BoException {
		ProjetoCreditoRuralParecerTecnico result = null;

		// recuperar os dados caso existam
		if (projetoCreditoRural.getParecerTecnicoList() != null) {
			for (ProjetoCreditoRuralParecerTecnico anterior : projetoCreditoRural.getParecerTecnicoList()) {
				if (this.equals(anterior.getCodigo())) {
					result = anterior;
					break;
				}
			}
		}
		if (result == null) {
			result = new ProjetoCreditoRuralParecerTecnico();
			result.setCodigo(this);
			result.setDescricao(this.toString());
			result.setOrdem(this.getOrdem());
			result.setData(Calendar.getInstance());
		}
		result = this.relatorio.gerar(projetoCreditoRural, result);
		return result;
	}

	public String getExplicacao() {
		return explicacao;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}

class ParecerTecnicoCodigoFluxoCaixa extends ParecerTecnicoCodigo.Relatorio {

	ProjetoCreditoRuralParecerTecnico gerar(ProjetoCreditoRural projetoCreditoRural, ProjetoCreditoRuralParecerTecnico modelo) throws BoException {
		StringBuilder conteudo = new StringBuilder("<h4 style=\"color: green; background-color: yellow;\"><u><b>Parecer ainda não definido. Valores captados no fluxo de caixa</b></u></h4><table width=\"100%\">");
		conteudo.append("<tr><th>Item do Fluxo</th><th>Valor</th></tr>");
		for (ProjetoCreditoRuralFluxoCaixa fc : projetoCreditoRural.getFluxoCaixaList()) {
			BigDecimal total = new BigDecimal("0");
			for (FluxoCaixaAno fca : fc.getFluxoCaixaAnoList()) {
				total = total.add(fca.getValor());
			}
			conteudo.append("<tr><td>").append(fc.getDescricao()).append("</td><td>").append(total).append("</td></tr>");
		}
		conteudo.append("</table>");
		modelo.setConteudo(conteudo.toString());
		return modelo;
	}

}