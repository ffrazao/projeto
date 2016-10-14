package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.List;

import br.gov.df.emater.aterwebsrv.dao._FiltrarCustom;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.IndiceProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;

public interface ProducaoProprietarioDaoCustom extends _FiltrarCustom<IndiceProducaoCadFiltroDto> {
	
	List<ProducaoProprietario> filtrarNovo(IndiceProducaoCadFiltroDto filtro);
	
}