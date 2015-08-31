package br.gov.df.emater.aterwebsrv.modelo.ipa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PerspectivaProducaoFloresProtecaoEpocaForma;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PerspectivaProducaoFloresTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PerspectivaProducaoFloresUsoDagua;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PerspectivaProducaoSistema;

/**
 * The persistent class for the perspectiva_producao_agricola database table.
 * 
 */
@Entity
@Table(name = "perspectiva_agricola", schema = EntidadeBase.IPA_SCHEMA)
@PrimaryKeyJoinColumn(name = "id")
public class PerspectivaProducaoAgricola extends PrevisaoProducao {

	private static final long serialVersionUID = 1L;

	@Column(name = "protecao_epoca_forma")
	@Enumerated(EnumType.STRING)
	private PerspectivaProducaoFloresProtecaoEpocaForma protecaoEpocaForma;

	@Column(name = "sistema")
	@Enumerated(EnumType.STRING)
	private PerspectivaProducaoSistema sistemaAgricola;

	@Column(name = "tipo")
	@Enumerated(EnumType.STRING)
	private PerspectivaProducaoFloresTipo tipo;

	@Column(name = "uso_dagua")
	@Enumerated(EnumType.STRING)
	private PerspectivaProducaoFloresUsoDagua usoDagua;

	public PerspectivaProducaoFloresProtecaoEpocaForma getProtecaoEpocaForma() {
		return protecaoEpocaForma;
	}

	public PerspectivaProducaoSistema getSistemaAgricola() {
		return sistemaAgricola;
	}

	public void setSistemaAgricola(PerspectivaProducaoSistema sistemaAgricola) {
		this.sistemaAgricola = sistemaAgricola;
	}

	public PerspectivaProducaoFloresTipo getTipo() {
		return tipo;
	}

	public PerspectivaProducaoFloresUsoDagua getUsoDagua() {
		return usoDagua;
	}

	public void setProtecaoEpocaForma(PerspectivaProducaoFloresProtecaoEpocaForma protecaoEpocaForma) {
		this.protecaoEpocaForma = protecaoEpocaForma;
	}

	public void setTipo(PerspectivaProducaoFloresTipo tipo) {
		this.tipo = tipo;
	}

	public void setUsoDagua(PerspectivaProducaoFloresUsoDagua usoDagua) {
		this.usoDagua = usoDagua;
	}

}