package br.gov.df.emater.aterwebsrv.bo.propriedade_rural;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;

@Service("PropriedadeRuralVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private PropriedadeRuralDao dao;

	@Autowired
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		PropriedadeRural result = dao.findOne(id);

		if (result == null) {
			throw new BoException("Registro não localizado");
		}
		
		result.setUsuarioInclusao(result.getUsuarioInclusao().infoBasica());
		result.setUsuarioAlteracao(result.getUsuarioAlteracao().infoBasica());
		result.setEndereco(result.getEndereco().infoBasica());

		em.detach(result);
		contexto.setResposta(result);

		return false;
	}
}