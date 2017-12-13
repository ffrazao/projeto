package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=IpaProducaoForma.class)
//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonIdentityReference(alwaysAsId = true)
public class IpaForma {
	
	private Integer id;
	
	private String nome;
	
	private FormaProducaoValor formaProducaoValor;
	
	private FormaProducaoItem formaProducaoItem;
	
	private List<FormaProducaoItem> formaProducaoValorList;
	
	private List<FormaProducaoItem> producaoComposicaoList;

	

	public List<FormaProducaoItem> getProducaoComposicaoList() {
		return producaoComposicaoList;
	}

	public void setProducaoComposicaoList(List<FormaProducaoItem> producaoComposicaoList) {
		this.producaoComposicaoList = producaoComposicaoList;
	}

	public FormaProducaoValor getFormaProducaoValor() {
		return formaProducaoValor;
	}

	public void setFormaProducaoValor(FormaProducaoValor formaProducaoValor) {
		this.formaProducaoValor = formaProducaoValor;
	}

	public List<FormaProducaoItem> getFormaProducaoValorList() {
		return formaProducaoValorList;
	}

	public void setFormaProducaoValorList(List<FormaProducaoItem> formaProducaoValorList) {
		this.formaProducaoValorList = formaProducaoValorList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public FormaProducaoItem getFormaProducaoItem() {
		return formaProducaoItem;
	}

	public void setFormaProducaoItem(FormaProducaoItem formaProducaoItem) {
		this.formaProducaoItem = formaProducaoItem;
	}


}
