package br.gov.df.emater.aterwebsrv.modelo.dto;

public class FuncionalidadeCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private String comando;

	private String funcionalidade;

	private String modulo;

	public String getComando() {
		return comando;
	}

	public String getFuncionalidade() {
		return funcionalidade;
	}

	public String getModulo() {
		return modulo;
	}

	public void setComando(String comando) {
		this.comando = comando;
	}

	public void setFuncionalidade(String funcionalidade) {
		this.funcionalidade = funcionalidade;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

}