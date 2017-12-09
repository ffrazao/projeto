package br.gov.df.emater.aterwebsrv.dto.indice_producao;

import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoValor;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Ipa;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoBemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoForma;

public class ProducaoCadDto extends CadFiltroDtoCustom {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private Float area;
	
	private Integer matriz;
	
	private Integer rebanho;
	
	private Float produtividade;
	
	private Float producao;
	
	private Integer quantidadeProdutores;
	
	private Float valorUnitario;
	
	private FormaProducaoValor forma;
	
	private BemClassificacao cultura;
	
	private BemClassificado bemClassificado;
	
	private BemClassificacao categoria;
	
	private BemClassificacao tipo;

	public BemClassificado getBemClassificado() {
		return bemClassificado;
	}

	public void setBemClassificado(BemClassificado bemClassificado) {
		this.bemClassificado = bemClassificado;
	}

	public Float getProducao() {
		return producao;
	}

	public void setProducao(Float producao) {
		this.producao = producao;
	}

	public Float getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(Float valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Float getProdutividade() {
		return produtividade;
	}

	public void setProdutividade(Float produtividade) {
		this.produtividade = produtividade;
	}

	private Ipa Ipa;

	private List<IpaProducao> producaoAgricolaList;

	private List<IpaProducao> producaoAnimalList;

	private List<IpaProducao> producaoFloriculturaList;
	
	private List<IpaProducao> producaoArtesanatoList;

	private List<IpaProducaoBemClassificado> produtoList;

	private List<IpaProducao> producaoNaoAgricolaList;
	
	private List<IpaProducaoForma> producaoComposicaoList;
	
	private List<IpaProducao> producaoAgroindustriaList;
	

	public List<IpaProducao> getProducaoAgricolaList() {
		return producaoAgricolaList;
	}

	public List<IpaProducao> getProducaoAnimalList() {
		return producaoAnimalList;
	}

	public List<IpaProducao> getProducaoFloriculturaList() {
		return producaoFloriculturaList;
	}

	public List<IpaProducaoBemClassificado> getProdutoList() {
		return produtoList;
	}

	public List<IpaProducao> getProducaoNaoAgricolaList() {
		return producaoNaoAgricolaList;
	}

	public void setProducaoAgricolaList(List<IpaProducao> producaoAgricolaList) {
		this.producaoAgricolaList = producaoAgricolaList;
	}
	public void setProducaoAnimalList(List<IpaProducao> producaoAnimalList) {
		this.producaoAnimalList = producaoAnimalList;
	}
	public void setProducaoFloriculturaList(List<IpaProducao> producaoFloriculturaList) {
		this.producaoFloriculturaList = producaoFloriculturaList;
	}

	public void setProdutoList(List<IpaProducaoBemClassificado> produtoList) {
		this.produtoList = produtoList;
	}

	public void setProducaoNaoAgricolaList(List<IpaProducao> producaoNaoAgricolaList) {
		this.producaoNaoAgricolaList = producaoNaoAgricolaList;
	}

	public Ipa getIpa() {
		return Ipa;
	}

	public void setIpa(Ipa ipa) {
		Ipa = ipa;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getArea() {
		return area;
	}

	public void setArea(Float area) {
		this.area = area;
	}

	public Integer getMatriz() {
		return matriz;
	}

	public void setMatriz(Integer matriz) {
		this.matriz = matriz;
	}

	public Integer getRebanho() {
		return rebanho;
	}

	public void setRebanho(Integer rebanho) {
		this.rebanho = rebanho;
	}

	public List<IpaProducaoForma> getProducaoComposicaoList() {
		return producaoComposicaoList;
	}

	public void setProducaoComposicaoList(List<IpaProducaoForma> producaoComposicaoList) {
		this.producaoComposicaoList = producaoComposicaoList;
	}

	public List<IpaProducao> getProducaoArtesanatoList() {
		return producaoArtesanatoList;
	}

	public void setProducaoArtesanatoList(List<IpaProducao> producaoArtesanatoList) {
		this.producaoArtesanatoList = producaoArtesanatoList;
	}

	public List<IpaProducao> getProducaoAgroindustriaList() {
		return producaoAgroindustriaList;
	}

	public void setProducaoAgroindustriaList(List<IpaProducao> producaoAgroindustriaList) {
		this.producaoAgroindustriaList = producaoAgroindustriaList;
	}

	public BemClassificacao getCultura() {
		return cultura;
	}

	public void setCultura(BemClassificacao cultura) {
		this.cultura = cultura;
	}

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

	public FormaProducaoValor getForma() {
		return forma;
	}

	public void setForma(FormaProducaoValor forma) {
		this.forma = forma;
	}

	public Integer getQuantidadeProdutores() {
		return quantidadeProdutores;
	}

	public void setQuantidadeProdutores(Integer quantidadeProdutores) {
		this.quantidadeProdutores = quantidadeProdutores;
	}
	
	

}
