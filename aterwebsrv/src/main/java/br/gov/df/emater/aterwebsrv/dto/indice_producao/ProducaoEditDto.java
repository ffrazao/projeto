package br.gov.df.emater.aterwebsrv.dto.indice_producao;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Ipa;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoBemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoForma;

public class ProducaoEditDto extends CadFiltroDtoCustom{

	private static final long serialVersionUID = 1L;
	//@JsonProperty("ipa")
	private Ipa ipa;
	//@JsonProperty("id")
	private Integer id;
	//@JsonProperty("area")
	private Float area;
	//@JsonProperty("rebanho")
	private Integer rebanho;
	//@JsonProperty("matriz")
	private Integer matriz;
	//@JsonProperty("ipaProducaoBemClassificadoList")
	private List<IpaProducaoBemClassificado> ipaProducaoBemClassificadoList;
	//@JsonProperty("ipaComposicaoList")
	private List<IpaForma> ipaComposicaoList;
	//@JsonProperty("producaoAgricolaList")
	private List<IpaProducao> producaoAgricolaList;

	public List<IpaProducao> getProducaoAgricolaList() {
		return producaoAgricolaList;
	}

	public void setProducaoAgricolaList(List<IpaProducao> producaoAgricolaList) {
		this.producaoAgricolaList = producaoAgricolaList;
	}

	public Ipa getIpa() {
		return ipa;
	}

	public void setIpa(Ipa ipa) {
		this.ipa = ipa;
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

	public List<IpaProducaoBemClassificado> getIpaProducaoBemClassificadoList() {
		return ipaProducaoBemClassificadoList;
	}

	public void setIpaProducaoBemClassificadoList(List<IpaProducaoBemClassificado> ipaProducaoBemClassificadoList) {
		this.ipaProducaoBemClassificadoList = ipaProducaoBemClassificadoList;
	}

	public List<IpaForma> getIpaComposicaoList() {
		return ipaComposicaoList;
	}

	public void setIpaComposicaoList(List<IpaForma> ipaComposicaoList) {
		this.ipaComposicaoList = ipaComposicaoList;
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
	
	

}
