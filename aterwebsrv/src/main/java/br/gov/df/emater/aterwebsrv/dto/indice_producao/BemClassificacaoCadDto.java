package br.gov.df.emater.aterwebsrv.dto.indice_producao;

import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;

public class BemClassificacaoCadDto extends CadFiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private List<BemClassificacao> bemClassificacaoList;

	private List<BemClassificacao> bemClassificacaoMatrizList;

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
}