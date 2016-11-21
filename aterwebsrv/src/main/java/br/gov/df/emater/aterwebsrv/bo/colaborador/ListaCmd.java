package br.gov.df.emater.aterwebsrv.bo.colaborador;

import java.util.ArrayList;
import java.util.Arrays;
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
import br.gov.df.emater.aterwebsrv.dto.funcional.OrganogramaDto;
import br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Empregador;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacionalHierarquiaAtivaVi;

@Service("ColaboradorListaCmd")
public class ListaCmd extends _Comando {

	@Autowired
	private EmpregadorDao empregadorDao;

	@Autowired
	private LotacaoAtivaViDao lotacaoAtivaViDao;

	@Autowired
	private UnidadeOrganizacionalHierarquiaAtivaViDao unidadeOrganizacionalHierarquiaAtivaViDao;

	@Override
	@Cacheable("colaboradorLista")
	public boolean executar(_Contexto contexto) throws Exception {
		List<ColaboradorListaDto> result = null;

		// ColaboradorListaCadFiltroDto filtro = (ColaboradorListaCadFiltroDto)
		// contexto.getRequisicao();

		List<Empregador> empregadorList = empregadorDao.findAll();
		if (!CollectionUtils.isEmpty(empregadorList)) {
			result = new ArrayList<>();
			String siglaUoPrincipal = "PRESI";
			for (Empregador empregador : empregadorList) {
				List<UnidadeOrganizacionalHierarquiaAtivaVi> unidadeOrganizacionalHierarquiaList = unidadeOrganizacionalHierarquiaAtivaViDao.findAllByAscendentePessoaJuridicaOrDescendentePessoaJuridica(empregador, empregador);
				if (!CollectionUtils.isEmpty(unidadeOrganizacionalHierarquiaList)) {
					ColaboradorListaDto registro = new ColaboradorListaDto();
					registro.setEmpregador(empregador.infoBasica());
					List<OrganogramaDto> organogramaList = montarOrganograma(unidadeOrganizacionalHierarquiaList, siglaUoPrincipal);
					// verificar se é necessário inserir o elemento raiz
					if (siglaUoPrincipal != null) {
						for (UnidadeOrganizacionalHierarquiaAtivaVi uo : unidadeOrganizacionalHierarquiaList) {
							if ((uo.getAscendente() == null && siglaUoPrincipal == null) || (uo.getAscendente() != null && uo.getAscendente().getApelidoSigla().equals(siglaUoPrincipal))) {
								OrganogramaDto item = new OrganogramaDto();
								item.setUnidadeOrganizacional(uo.getAscendente().infoBasica());
								item.setDescendenteList(organogramaList);
								item.setLotacaoList(UtilitarioInfoBasica.infoBasicaList(lotacaoAtivaViDao.findAllByUnidadeOrganizacionalId(item.getUnidadeOrganizacional().getId())));
								organogramaList = Arrays.asList(new OrganogramaDto[] { item });
								break;
							}
						}
					}
					registro.setOrganogramaList(organogramaList);
					result.add(registro);
				}
			}
		}

		contexto.setResposta(result);

		return false;
	}

	private List<OrganogramaDto> montarOrganograma(List<UnidadeOrganizacionalHierarquiaAtivaVi> lista, String sigla) {
		List<OrganogramaDto> result = null;
		if (!CollectionUtils.isEmpty(lista)) {
			for (UnidadeOrganizacionalHierarquiaAtivaVi uo : lista) {
				if ((uo.getAscendente() == null && sigla == null) || (uo.getAscendente() != null && uo.getAscendente().getApelidoSigla().equals(sigla))) {
					if (result == null) {
						result = new ArrayList<>();
					}
					OrganogramaDto item = new OrganogramaDto();
					item.setUnidadeOrganizacional(uo.getDescendente().infoBasica());
					item.setLotacaoList(UtilitarioInfoBasica.infoBasicaList(lotacaoAtivaViDao.findAllByUnidadeOrganizacionalId(item.getUnidadeOrganizacional().getId())));
					item.setDescendenteList(montarOrganograma(lista, item.getUnidadeOrganizacional().getApelidoSigla()));
					result.add(item);
				}
			}
		}
		return result;
	}

}
