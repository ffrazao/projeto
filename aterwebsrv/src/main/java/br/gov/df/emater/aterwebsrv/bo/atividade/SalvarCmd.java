package br.gov.df.emater.aterwebsrv.bo.atividade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadeDao;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Atividade;

@Service("AtividadeSalvarCmd")
public class SalvarCmd extends _Comando {

	public SalvarCmd() {
	}

	@Autowired
	private AtividadeDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Atividade result = (Atividade) contexto.getRequisicao();
		if (result.getId() == null) {
			result.setInclusaoUsuario(getUsuario(contexto.getUsuario().getName()));
		}
		result.setAlteracaoUsuario(getUsuario(contexto.getUsuario().getName()));

		dao.save(result);

		dao.flush();

		contexto.setResposta(result.getId());
		return false;
	}

}