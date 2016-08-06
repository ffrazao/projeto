package br.gov.df.emater.aterwebsrv.bo.usuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.dto.UsuarioCadFiltroDto;

@Service("UsuarioFiltrarCmd")
public class FiltrarCmd extends _Comando {

	@Autowired
	private UsuarioDao dao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		UsuarioCadFiltroDto filtro = (UsuarioCadFiltroDto) contexto.getRequisicao();
		List<Object> result = null;
		result = (List<Object>) dao.filtrar(filtro);
		contexto.setResposta(result);
		return false;
	}

}