package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;

@Service("IndiceProducaoSalvarCmd")
public class SalvarCmd extends _Comando {

	public SalvarCmd() {
	}

	@Autowired
	private ProducaoDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Producao result = (Producao) contexto.getRequisicao();
		// if (result.getId() == null) {
		// result.setUsuarioInclusao(getUsuario(contexto.getUsuario().getName()));
		// result.setInclusaoData(Calendar.getInstance());
		// } else {
		// result.setUsuarioInclusao(getUsuario(result.getUsuarioInclusao().getUsername()));
		// }
		// result.setUsuarioAlteracao(getUsuario(contexto.getUsuario().getName()));
		// result.setAlteracaoData(Calendar.getInstance());

		dao.save(result);

		dao.flush();

		contexto.setResposta(result.getId());
		return true;
	}

}