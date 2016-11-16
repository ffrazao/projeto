package br.gov.df.emater.aterwebsrv.bo.colaborador;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.funcional.EmpregadorDao;
import br.gov.df.emater.aterwebsrv.dao.funcional.LotacaoAtivaViDao;
import br.gov.df.emater.aterwebsrv.dao.funcional.UnidadeOrganizacionalHierarquiaAtivaViDao;
import br.gov.df.emater.aterwebsrv.dto.funcional.ColaboradorListaDto;
import br.gov.df.emater.aterwebsrv.dto.funcional.UnidadeOrganizacionalListaDto;
import br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Empregador;
import br.gov.df.emater.aterwebsrv.modelo.funcional.LotacaoAtivaVi;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacionalHierarquiaAtivaVi;

@Service("ColaboradorListaCmd")
public class ListaCmd extends _Comando {

	@Autowired
	private EmpregadorDao empregadorDao;

	@Autowired
	private LotacaoAtivaViDao lotacaoAtivaViDao;

	@Autowired
	private UnidadeOrganizacionalHierarquiaAtivaViDao unidadeOrganizacionalHierarquiaAtivaViDao;

	private List<UnidadeOrganizacionalListaDto> criarArvore(List<UnidadeOrganizacionalHierarquiaAtivaVi> lista, String sigla) {
		List<UnidadeOrganizacionalListaDto> result = null;
		if (!CollectionUtils.isEmpty(lista)) {
			for (UnidadeOrganizacionalHierarquiaAtivaVi uo : lista) {
				if ((uo.getAscendente() == null && sigla == null) || (uo.getAscendente() != null && uo.getAscendente().getSigla().equals(sigla))) {
					if (result == null) {
						result = new ArrayList<>();
					}
					UnidadeOrganizacionalListaDto item = new UnidadeOrganizacionalListaDto();
					item.setUnidadeOrganizacional(uo.getDescendente().infoBasica());
					List<LotacaoAtivaVi> lotacaoList = lotacaoAtivaViDao.findAllByUnidadeOrganizacionalId(uo.getDescendente().getId());
					if (!CollectionUtils.isEmpty(lotacaoList)) {
						item.setLotacaoList(UtilitarioInfoBasica.infoBasicaList(lotacaoList));
					}
					item.setDescendenteList(criarArvore(lista, uo.getDescendente().getSigla()));
					result.add(item);
				}
			}
		}
		return result;
	}

	@Override
	@Cacheable("colaboradorLista")
	public boolean executar(_Contexto contexto) throws Exception {
		List<ColaboradorListaDto> result = null;

		// ColaboradorListaCadFiltroDto filtro = (ColaboradorListaCadFiltroDto)
		// contexto.getRequisicao();

		List<Empregador> empregadorList = empregadorDao.findAll();

		if (!CollectionUtils.isEmpty(empregadorList)) {
			result = new ArrayList<>();
			for (Empregador empregador : empregadorList) {
				List<UnidadeOrganizacionalHierarquiaAtivaVi> unidadeOrganizacionalHierarquiaList = unidadeOrganizacionalHierarquiaAtivaViDao.findAllByAscendentePessoaJuridicaOrDescendentePessoaJuridica(empregador, empregador);

				montarArvore(result, empregador, unidadeOrganizacionalHierarquiaList);
			}
		}

		contexto.setResposta(result);
		
		return false;
	}

	private void montarArvore(List<ColaboradorListaDto> result, Empregador empregador, List<UnidadeOrganizacionalHierarquiaAtivaVi> unidadeOrganizacionalHierarquiaList) {

		ColaboradorListaDto registro = new ColaboradorListaDto();
		registro.setEmpregador(empregador.infoBasica());

		registro.setUnidadeOrganizacionalList(criarArvore(unidadeOrganizacionalHierarquiaList, "PRESI"));

		result.add(registro);
	}

}
