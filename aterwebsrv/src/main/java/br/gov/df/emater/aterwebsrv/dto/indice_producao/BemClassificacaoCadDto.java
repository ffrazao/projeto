package br.gov.df.emater.aterwebsrv.dto.indice_producao;

import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificado;

public class BemClassificacaoCadDto extends CadFiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private List<BemClassificacao> bemClassificacaoList;

	private List<BemClassificacao> bemClassificacaoMatrizList;

	private List<BemClassificado> bemClassificadoAgricolaList;

	private List<BemClassificado> bemClassificadoAgroindustriaList;
	
	private List<BemClassificado> bemClassificadoArtesanatoList;
	
	private List<BemClassificado> bemClassificadoAnimalList;
	
	private List<BemClassificado> bemClassificadoFloricuturaList;
	
	public List<BemClassificacao> getBemClassificacaoList() {
		return bemClassificacaoList;
	}

	public List<BemClassificacao> getBemClassificacaoMatrizList() {
		return bemClassificacaoMatrizList;
	}

	

	public void setBemClassificacaoList(List<BemClassificacao> bemClassificacaoList) {
		this.bemClassificacaoList = bemClassificacaoList;
	}

	public void setBemClassificacaoMatrizList(List<BemClassificacao> bemClassificacaoMatrizList) {
		this.bemClassificacaoMatrizList = bemClassificacaoMatrizList;
	}


	public List<BemClassificado> getBemClassificadoAgricolaList() {
		return bemClassificadoAgricolaList;
	}

	public void setBemClassificadoAgricolaList(List<BemClassificado> bemClassificadoAgricolaList) {
		this.bemClassificadoAgricolaList = bemClassificadoAgricolaList;
	}

	public List<BemClassificado> getBemClassificadoAnimalList() {
		return bemClassificadoAnimalList;
	}

	
	
	public void setBemClassificadoAnimalList(List<BemClassificado> bemClassificadoAnimalList) {
		this.bemClassificadoAnimalList = bemClassificadoAnimalList;
	}

	public List<BemClassificado> getBemClassificadoFloricuturaList() {
		return bemClassificadoFloricuturaList;
	}

	public void setBemClassificadoFloricuturaList(List<BemClassificado> bemClassificadoFloricuturaList) {
		this.bemClassificadoFloricuturaList = bemClassificadoFloricuturaList;
	}


	public List<BemClassificado> getBemClassificadoArtesanatoList() {
		return bemClassificadoArtesanatoList;
	}

	public void setBemClassificadoArtesanatoList(List<BemClassificado> bemClassificadoArtesanatoList) {
		this.bemClassificadoArtesanatoList = bemClassificadoArtesanatoList;
	}

	public List<BemClassificado> getBemClassificadoAgroindustriaList() {
		return bemClassificadoAgroindustriaList;
	}

	public void setBemClassificadoAgroindustriaList(List<BemClassificado> bemClassificadoAgroindustriaList) {
		this.bemClassificadoAgroindustriaList = bemClassificadoAgroindustriaList;
	}


	
}