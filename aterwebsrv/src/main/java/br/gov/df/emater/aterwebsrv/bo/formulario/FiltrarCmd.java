package br.gov.df.emater.aterwebsrv.bo.formulario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.formulario.FormularioDao;
import br.gov.df.emater.aterwebsrv.dto.formulario.FormularioCadFiltroDto;

@Service("FormularioFiltrarCmd")
public class FiltrarCmd extends _Comando {

	@Autowired
	private FormularioDao formularioDao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		FormularioCadFiltroDto filtro = (FormularioCadFiltroDto) contexto.getRequisicao();
		List<Object> result = null;
		result = (List<Object>) formularioDao.filtrar(filtro);
		contexto.setResposta(result);
		return false;
	}

}
