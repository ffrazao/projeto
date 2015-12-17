package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoValor;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoFormaComposicao;

@Service("IndiceProducaoVisualizarCmd")
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
		
		UnidadeOrganizacional unidadeOrganizacional = result.getComunidade().getUnidadeOrganizacional().infoBasica();
		result.setComunidade(result.getComunidade() != null ? result.getComunidade().infoBasica(): null);
		result.getComunidade().setUnidadeOrganizacional(unidadeOrganizacional);
		
		result.setPropriedadeRural(result.getPropriedadeRural() != null? result.getPropriedadeRural().infoBasica(): null);
		result.setPublicoAlvo(result.getPublicoAlvo() != null ? result.getPublicoAlvo().infoBasica() : null);
		result.getBem().setBemClassificacao(new BemClassificacao(result.getBem().getBemClassificacao().getId()));
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
						pfc.setFormaProducaoValor(new FormaProducaoValor(pfc.getFormaProducaoValor().getId()));
					}
				}
			}
		}

		em.detach(result);
		contexto.setResposta(result);

		return false;
	}
}