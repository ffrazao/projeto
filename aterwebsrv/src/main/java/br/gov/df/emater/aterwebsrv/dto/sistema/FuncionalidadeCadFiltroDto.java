package br.gov.df.emater.aterwebsrv.dto.sistema;

import java.util.Set;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;
import br.gov.df.emater.aterwebsrv.dto.TagDto;

public class FuncionalidadeCadFiltroDto extends CadFiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private Set<TagDto> comando;

	private Set<TagDto> funcionalidade;

	private Set<TagDto> modulo;

	public Set<TagDto> getComando() {
		return comando;
	}

	public Set<TagDto> getFuncionalidade() {
		return funcionalidade;
	}

	public Set<TagDto> getModulo() {
		return modulo;
	}

	public void setComando(Set<TagDto> comando) {
		this.comando = comando;
	}

	public void setFuncionalidade(Set<TagDto> funcionalidade) {
		this.funcionalidade = funcionalidade;
	}

	public void setModulo(Set<TagDto> modulo) {
		this.modulo = modulo;
	}

}