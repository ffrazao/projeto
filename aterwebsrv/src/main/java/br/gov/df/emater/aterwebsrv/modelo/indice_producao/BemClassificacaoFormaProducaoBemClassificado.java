package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

@Entity
@Table(name = "bem_classificacao_forma_producao_bem_classificado", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class BemClassificacaoFormaProducaoBemClassificado extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<BemClassificacaoFormaProducaoBemClassificado> {
	

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "bem_classificado_id")
	private BemClassificado bemClassificado;
	
	@ManyToOne          
	@JoinColumn(name = "bem_classificacao_forma_producao_id")
	private BemClassificacaoFormaProducao bemClassificacaoFormaProducao;
	
	private String formula;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Override
	public BemClassificacaoFormaProducaoBemClassificado infoBasica() {
		return new BemClassificacaoFormaProducaoBemClassificado(this.id, new BemClassificacaoFormaProducao(this.bemClassificacaoFormaProducao == null ? null : this.bemClassificacaoFormaProducao.getId()), infoBasicaReg(this.bemClassificado), this.formula);
	}
	

	
	
	public BemClassificacaoFormaProducaoBemClassificado() {
		super();
	}
	
	public BemClassificacaoFormaProducaoBemClassificado(Serializable id) {
		super(id);
	}
	
	
	public BemClassificacaoFormaProducaoBemClassificado(Integer id, BemClassificacaoFormaProducao bemClassificacaoFormaProducao,
			BemClassificado bemClassificado, String formula) {
		super(id);
		this.bemClassificacaoFormaProducao = bemClassificacaoFormaProducao;
		this.bemClassificado = bemClassificado;
		this.formula = formula;
	}

	public BemClassificado getBemClassificado() {
		return bemClassificado;
	}

	public BemClassificacaoFormaProducao getbemClassificacaoFormaProducao() {
		return bemClassificacaoFormaProducao;
	}

	public String getFormula() {
		return formula;
	}

	public Integer getId() {
		return id;
	}

	public void setBemClassificacao(BemClassificado bemClassificado) {
		this.bemClassificado = bemClassificado;
	}

	public void setBemClassificacaoFormaProducao(BemClassificacaoFormaProducao bemClassificacaoFormaProducao) {
		this.bemClassificacaoFormaProducao = bemClassificacaoFormaProducao;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public void setId(Integer id) {
		this.id = id;
	}


	
	
	
	
	
	
	

}
