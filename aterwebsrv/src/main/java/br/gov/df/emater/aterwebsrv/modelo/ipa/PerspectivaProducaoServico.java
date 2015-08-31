package br.gov.df.emater.aterwebsrv.modelo.ipa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PerspectivaProducaoServicoCondicao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PerspectivaProducaoServicoProjeto;

/**
 * The persistent class for the perspectiva_producao_agricola database table.
 * 
 */
@Entity
@Table(name = "perspectiva_servico", schema = EntidadeBase.IPA_SCHEMA)
@PrimaryKeyJoinColumn(name = "id")
public class PerspectivaProducaoServico extends PrevisaoProducao {

	private static final long serialVersionUID = 1L;

	@Column(name = "condicao")
	@Enumerated(EnumType.STRING)
	private PerspectivaProducaoServicoCondicao condicao;

	@Column(name = "projeto")
	@Enumerated(EnumType.STRING)
	private PerspectivaProducaoServicoProjeto projeto;

	public PerspectivaProducaoServicoCondicao getCondicao() {
		return condicao;
	}

	public PerspectivaProducaoServicoProjeto getProjeto() {
		return projeto;
	}

	public void setCondicao(PerspectivaProducaoServicoCondicao condicao) {
		this.condicao = condicao;
	}

	public void setProjeto(PerspectivaProducaoServicoProjeto projeto) {
		this.projeto = projeto;
	}

}