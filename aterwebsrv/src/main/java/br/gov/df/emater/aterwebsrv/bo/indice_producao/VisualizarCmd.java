package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaList;
import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoComposicaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoProprietarioDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioData;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoComposicao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;

@Service("IndiceProducaoVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private ProducaoProprietarioDao dao;

	@Autowired
	private EntityManager em;

	@Autowired
	private ProducaoComposicaoDao producaoComposicaoDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();

		ProducaoProprietario result = fetch(id);
		if (result.getUnidadeOrganizacional() != null) {
			List<Integer> idList = dao.findAllByAnoAndBemClassificadoIdAndPropriedadeRuralComunidadeUnidadeOrganizacionalId(result.getAno(), result.getBemClassificado().getId(), result.getUnidadeOrganizacional().getId());
			if (idList != null) {
				List<ProducaoProprietario> producaoProprietarioList = new ArrayList<>();
				for (Integer producaoProprietarioid : idList) {
					producaoProprietarioList.add(fetch(producaoProprietarioid));
				}
				result.setProducaoProprietarioList(producaoProprietarioList);
			}
		}

		em.detach(result);
		contexto.setResposta(result);

		return false;
	}

	private ProducaoProprietario fetch(Integer id) throws BoException {
		ProducaoProprietario result = dao.findOne(id);
		if (result == null) {
			throw new BoException("Registro não localizado");
		}

		// limpar dados
		result.setInclusaoUsuario(infoBasicaReg(result.getInclusaoUsuario()));
		result.setAlteracaoUsuario(infoBasicaReg(result.getAlteracaoUsuario()));

		if (result.getUnidadeOrganizacional() != null) {
			result.setUnidadeOrganizacional(infoBasicaReg(result.getUnidadeOrganizacional()));
		}

		if (result.getPublicoAlvo() != null) {
			// preservar os vinculos com as propriedades rurais
			List<PublicoAlvoPropriedadeRural> publicoAlvoPropriedadeRuralList = infoBasicaList(result.getPublicoAlvo().getPublicoAlvoPropriedadeRuralList());
			result.setPublicoAlvo(infoBasicaReg(result.getPublicoAlvo()));
			if (publicoAlvoPropriedadeRuralList != null) {
				// filtrar somente os vínculos ativos no momento da coleta do
				// IPA
				Calendar inicio = UtilitarioData.getInstance().ajustaInicioDia(new GregorianCalendar(result.getAno(), 0, 1));
				Calendar termino = UtilitarioData.getInstance().ajustaFinalDia(new GregorianCalendar(result.getAno(), 11, 31));
				publicoAlvoPropriedadeRuralList.stream().filter(papr -> papr.getInicio().before(termino) && (papr.getTermino() == null || papr.getTermino().after(inicio))).collect(Collectors.toList());
				if (publicoAlvoPropriedadeRuralList.size() > 0) {
					result.getPublicoAlvo().setPublicoAlvoPropriedadeRuralList(publicoAlvoPropriedadeRuralList);
				}
			}
		}

		if (result.getPropriedadeRural() != null) {
			result.setPropriedadeRural(infoBasicaReg(result.getPropriedadeRural()));
		}

		result.setBemClassificado(result.getBemClassificado().infoBasica());

		if (result.getProducaoList() != null) {
			for (Producao producao : result.getProducaoList()) {
				producao.setInclusaoUsuario(infoBasicaReg(producao.getInclusaoUsuario()));
				producao.setAlteracaoUsuario(infoBasicaReg(producao.getAlteracaoUsuario()));
				producao.setProducaoProprietario(null);
				if (producao.getProducaoComposicaoList() != null) {
					producao.setProducaoComposicaoList(producaoComposicaoDao.findAllByProducao(producao));
					for (ProducaoComposicao pfc : producao.getProducaoComposicaoList()) {
						pfc.setProducao(null);
						pfc.setFormaProducaoValor(infoBasicaReg(pfc.getFormaProducaoValor()));
					}
				}
			}
		}

		return result;
	}
}