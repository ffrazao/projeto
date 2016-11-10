package br.gov.df.emater.aterwebsrv.dto.sistema;

import java.util.Set;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;
import br.gov.df.emater.aterwebsrv.dto.TagDto;

public class ManualOnlineCadFiltroDto extends CadFiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private Set<TagDto> codigo;

	public Set<TagDto> getCodigo() {
		return codigo;
	}

	public void setCodigo(Set<TagDto> codigo) {
		this.codigo = codigo;
	}

}