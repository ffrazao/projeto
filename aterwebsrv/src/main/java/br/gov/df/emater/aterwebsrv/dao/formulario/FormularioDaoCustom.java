package br.gov.df.emater.aterwebsrv.dao.formulario;

import java.util.List;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.dao._FiltrarCustom;
import br.gov.df.emater.aterwebsrv.modelo.dto.FormularioCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.dto.FormularioColetaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersao;

public interface FormularioDaoCustom extends _FiltrarCustom<FormularioCadFiltroDto> {

	List<FormularioVersao> filtrarComColeta(FormularioColetaCadFiltroDto filtro) throws Exception;

}