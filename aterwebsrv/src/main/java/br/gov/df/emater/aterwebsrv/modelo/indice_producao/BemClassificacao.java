package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaList;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import br.gov.df.emater.aterwebsrv.modelo.dominio.FormulaProduto;

@Entity
@Table(name = "bem_classificacao", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class BemClassificacao extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<BemClassificacao> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "bem_classificacao_id")
	private BemClassificacao bemClassificacao;

	@OneToMany(mappedBy = "bemClassificacao")
	private List<BemClassificacaoFormaProducaoItem> bemClassificacaoFormaProducaoItemList;

	@OneToMany(mappedBy = "bemClassificacao")
	private List<BemClassificacaoFormaProducaoValor> bemClassificacaoFormaProducaoValorList;

	@OneToMany(mappedBy = "bemClassificacao")
	private List<BemClassificacao> bemClassificacaoList;

	@OneToMany(mappedBy = "bemClassificacao")
	private List<BemClassificado> bemClassificadoList;

	@Enumerated(EnumType.STRING)
	private FormulaProduto formula;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "item_a_nome_id")
	private ItemNome itemANome;

	@ManyToOne
	@JoinColumn(name = "item_b_nome_id")
	private ItemNome itemBNome;

	@ManyToOne
	@JoinColumn(name = "item_c_nome_id")
	private ItemNome itemCNome;

	private String nome;

	@ManyToOne
	@JoinColumn(name = "unidade_medida_id")
	private UnidadeMedida unidadeMedida;

	public BemClassificacao() {
		super();
	}

	public BemClassificacao(Serializable id) {
		super(id);
	}

	public BemClassificacao(Serializable id, String nome, BemClassificacao bemClassificacao, List<BemClassificacao> bemClassificacaoList, List<BemClassificado> bemClassificadoList) {
		super(id);
		setNome(nome);
		setBemClassificacao(bemClassificacao);
		setBemClassificacaoList(bemClassificacaoList);
		setBemClassificadoList(bemClassificadoList);
	}

	public BemClassificacao getBemClassificacao() {
		return bemClassificacao;
	}

	public List<BemClassificacaoFormaProducaoItem> getBemClassificacaoFormaProducaoItemList() {
		return bemClassificacaoFormaProducaoItemList;
	}

	public List<BemClassificacaoFormaProducaoValor> getBemClassificacaoFormaProducaoValorList() {
		return bemClassificacaoFormaProducaoValorList;
	}

	public List<BemClassificacao> getBemClassificacaoList() {
		return bemClassificacaoList;
	}

	public List<BemClassificado> getBemClassificadoList() {
		return bemClassificadoList;
	}

	public FormulaProduto getFormula() {
		return formula;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public ItemNome getItemANome() {
		return itemANome;
	}

	public ItemNome getItemBNome() {
		return itemBNome;
	}

	public ItemNome getItemCNome() {
		return itemCNome;
	}

	public String getNome() {
		return nome;
	}

	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	@Override
	public BemClassificacao infoBasica() {
		return new BemClassificacao(getId(), getNome(), bemClassificacao == null ? null : new BemClassificacao(bemClassificacao.id), infoBasicaList(bemClassificacaoList), infoBasicaList(bemClassificadoList));

	}

	public void setBemClassificacao(BemClassificacao bemClassificacao) {
		this.bemClassificacao = bemClassificacao;
	}

	public void setBemClassificacaoFormaProducaoItemList(List<BemClassificacaoFormaProducaoItem> bemClassificacaoFormaProducaoItemList) {
		this.bemClassificacaoFormaProducaoItemList = bemClassificacaoFormaProducaoItemList;
	}

	public void setBemClassificacaoFormaProducaoValorList(List<BemClassificacaoFormaProducaoValor> bemClassificacaoFormaProducaoValorList) {
		this.bemClassificacaoFormaProducaoValorList = bemClassificacaoFormaProducaoValorList;
	}

	public void setBemClassificacaoList(List<BemClassificacao> bemClassificacaoList) {
		this.bemClassificacaoList = bemClassificacaoList;
	}

	public void setBemClassificadoList(List<BemClassificado> bemClassificadoList) {
		this.bemClassificadoList = bemClassificadoList;
	}

	public void setFormula(FormulaProduto formula) {
		this.formula = formula;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setItemANome(ItemNome itemANome) {
		this.itemANome = itemANome;
	}

	public void setItemBNome(ItemNome itemBNome) {
		this.itemBNome = itemBNome;
	}

	public void setItemCNome(ItemNome itemCNome) {
		this.itemCNome = itemCNome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

}