package br.gov.df.emater.aterwebsrv.bo.manual_online;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo._SalvarCmd;
import br.gov.df.emater.aterwebsrv.dao.sistema.ManualOnlineDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.ManualOnline;

@Service("ManualOnlineSalvarCmd")
public class SalvarCmd extends _SalvarCmd {

	@Autowired
	private ManualOnlineDao dao;

	public SalvarCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ManualOnline result = (ManualOnline) contexto.getRequisicao();

		logAtualizar(result, contexto);
		
		result.setCodigo(result.getCodigo().toUpperCase().replaceAll("\\s", "_"));

		// preparar para salvar
		dao.save(result);

		contexto.setResposta(result.getId());
		return false;
	}
}