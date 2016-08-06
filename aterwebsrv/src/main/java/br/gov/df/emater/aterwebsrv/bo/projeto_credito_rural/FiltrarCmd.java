package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoRuralDao;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoCreditoRuralCadFiltroDto;

@Service("ProjetoCreditoRuralFiltrarCmd")
public class FiltrarCmd extends _Comando {

	@Autowired
	private ProjetoCreditoRuralDao dao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ProjetoCreditoRuralCadFiltroDto filtro = (ProjetoCreditoRuralCadFiltroDto) contexto.getRequisicao();
		List<Object> result = null;
		result = (List<Object>) dao.filtrar(filtro);
		contexto.setResposta(result);
		return false;
	}

}
