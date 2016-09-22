package br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural;

import br.gov.df.emater.aterwebsrv.dto.Dto;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralArquivo;

public class ProjetoCreditoRuralReceitaDespesaApoioDto implements Dto {

	private static final long serialVersionUID = 1L;

	private Arquivo arquivo;

	private ProjetoCreditoRuralArquivo.Codigo codigo;

	private ProjetoCreditoRural projetoCreditoRural;

	public Arquivo getArquivo() {
		return arquivo;
	}

	public ProjetoCreditoRuralArquivo.Codigo getCodigo() {
		return codigo;
	}

	public ProjetoCreditoRural getProjetoCreditoRural() {
		return projetoCreditoRural;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	public void setCodigo(ProjetoCreditoRuralArquivo.Codigo codigo) {
		this.codigo = codigo;
	}

	public void setProjetoCreditoRural(ProjetoCreditoRural projetoCreditoRural) {
		this.projetoCreditoRural = projetoCreditoRural;
	}

}