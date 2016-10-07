package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoProprietarioDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoComposicaoDao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoComposicao;

@Service("IndiceProducaoVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private ProducaoProprietarioDao dao;
	
	@Autowired
	private ProducaoComposicaoDao producaoComposicaoDao;

	@Autowired
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		ProducaoProprietario result = dao.findOne(id);

		if (result == null) {
			throw new BoException("Registro não localizado");
		}

		// limpar dados
		if (result.getInclusaoUsuario() != null) {
			result.setInclusaoUsuario(result.getInclusaoUsuario().infoBasica());
		}
		if (result.getAlteracaoUsuario() != null) {
			result.setAlteracaoUsuario(result.getAlteracaoUsuario().infoBasica());
		}

		result.setUnidadeOrganizacional(result.getUnidadeOrganizacional() == null ? null : result.getUnidadeOrganizacional().infoBasica());
		result.setPropriedadeRural(result.getPropriedadeRural() == null ? null : result.getPropriedadeRural().infoBasica());
		result.setPublicoAlvo(result.getPublicoAlvo() == null ? null : result.getPublicoAlvo().infoBasica());

		result.setBemClassificado(result.getBemClassificado().infoBasica());
		
		if (result.getProducaoList() != null) {
			for (Producao pf : result.getProducaoList()) {
				if (pf.getInclusaoUsuario() != null) {
					pf.setInclusaoUsuario(pf.getInclusaoUsuario().infoBasica());
				}
				if (pf.getAlteracaoUsuario() != null) {
					pf.setAlteracaoUsuario(pf.getAlteracaoUsuario().infoBasica());
				}
				pf.setProducaoProprietario(null);
				if (pf.getProducaoComposicaoList() != null) {
					pf.setProducaoComposicaoList(producaoComposicaoDao.findAllByProducao(pf));
					for (ProducaoComposicao pfc : pf.getProducaoComposicaoList()) {
						pfc.setProducao(null);
						pfc.setFormaProducaoValor(pfc.getFormaProducaoValor().infoBasica());
					}
				}
			}
		}

		em.detach(result);
		contexto.setResposta(result);

		return false;
	}
}