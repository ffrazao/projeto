package br.gov.df.emater.aterwebsrv.bo.log_acao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.LogAcaoDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.LogAcao;

@Service("LogAcaoExcluirCmd")
public class ExcluirCmd extends _Comando {

	@Autowired
	private LogAcaoDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		LogAcao logAcao = (LogAcao) contexto.getRequisicao();

		dao.delete(logAcao);

		contexto.setResposta(logAcao);
		return false;
	}

}