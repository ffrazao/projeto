package br.gov.df.emater.aterwebsrv.bo.propriedade_rural;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRuralArquivo;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRuralFormaUtilizacaoEspacoRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRuralPendencia;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;

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

		result.setInclusaoUsuario(result.getInclusaoUsuario() == null ? null: result.getInclusaoUsuario().infoBasica());
		result.setAlteracaoUsuario(result.getAlteracaoUsuario() == null ? null: result.getAlteracaoUsuario().infoBasica());
		result.setEndereco(result.getEndereco().infoBasica());

		if (result.getPublicoAlvoPropriedadeRuralList() != null) {
			List<PublicoAlvoPropriedadeRural> paprList = new ArrayList<PublicoAlvoPropriedadeRural>();
			for (PublicoAlvoPropriedadeRural papr : result.getPublicoAlvoPropriedadeRuralList()) {
				PublicoAlvoPropriedadeRural p = new PublicoAlvoPropriedadeRural(papr.getId());
				p.setArea(papr.getArea());
				p.setComunidade(papr.getComunidade().infoBasica());
				p.setInicio(papr.getInicio());
				p.setPublicoAlvo(papr.getPublicoAlvo().infoBasica());
				p.setTermino(papr.getTermino());
				p.setVinculo(papr.getVinculo());
				paprList.add(p);
			}
			result.setPublicoAlvoPropriedadeRuralList(paprList);
		}
		
		if (result.getFormaUtilizacaoEspacoRuralList() != null) {
			for (PropriedadeRuralFormaUtilizacaoEspacoRural formaUtilizacaoEspacoRural : result.getFormaUtilizacaoEspacoRuralList()) {
				formaUtilizacaoEspacoRural.setPropriedadeRural(null);
			}
		}
		if (result.getArquivoList() != null) {
			for (PropriedadeRuralArquivo a: result.getArquivoList()) {
				a.getId();
			}
		}
		
		if (result.getPendenciaList() != null) {
			for (PropriedadeRuralPendencia propriedadeRuralPendencia: result.getPendenciaList()) {
				propriedadeRuralPendencia.setPropriedadeRural(null);
			}
		}

		
		em.detach(result);
		contexto.setResposta(result);

		return false;
	}
}