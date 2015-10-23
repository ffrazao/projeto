package br.gov.df.emater.aterwebsrv.modelo.dto;

import javax.persistence.Query;

public abstract class FiltroDtoCustom implements FiltroDto {

	private static final long serialVersionUID = 1L;

	private Integer numeroPagina;

	private Integer registrosPagina;

	private TemMaisRegistros temMaisRegistros;

	public Integer getNumeroPagina() {
		return numeroPagina;
	}
	
	public void configuraPaginacao(Query query) {
		Integer ini = this.getNumeroPagina() == null ? 1 : this.getNumeroPagina();
		Integer tam = FiltroDto.TAMANHO_PAGINA;

		ini = (ini - 1) * tam;

		if (this.getTemMaisRegistros() != null) {
			switch (this.getTemMaisRegistros()) {
			case PRIMEIRA_PAGINA:
				ini = 1;
				break;
			case ULTIMA_PAGINA:
				tam = null;
			case PROXIMA_PAGINA:
				if (ini == 0) {
					ini = tam;
				}
			default:
				break;
			}
		}
		query.setFirstResult(ini);
		if (tam != null) {
			query.setMaxResults(tam);
		}
	}

	public void setNumeroPagina(Integer numeroPagina) {
		this.numeroPagina = numeroPagina;
	}

	public Integer getRegistrosPagina() {
		return registrosPagina;
	}

	public void setRegistrosPagina(Integer registrosPagina) {
		this.registrosPagina = registrosPagina;
	}

	public TemMaisRegistros getTemMaisRegistros() {
		return temMaisRegistros;
	}

	public void setTemMaisRegistros(TemMaisRegistros temMaisRegistros) {
		this.temMaisRegistros = temMaisRegistros;
	}

}
