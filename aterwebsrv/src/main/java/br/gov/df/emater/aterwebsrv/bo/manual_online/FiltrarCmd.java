package br.gov.df.emater.aterwebsrv.bo.manual_online;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.ManualOnlineDao;
import br.gov.df.emater.aterwebsrv.dto.sistema.ManualOnlineCadFiltroDto;

@Service("ManualOnlineFiltrarCmd")
public class FiltrarCmd extends _Comando {

	@Autowired
	private ManualOnlineDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ManualOnlineCadFiltroDto filtro = (ManualOnlineCadFiltroDto) contexto.getRequisicao();
		List<Object> result = null;
		//result = (List<Object>) dao.filtrar(filtro);
		contexto.setResposta(result);
		return false;
	}

}