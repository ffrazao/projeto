package br.gov.df.emater.aterwebsrv.bo.log_acao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.LogAcaoDao;
import br.gov.df.emater.aterwebsrv.modelo.dto.LogAcaoCadFiltroDto;

@Service("LogAcaoFiltrarCmd")
public class FiltrarCmd extends _Comando {

	@Autowired
	private LogAcaoDao dao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		LogAcaoCadFiltroDto filtro = (LogAcaoCadFiltroDto) contexto.getRequisicao();
		List<Object> result = null;
		result = (List<Object>) dao.filtrar(filtro);
		contexto.setResposta(result);
		return false;
	}

}