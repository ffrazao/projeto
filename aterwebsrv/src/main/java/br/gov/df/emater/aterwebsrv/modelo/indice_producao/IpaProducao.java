package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;

@Entity
@Table(name = "ipa_producao", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class IpaProducao extends EntidadeBase{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "ipa_id")
	private Ipa ipa;
	
	private Integer rebanho;
	private Integer matriz;
	private Float area;
	
	@ManyToOne
	@JoinColumn(name = "cultura")
	private BemClassificacao cultura;
	
	@ManyToMany(mappedBy = "ipaProducao", cascade = CascadeType.ALL)
	private List<IpaProducaoForma> ipaProducaoFormaList;
	
	@OneToMany(mappedBy = "ipaProducao", cascade = CascadeType.ALL)
	private List<IpaProducaoBemClassificado> ipaProducaoBemClassificadoList;
	
	@Transient
	private List<IpaProducaoBemClassificado> ipaProducaoList;
	
	@Transient
	private Integer quantidadeProdutores;
	
	@Transient
	private List<IpaForma> producaoComposicaoList;
	@Transient
	private Float producao;
	@Transient
	private Float produtividade;
	@Transient
	private Float valorUnitario;
	@Transient
	private BemClassificado bemClassificado;
	@Transient
	private List<BemClassificado> bemClassificadoList;
	@Transient
	private BemClassificacao categoria;
	@Transient
	private BemClassificacao tipo;
	@Transient
	private List<IpaProducaoBemClassificado> produtoList;
	
	

	public BemClassificacao getCategoria() {
		return categoria;
	}

	public void setCategoria(BemClassificacao categoria) {
		this.categoria = categoria;
	}

	public BemClassificacao getTipo() {
		return tipo;
	}

	public void setTipo(BemClassificacao tipo) {
		this.tipo = tipo;
	}

	public List<IpaForma> getProducaoComposicaoList() {
		return producaoComposicaoList;
	}

	public void setProducaoComposicaoList(List<IpaForma> producaoComposicaoList) {
		this.producaoComposicaoList = producaoComposicaoList;
	}

	public IpaProducao() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Ipa getIpa() {
		return ipa;
	}

	public void setIpaId(Ipa ipa) {
		this.ipa = ipa;
	}

	public Integer getRebanho() {
		return rebanho;
	}

	public void setRebanho(Integer rebanho) {
		this.rebanho = rebanho;
	}

	public Integer getMatriz() {
		return matriz;
	}

	public void setMatriz(Integer matriz) {
		this.matriz = matriz;
	}

	public Float getArea() {
		return area;
	}

	public void setArea(Float area) {
		this.area = area;
	}

//	public List<IpaProducaoForma> getIpaProducaoFormaList() {
//		return ipaProducaoFormaList;
//	}
//
//	public void setIpaProducaoFormaList(List<IpaProducaoForma> ipaProducaoFormaList) {
//		this.ipaProducaoFormaList = ipaProducaoFormaList;
//	}
//
	public List<IpaProducaoBemClassificado> getIpaProducaoBemClassificadoList() {
		return ipaProducaoBemClassificadoList;
	}

	public void setIpaProducaoBemClassificadoList(List<IpaProducaoBemClassificado> ipaProducaoBemClassificadoList) {
		this.ipaProducaoBemClassificadoList = ipaProducaoBemClassificadoList;
	}

	public void setIpa(Ipa ipa) {
		this.ipa = ipa;
	}

	public List<IpaProducaoBemClassificado> getIpaProducaoList() {
		return ipaProducaoList;
	}

	public void setIpaProducaoList(List<IpaProducaoBemClassificado> ipaProducaoList) {
		this.ipaProducaoList = ipaProducaoList;
	}

	public List<IpaProducaoForma> getIpaProducaoFormaList() {
		return ipaProducaoFormaList;
	}

	public void setIpaProducaoFormaList(List<IpaProducaoForma> ipaProducaoFormaList) {
		this.ipaProducaoFormaList = ipaProducaoFormaList;
	}
	
	public Float getProducao() {
		return producao;
	}

	public void setProducao(Float producao) {
		this.producao = producao;
	}

	public Float getProdutividade() {
		return produtividade;
	}

	public void setProdutividade(Float produtividade) {
		this.produtividade = produtividade;
	}

	public Float getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(Float valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public BemClassificado getBemClassificado() {
		return bemClassificado;
	}

	public void setBemClassificado(BemClassificado bemClassificado) {
		this.bemClassificado = bemClassificado;
	}

	public BemClassificacao getCultura() {
		return cultura;
	}

	public void setCultura(BemClassificacao cultura) {
		this.cultura = cultura;
	}

	public List<BemClassificado> getBemClassificadoList() {
		return bemClassificadoList;
	}

	public void setBemClassificadoList(List<BemClassificado> bemClassificadoList) {
		this.bemClassificadoList = bemClassificadoList;
	}

	public List<IpaProducaoBemClassificado> getProdutoList() {
		return produtoList;
	}

	public void setProdutoList(List<IpaProducaoBemClassificado> produtoList) {
		this.produtoList = produtoList;
	}

	public Integer getQuantidadeProdutores() {
		return quantidadeProdutores;
	}

	public void setQuantidadeProdutores(Integer quantidadeProdutores) {
		this.quantidadeProdutores = quantidadeProdutores;
	}
	
}
