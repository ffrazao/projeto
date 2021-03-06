package br.gov.df.emater.aterwebsrv.bo.propriedade_rural;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralDao;

@Service("PropriedadeRuralExcluirCmd")
public class ExcluirCmd extends _Comando {

	@Autowired
	private PropriedadeRuralDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer result = (Integer) contexto.getRequisicao();
		try {
			dao.delete(result);
			dao.flush();
		} catch (DataIntegrityViolationException e) {
			throw new BoException("Não é possível excluir este registro! Há dados vinculados", e);
		}
		contexto.setResposta(result);
		return false;
	}

}