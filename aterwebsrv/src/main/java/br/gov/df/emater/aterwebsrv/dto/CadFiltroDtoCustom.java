package br.gov.df.emater.aterwebsrv.dto;

import javax.persistence.Query;

public abstract class CadFiltroDtoCustom implements CadFiltroDto {

	private static final long serialVersionUID = 1L;

	private Integer numeroPagina;

	private Integer registrosPagina;

	private TemMaisRegistros temMaisRegistros;

	public void configuraPaginacao(org.hibernate.Query query) {
		Integer ini = this.getNumeroPagina() == null ? 1 : this.getNumeroPagina();
		Integer tam = CadFiltroDto.TAMANHO_PAGINA;

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

	public void configuraPaginacao(Query query) {
		Integer ini = this.getNumeroPagina() == null ? 1 : this.getNumeroPagina();
		Integer tam = CadFiltroDto.TAMANHO_PAGINA;

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

	@Override
	public Integer getNumeroPagina() {
		return numeroPagina;
	}

	@Override
	public Integer getRegistrosPagina() {
		return registrosPagina;
	}

	@Override
	public TemMaisRegistros getTemMaisRegistros() {
		return temMaisRegistros;
	}

	@Override
	public void setNumeroPagina(Integer numeroPagina) {
		this.numeroPagina = numeroPagina;
	}

	@Override
	public void setRegistrosPagina(Integer registrosPagina) {
		this.registrosPagina = registrosPagina;
	}

	@Override
	public void setTemMaisRegistros(TemMaisRegistros temMaisRegistros) {
		this.temMaisRegistros = temMaisRegistros;
	}

}
