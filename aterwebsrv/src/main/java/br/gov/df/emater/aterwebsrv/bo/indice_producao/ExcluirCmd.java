package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoProprietarioDao;

@Service("IndiceProducaoExcluirCmd")
public class ExcluirCmd extends _Comando {

	@Autowired
	private ProducaoProprietarioDao dao;

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