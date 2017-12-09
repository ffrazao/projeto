package br.gov.df.emater.aterwebsrv.dto.indice_producao;

import java.util.List;

import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Ipa;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoBemClassificado;

public class ProducaoGravaDto  {
	
	private Ipa ipa;
	
	private IpaProducao ipaProducao;
	
	private List<IpaProducaoBemClassificado> producaoAgricolaList;
	
	private List<IpaForma> producaoComposicaoList;

	private List<IpaProducao> producaoAnimalList;

	private List<IpaProducao> producaoFloriculturaList;
	
	private List<IpaProducao> producaoArtesanatoList;
	
	private List<IpaProducao> producaoAgroindustriaList;

	private List<IpaProducao> producaoIpaList;

	private List<IpaProducao> produtoList;

	private List<IpaProducao> ipaProducaoList;

	public List<IpaProducao> getIpaProducaoList() {
		return ipaProducaoList;
	}

	public void setIpaProducaoList(List<IpaProducao> ipaProducaoList) {
		this.ipaProducaoList = ipaProducaoList;
	}

	private List<Object> producaoNaoAgricolaList;

	public List<IpaProducaoBemClassificado> getProducaoAgricolaList() {
		return producaoAgricolaList;
	}

	public List<IpaProducao> getProducaoAnimalList() {
		return producaoAnimalList;
	}

	public List<IpaProducao> getProducaoFloriculturaList() {
		return producaoFloriculturaList;
	}

	public List<IpaProducao> getProdutoList() {
		return produtoList;
	}

	public List<Object> getProducaoNaoAgricolaList() {
		return producaoNaoAgricolaList;
	}

	public void setProducaoAgricolaList(List<IpaProducaoBemClassificado> producaoAgricolaList) {
		this.producaoAgricolaList = producaoAgricolaList;
	}
	public void setProducaoAnimalList(List<IpaProducao> producaoAnimalList) {
		this.producaoAnimalList = producaoAnimalList;
	}
	public void setProducaoFloriculturaList(List<IpaProducao> producaoFloriculturaList) {
		this.producaoFloriculturaList = producaoFloriculturaList;
	}

	public void setProdutoList(List<IpaProducao> produtoList) {
		this.produtoList = produtoList;
	}

	public void setProducaoNaoAgricolaList(List<Object> producaoNaoAgricolaList) {
		this.producaoNaoAgricolaList = producaoNaoAgricolaList;
	}
	
	public Ipa getIpa() {
		return ipa;
	}

	public void setIpa(Ipa ipa) {
		this.ipa = ipa;
	}

	public IpaProducao getIpaProducao() {
		return ipaProducao;
	}

	public void setIpaProducao(IpaProducao ipaProducao) {
		this.ipaProducao = ipaProducao;
	}

	public List<IpaForma> getProducaoComposicaoList() {
		return producaoComposicaoList;
	}

	public void setProducaoComposicaoList(List<IpaForma> producaoComposicaoList) {
		this.producaoComposicaoList = producaoComposicaoList;
	}

	public List<IpaProducao> getProducaoIpaList() {
		return producaoIpaList;
	}

	public void setProducaoIpaList(List<IpaProducao> producaoIpaList) {
		this.producaoIpaList = producaoIpaList;
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


	
	


}
