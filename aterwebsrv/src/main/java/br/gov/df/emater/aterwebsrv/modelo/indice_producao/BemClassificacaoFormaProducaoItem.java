package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

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
@Table(name = "bem_classificacao_forma_producao_item", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class BemClassificacaoFormaProducaoItem extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<BemClassificacaoFormaProducaoItem> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "bem_classificacao_id")
	private BemClassificacao bemClassificacao;

	@ManyToOne
	@JoinColumn(name = "forma_producao_item_id")
	private FormaProducaoItem formaProducaoItem;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private Integer ordem;

	public BemClassificacaoFormaProducaoItem() {
		super();
	}

	public BemClassificacaoFormaProducaoItem(Integer id, BemClassificacao bemClassificacao, FormaProducaoItem formaProducaoItem, Integer ordem) {
		super();
		this.bemClassificacao = bemClassificacao;
		this.formaProducaoItem = formaProducaoItem;
		this.id = id;
		this.ordem = ordem;
	}

	public BemClassificacao getBemClassificacao() {
		return bemClassificacao;
	}

	public FormaProducaoItem getFormaProducaoItem() {
		return formaProducaoItem;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Integer getOrdem() {
		return ordem;
	}

	@Override
	public BemClassificacaoFormaProducaoItem infoBasica() {
		return new BemClassificacaoFormaProducaoItem(this.id, new BemClassificacao(this.bemClassificacao == null ? null : this.bemClassificacao.getId()), infoBasicaReg(this.formaProducaoItem), this.ordem);
	}

	public void setBemClassificacao(BemClassificacao bemClassificacao) {
		this.bemClassificacao = bemClassificacao;
	}

	public void setFormaProducaoItem(FormaProducaoItem formaProducaoItem) {
		this.formaProducaoItem = formaProducaoItem;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

}