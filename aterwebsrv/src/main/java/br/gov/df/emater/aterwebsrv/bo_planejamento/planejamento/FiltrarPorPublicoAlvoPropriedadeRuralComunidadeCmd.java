package br.gov.df.emater.aterwebsrv.bo_planejamento.planejamento;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoPropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dto.ater.PublicoAlvoPropriedadeRuralCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;

@Service("PropriedadeRuralFiltrarPorPublicoAlvoPropriedadeRuralComunidadeCmd")
public class FiltrarPorPublicoAlvoPropriedadeRuralComunidadeCmd extends _Comando {

	@Autowired
	private PublicoAlvoPropriedadeRuralDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		PublicoAlvoPropriedadeRuralCadFiltroDto filtro = (PublicoAlvoPropriedadeRuralCadFiltroDto) contexto.getRequisicao();
		List<Object> result = null;
		List<PublicoAlvoPropriedadeRural> list = null;
		if (!CollectionUtils.isEmpty(filtro.getPublicoAlvoList())) {
			list = dao.findByPublicoAlvoInAndPropriedadeRuralIdIsNotNull(filtro.getPublicoAlvoList());
		} else {
			list = dao.findByPropriedadeRuralInAndComunidadeUnidadeOrganizacionalInAndPublicoAlvoIdIsNotNull(filtro.getPropriedadeRuralList(), filtro.getUnidadeOrganizacionalList());
		}
		if (list != null) {
			for (PublicoAlvoPropriedadeRural papr : list) {
				List<Object> linha = new ArrayList<Object>();
				if (!CollectionUtils.isEmpty(filtro.getPublicoAlvoList())) {
					linha.add(papr.getPropriedadeRural().getId());
					linha.add(papr.getPropriedadeRural().getNome());
				} else {
					linha.add(papr.getPublicoAlvo().getId());
					linha.add(papr.getPublicoAlvo().getPessoa().getNome());
					linha.add(papr.getPublicoAlvo().getPessoa().getPessoaTipo());
				}
				if (result == null) {
					result = new ArrayList<Object>();
				}
				result.add(linha);
			}
		}
		contexto.setResposta(result);
		return false;
	}

}
