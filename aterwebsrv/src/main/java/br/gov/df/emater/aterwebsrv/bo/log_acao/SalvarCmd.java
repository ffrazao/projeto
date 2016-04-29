package br.gov.df.emater.aterwebsrv.bo.log_acao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.LogAcaoDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.LogAcao;

@Service("LogAcaoSalvarCmd")
public class SalvarCmd extends _Comando {

	@Autowired
	private LogAcaoDao dao;

	public SalvarCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		LogAcao result = (LogAcao) contexto.getRequisicao();

		// preparar para salvar
		dao.save(result);

		contexto.setResposta(result.getId());
		return false;
	}
}