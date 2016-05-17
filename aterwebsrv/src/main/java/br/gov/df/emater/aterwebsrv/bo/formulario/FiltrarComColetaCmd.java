package br.gov.df.emater.aterwebsrv.bo.formulario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.formulario.ColetaDao;
import br.gov.df.emater.aterwebsrv.dao.formulario.FormularioDao;
import br.gov.df.emater.aterwebsrv.modelo.dto.FormularioColetaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Coleta;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersao;

@Service("FormularioFiltrarComColetaCmd")
public class FiltrarComColetaCmd extends _Comando {

	@Autowired
	private FormularioDao dao;

	@Autowired
	private ColetaDao coletaDao;

	@Override
	@SuppressWarnings("unchecked")
	public boolean executar(_Contexto contexto) throws Exception {
		List<Object[]> resposta = (List<Object[]>) contexto.getResposta();
		
		if (resposta == null || resposta.size() == 0) {
			return false;
		}
		
		FormularioColetaCadFiltroDto filtro = (FormularioColetaCadFiltroDto) contexto.getRequisicao();
		
		List<Object[]> result = new ArrayList<Object[]>();
		
		for (Object[] resp: resposta) {
			List<Object> linha = new ArrayList<Object>();
			linha.addAll(Arrays.asList(resp));
			filtro.setFormularioId((Integer) linha.get(0));
			List<FormularioVersao> formularioVersaoList = dao.filtrarComColeta(filtro); 
			
			if (formularioVersaoList != null && formularioVersaoList.size() > 0) {
				for (int i = 0; i < formularioVersaoList.size(); i++) {
					FormularioVersao fv = formularioVersaoList.get(i).infoBasica();
					fv.setColetaList(null);
					if ((filtro.getPessoa() != null && filtro.getPessoa().getId() != null) || (filtro.getPropriedadeRural() != null && filtro.getPropriedadeRural().getId() != null)) {
						List<Coleta> coletaList = null;
						if (filtro.getPessoa() != null && filtro.getPessoa().getId() != null) {
							coletaList = coletaDao.findByFormularioVersaoAndPessoa(fv, filtro.getPessoa());
						}
						if (filtro.getPropriedadeRural() != null && filtro.getPropriedadeRural().getId() != null) {
							coletaList = coletaDao.findByFormularioVersaoAndPropriedadeRural(fv, filtro.getPropriedadeRural());
						}
						if (coletaList != null) {
							for (int j = 0; j < coletaList.size(); j++) {
								coletaList.set(j, coletaList.get(j).infoBasica());
							}
						}
						fv.setColetaList(coletaList);
					}
					formularioVersaoList.set(i, fv);
				}
			}
			linha.add(formularioVersaoList);
			result.add(linha.toArray());
		}
		
		contexto.setResposta(result);

		return false;
	}
}