package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoDao;
import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;

@Service("PessoaPublicoAlvoPorPessoaIdCmd")
public class PublicoAlvoPorPessoaIdCmd extends _Comando {

	@Autowired
	private PublicoAlvoDao dao;

	@PersistenceContext(unitName = EntidadeBase.PERSISTENCE_UNIT)
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		PublicoAlvo result = dao.findOneByPessoaId(id);

		if (result == null) {
			throw new BoException("Registro não localizado");
		}

		List<PublicoAlvoPropriedadeRural> propList = new ArrayList<>();
		result.getPublicoAlvoPropriedadeRuralList().forEach((pr) -> propList.add(pr.infoBasica()));

		result = result.infoBasica();
		result.setPublicoAlvoPropriedadeRuralList(propList);

		em.detach(result);
		contexto.setResposta(result);

		return false;
	}
}