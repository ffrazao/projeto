package br.gov.df.emater.aterwebsrv.bo.comunidade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.ComunidadeDao;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.dto.ComunidadeListaDto;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Service("ComunidadeListaCmd")
public class ListaCmd extends _Comando {

	@Autowired
	private ComunidadeDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ComunidadeListaDto filtro = (ComunidadeListaDto) contexto.getRequisicao();
		List<Comunidade> comunidadeList = null;
		List<Object> result = null;
		if (!CollectionUtils.isEmpty(filtro.getPessoaJuridicaList())) {
			comunidadeList = dao.findByUnidadeOrganizacionalPessoaJuridicaIdInAndNomeLikeOrderByNomeAsc(filtro.getPessoaJuridicaList(), filtro.getNomeLike());
		} else if (!CollectionUtils.isEmpty(filtro.getUnidadeOrganizacionalList())) {
			comunidadeList = dao.findByUnidadeOrganizacionalIdInAndNomeLikeOrderByNomeAsc(filtro.getUnidadeOrganizacionalList(), filtro.getNomeLike());
		} else {
			comunidadeList = dao.findByNomeLikeOrderByNomeAsc(filtro.getNomeLike());
		}
		for (Comunidade c : comunidadeList) {
			PessoaJuridica pessoaJuridica = (PessoaJuridica) c.getUnidadeOrganizacional().getPessoaJuridica().infoBasica();
			UnidadeOrganizacional unidadeOrganizacional = c.getUnidadeOrganizacional().infoBasica();
			Comunidade comunidade = c.infoBasica();

			unidadeOrganizacional.setPessoaJuridica(pessoaJuridica);
			comunidade.setUnidadeOrganizacional(unidadeOrganizacional);
			if (result == null) {
				result = new ArrayList<Object>();
			}
			result.add(comunidade);
		}
		// ordernar pelo nome de todas as comunidades
		Collections.sort(result, new Comparator<Object>() {
			@Override
			public int compare(Object oba, Object obb) {
				Comunidade a = (Comunidade) oba;
				Comunidade b = (Comunidade) obb;
				return a.getNome().compareTo(b.getNome());
			}
		});

		// ordernar pela Unidade Organizacional das comunidades
		Collections.sort(result, new Comparator<Object>() {
			@Override
			public int compare(Object oba, Object obb) {
				Comunidade a = (Comunidade) oba;
				Comunidade b = (Comunidade) obb;
				return a.getUnidadeOrganizacional().getNome().compareTo(b.getUnidadeOrganizacional().getNome());
			}
		});

		contexto.setResposta(result);
		return false;
	}

}
