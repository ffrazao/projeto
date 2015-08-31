package br.gov.df.emater.aterwebsrv.modelo.dto;

public interface FiltroDto extends Dto {

	Integer getNumeroPagina();

	void setNumeroPagina(Integer numeroPagina);

	Integer getRegistrosPagina();

	void setRegistrosPagina(Integer registrosPagina);

}
