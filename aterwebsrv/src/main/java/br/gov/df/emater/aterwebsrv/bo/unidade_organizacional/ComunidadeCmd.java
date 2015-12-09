package br.gov.df.emater.aterwebsrv.bo.unidade_organizacional;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.funcional.UnidadeOrganizacionalComunidadeDao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacionalComunidade;

@Service("UnidadeOrganizacionalComunidadeCmd")
public class ComunidadeCmd extends _Comando {

	@Autowired
	private UnidadeOrganizacionalComunidadeDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		System.out.println("Filtrando Unidade Organizacional...");
		Integer pessoaJuridicaId = (Integer) contexto.getRequisicao();
		List<UnidadeOrganizacionalComunidade> result = null;
		List<UnidadeOrganizacionalComunidade> lista = null;
		lista = dao.findByUnidadeOrganizacionalPessoaJuridicaId(pessoaJuridicaId);
		if (lista != null) {
			result = new ArrayList<UnidadeOrganizacionalComunidade>();
			for (UnidadeOrganizacionalComunidade uoc : lista) {
				uoc.setComunidade(uoc.getComunidade().infoBasica());
				uoc.setUnidadeOrganizacional(uoc.getUnidadeOrganizacional().infoBasica());
				uoc.setId(null);
				result.add(uoc);
			}
		}
		contexto.setResposta(result);
		return false;
	}

}
