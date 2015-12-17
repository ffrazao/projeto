package br.gov.df.emater.aterwebsrv.bo.comunidade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.ComunidadeDao;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.dto.ComunidadeListaDto;

@Service("ComunidadeListaCmd")
public class ListaCmd extends _Comando {

	@Autowired
	private ComunidadeDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ComunidadeListaDto filtro = (ComunidadeListaDto) contexto.getRequisicao();
		List<Comunidade> result = null;
		if(filtro.getUnidadeOrganizacionalList() != null ){
			result = dao.findByUnidadeOrganizacionalIdInAndNomeLike(filtro.getUnidadeOrganizacionalList(), filtro.getNomeLike());
		} else {
			result = dao.findByNomeLike( filtro.getNomeLike());
		}
		contexto.setResposta(result);
		return false;
	}

}
