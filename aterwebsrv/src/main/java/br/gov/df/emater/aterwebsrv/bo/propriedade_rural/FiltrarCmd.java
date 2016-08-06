package br.gov.df.emater.aterwebsrv.bo.propriedade_rural;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dto.PropriedadeRuralCadFiltroDto;

@Service("PropriedadeRuralFiltrarCmd")
public class FiltrarCmd extends _Comando {

	@Autowired
	private PropriedadeRuralDao propriedadeRuralDao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		PropriedadeRuralCadFiltroDto filtro = (PropriedadeRuralCadFiltroDto) contexto.getRequisicao();
		List<Object> result = null;
		result = (List<Object>) propriedadeRuralDao.filtrar(filtro);
		contexto.setResposta(result);
		return false;
	}

}
