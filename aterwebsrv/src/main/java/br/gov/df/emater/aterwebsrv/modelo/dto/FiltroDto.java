package br.gov.df.emater.aterwebsrv.modelo.dto;

public interface FiltroDto extends Dto {
	
	int TAMANHO_PAGINA = 100;

	enum TemMaisRegistros {
		PRIMEIRA_PAGINA, PROXIMA_PAGINA, ULTIMA_PAGINA;
	};

	Integer getNumeroPagina();

	Integer getRegistrosPagina();

	TemMaisRegistros getTemMaisRegistros();

	void setNumeroPagina(Integer numeroPagina);

	void setRegistrosPagina(Integer registrosPagina);

	void setTemMaisRegistros(TemMaisRegistros temMaisRegistros);

}
