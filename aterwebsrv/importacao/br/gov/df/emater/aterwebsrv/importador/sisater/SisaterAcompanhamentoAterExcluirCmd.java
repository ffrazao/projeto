package br.gov.df.emater.aterwebsrv.importador.sisater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadeDao;

@Service
public class SisaterAcompanhamentoAterExcluirCmd extends _Comando {

	@Autowired
	private AtividadeDao atividadeDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		try {
			atividadeDao.deleteAll();
			atividadeDao.flush();
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}

		return false;
	}

}