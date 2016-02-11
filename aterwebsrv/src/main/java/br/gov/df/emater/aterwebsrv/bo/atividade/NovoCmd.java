package br.gov.df.emater.aterwebsrv.bo.atividade;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;

@Service("AtividadeNovoCmd")
public class NovoCmd extends _Comando {

	@Autowired
	private ProducaoDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Producao result = (Producao) contexto.getRequisicao();

		if (result == null) {
			result = new Producao();
		} else {
			if (result.getId() != null) {
				Producao modelo = dao.findOne(result.getId());
				result.setId(null);
				result.setAno(modelo.getAno());
				result.setBem(modelo.getBem());
			}
		}
		if (result.getAno() == null) {
			result.setAno(Calendar.getInstance().get(Calendar.YEAR));
		}

		contexto.setResposta(result);

		return true;
	}

}