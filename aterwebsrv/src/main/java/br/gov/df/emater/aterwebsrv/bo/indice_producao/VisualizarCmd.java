package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoFormaComposicaoDao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoFormaComposicao;

@Service("IndiceProducaoVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private ProducaoDao dao;
	
	@Autowired
	private ProducaoFormaComposicaoDao producaoFormaComposicaoDao;

	@Autowired
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		Producao result = dao.findOne(id);

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

		result.setBem(result.getBem().infoBasica());
		
		if (result.getProducaoFormaList() != null) {
			for (ProducaoForma pf : result.getProducaoFormaList()) {
				if (pf.getInclusaoUsuario() != null) {
					pf.setInclusaoUsuario(pf.getInclusaoUsuario().infoBasica());
				}
				if (pf.getAlteracaoUsuario() != null) {
					pf.setAlteracaoUsuario(pf.getAlteracaoUsuario().infoBasica());
				}
				pf.setProducao(null);
				if (pf.getProducaoFormaComposicaoList() != null) {
					pf.setProducaoFormaComposicaoList(producaoFormaComposicaoDao.findAllByProducaoForma(pf));
					for (ProducaoFormaComposicao pfc : pf.getProducaoFormaComposicaoList()) {
						pfc.setProducaoForma(null);
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