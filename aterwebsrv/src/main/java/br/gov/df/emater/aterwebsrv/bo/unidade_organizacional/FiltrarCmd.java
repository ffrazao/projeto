package br.gov.df.emater.aterwebsrv.bo.unidade_organizacional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.funcional.UnidadeOrganizacionalDao;
import br.gov.df.emater.aterwebsrv.modelo.dto.UnidadeOrganizacionalCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;

@Service("UnidadeOrganizacionalFiltrarCmd")
public class FiltrarCmd extends _Comando {
	
	@Autowired
	private UnidadeOrganizacionalDao unidadeOrganizacionalDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		System.out.println("Filtrando Unidade Organizacional...");
		UnidadeOrganizacionalCadFiltroDto filtro = (UnidadeOrganizacionalCadFiltroDto) contexto.getRequisicao();
		List<UnidadeOrganizacional> result = null;
		result = unidadeOrganizacionalDao.findByNomeLikeAndClassificacaoIn(filtro.getNomeLike(), filtro.getClassificacao());
		contexto.setResposta(result);
		return false;
	}

}
