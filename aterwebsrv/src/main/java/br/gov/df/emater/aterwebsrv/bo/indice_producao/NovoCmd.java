package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoProprietarioDao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;

@Service("IndiceProducaoNovoCmd")
public class NovoCmd extends _Comando {

	@Autowired
	private ProducaoProprietarioDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ProducaoProprietario result = (ProducaoProprietario) contexto.getRequisicao();

		if (result == null) {
			result = new ProducaoProprietario();
		} else {
			if (result.getId() != null) {
				ProducaoProprietario modelo = dao.findOne(result.getId());
				result.setId(null);
				result.setAno(modelo.getAno());
				result.setBemClassificado(modelo.getBemClassificado());
			}
		}
		if (result.getAno() == null) {
			result.setAno(Calendar.getInstance().get(Calendar.YEAR));
		}

		contexto.setResposta(result);

		return true;
	}

}