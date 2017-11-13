package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

@Entity
@Table(name = "bem_classificacao_forma_producao", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class BemClassificacaoFormaProducao extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<BemClassificacaoFormaProducao> {

	private static final long serialVersionUID = 1L;

	
	@ManyToOne
	@JoinColumn(name = "bem_classificacao_id")
	private BemClassificacao bemClassificacao;


	@OneToMany(mappedBy = "bemClassificacaoFormaProducao")
	private List<BemClassificacaoFormaProducaoBemClassificado> bemClassificacaoFormaProducaoBemClassificadoList;

	
	@ManyToOne
	@JoinColumn(name = "forma_producao_valor1_id")
	private FormaProducaoValor formaProducaoValor1;
	
	@ManyToOne
	@JoinColumn(name = "forma_producao_valor2_id")
	private FormaProducaoValor formaProducaoValor2;
	
	@ManyToOne
	@JoinColumn(name = "forma_producao_valor3_id")
	private FormaProducaoValor formaProducaoValor3;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	
	public BemClassificacaoFormaProducao() {
		super();
	}

	public BemClassificacaoFormaProducao(Integer id, BemClassificacao bemClassificacao,
			FormaProducaoValor formaProducaoValor1, FormaProducaoValor formaProducaoValor2,
			FormaProducaoValor formaProducaoValor3) {
		super(id);
		this.bemClassificacao = bemClassificacao;
		this.formaProducaoValor1 = formaProducaoValor1;
		this.formaProducaoValor2 = formaProducaoValor2;
		this.formaProducaoValor3 = formaProducaoValor3;
	}
	
	public BemClassificacaoFormaProducao(Serializable id) {
		super(id);
	}
	

	public BemClassificacao getBemClassificacao() {
		return bemClassificacao;
	}

	
	public List<BemClassificacaoFormaProducaoBemClassificado> getBemClassificacaoFormaProducaoBemClassificadoList() {
		return bemClassificacaoFormaProducaoBemClassificadoList;
	}

	public FormaProducaoValor getFormaProducaoValor1() {
		return formaProducaoValor1;
	}

	public FormaProducaoValor getFormaProducaoValor2() {
		return formaProducaoValor2;
	}

	public FormaProducaoValor getFormaProducaoValor3() {
		return formaProducaoValor3;
	}

	@Override
	public Integer getId() {
		return id;
	}
	@Override
	public BemClassificacaoFormaProducao infoBasica() {
		return new BemClassificacaoFormaProducao(this.id, new BemClassificacao(this.bemClassificacao == null ? null : this.bemClassificacao.getId()), infoBasicaReg(this.formaProducaoValor1), infoBasicaReg(this.formaProducaoValor2), infoBasicaReg(this.formaProducaoValor3)
				);
	}
	public void setBemClassificacao(BemClassificacao bemClassificacao) {
		this.bemClassificacao = bemClassificacao;
	}
	public void setBemClassificacaoFormaProducaoBemClassificadoList( List<BemClassificacaoFormaProducaoBemClassificado> bemClassificacaoFormaProducaoBemClassificadoList) {
		this.bemClassificacaoFormaProducaoBemClassificadoList = bemClassificacaoFormaProducaoBemClassificadoList;
	}
	
	public void setFormaProducaoValor1(FormaProducaoValor formaProducaoValor1) {
		this.formaProducaoValor1 = formaProducaoValor1;
	}

	public void setFormaProducaoValor2(FormaProducaoValor formaProducaoValor2) {
		this.formaProducaoValor2 = formaProducaoValor2;
	}
	public void setFormaProducaoValor3(FormaProducaoValor formaProducaoValor3) {
		this.formaProducaoValor3 = formaProducaoValor3;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
	
}