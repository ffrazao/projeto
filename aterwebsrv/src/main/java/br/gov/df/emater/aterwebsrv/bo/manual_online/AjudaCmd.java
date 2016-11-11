package br.gov.df.emater.aterwebsrv.bo.manual_online;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.ManualOnlineDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.ManualOnline;

@Service("ManualOnlineAjudaCmd")
public class AjudaCmd extends _Comando {

	@Autowired
	private ManualOnlineDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		String codigo = (String) contexto.getRequisicao();
		ManualOnline manualOnline = dao.findOneByCodigo(codigo);

		if (manualOnline == null) {
			throw new BoException("Registro não localizado");
		}

		contexto.setResposta(manualOnline.getHtml());

		return false;
	}
}