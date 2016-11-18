package br.gov.df.emater.aterwebsrv.dao.atividade;

import java.util.List;

import br.gov.df.emater.aterwebsrv.dao._FiltrarCustom;
import br.gov.df.emater.aterwebsrv.dto.agenda.AgendaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.agenda.AgendaDto;
import br.gov.df.emater.aterwebsrv.dto.atividade.AtividadeCadFiltroDto;

public interface AtividadeDaoCustom extends _FiltrarCustom<AtividadeCadFiltroDto> {

	public List<AgendaDto> filtrarAgenda(AgendaCadFiltroDto filtro);
}