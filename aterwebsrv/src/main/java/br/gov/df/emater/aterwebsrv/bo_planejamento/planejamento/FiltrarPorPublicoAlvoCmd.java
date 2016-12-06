package br.gov.df.emater.aterwebsrv.bo_planejamento.planejamento;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoPropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;

@Service("PropriedadeRuralFiltrarPorPublicoAlvoCmd")
public class FiltrarPorPublicoAlvoCmd extends _Comando {

	@Autowired
	private PublicoAlvoPropriedadeRuralDao dao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		List<PublicoAlvo> result = (List<PublicoAlvo>) contexto.getRequisicao();
		try {
			List<PublicoAlvoPropriedadeRural> list = dao.findByPublicoAlvoInAndPropriedadeRuralIdIsNotNull(result);
			Calendar hoje = Calendar.getInstance();
			if (list != null) {
				for (PublicoAlvo pa : result) {
					for (PublicoAlvoPropriedadeRural papr : list) {
						if (pa.getId().equals(papr.getPublicoAlvo().getId())) {
							if (papr.getInicio().before(hoje) && (papr.getTermino() == null || papr.getTermino().after(hoje))) {
								if (pa.getPublicoAlvoPropriedadeRuralList() == null) {
									pa.setPublicoAlvoPropriedadeRuralList(new ArrayList<>());
								}
								pa.getPublicoAlvoPropriedadeRuralList().add(papr.infoBasica());
							}
						}
					}
				}
			}
		} catch (DataIntegrityViolationException e) {
			throw new BoException("Não é possível excluir este registro! Há dados vinculados", e);
		}
		contexto.setResposta(result);
		return false;
	}

}