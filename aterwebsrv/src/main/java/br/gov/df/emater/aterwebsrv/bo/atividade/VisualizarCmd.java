package br.gov.df.emater.aterwebsrv.bo.atividade;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoFormaComposicao;

@Service("AtividadeVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private ProducaoDao dao;

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
		result.setAlteracaoUsuario(null);
		result.setAlteracaoData(null);
		result.setInclusaoUsuario(null);
		result.setInclusaoData(null);

		if (result.getUnidadeOrganizacional() != null) {
			result.setUnidadeOrganizacional(result.getUnidadeOrganizacional() == null ? null : result.getUnidadeOrganizacional().infoBasica());
		}
		result.setPropriedadeRural(result.getPropriedadeRural() != null ? result.getPropriedadeRural().infoBasica() : null);
		result.setPublicoAlvo(result.getPublicoAlvo() != null ? result.getPublicoAlvo().infoBasica() : null);
		result.setBem(result.getBem().infoBasica());
		if (result.getProducaoFormaList() != null) {
			for (ProducaoForma pf : result.getProducaoFormaList()) {
				pf.setAlteracaoUsuario(null);
				pf.setAlteracaoData(null);
				pf.setInclusaoUsuario(null);
				pf.setInclusaoData(null);
				pf.setProducao(null);
				if (pf.getProducaoFormaComposicaoList() != null) {
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