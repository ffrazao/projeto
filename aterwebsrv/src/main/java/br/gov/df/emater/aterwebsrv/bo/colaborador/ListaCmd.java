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
import br.gov.df.emater.aterwebsrv.dto.funcional.UnidadeOrganizacionalHierarquiaDto;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Empregador;
import br.gov.df.emater.aterwebsrv.modelo.funcional.LotacaoAtivaVi;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacionalHierarquiaAtivaVi;

@Service("ColaboradorListaCmd")
public class ListaCmd extends _Comando {

	@Autowired
	private EmpregadorDao empregadorDao;

	@Autowired
	private UnidadeOrganizacionalHierarquiaAtivaViDao unidadeOrganizacionalHierarquiaAtivaViDao;

	@Autowired
	private LotacaoAtivaViDao lotacaoAtivaViDao;

	@Override
	@Cacheable("colaboradorLista")
	public boolean executar(_Contexto contexto) throws Exception {
		List<UnidadeOrganizacionalHierarquiaDto> result = new ArrayList<>();

		List<Empregador> empregadorList = empregadorDao.findAll();
		if (!CollectionUtils.isEmpty(empregadorList)) {
			for (Empregador empregador : empregadorList) {
				montarArvore(result, empregador, null);
			}
		}

		contexto.setResposta(result);
		return false;
	}

	private void montarArvore(List<UnidadeOrganizacionalHierarquiaDto> result, Empregador empregador, UnidadeOrganizacional unidadeOrganizacional) {
		// TODO Auto-generated method stub
		Integer id = 0;
		List<UnidadeOrganizacionalHierarquiaAtivaVi> unidadeOrganizacionalHierarquiaList = unidadeOrganizacionalHierarquiaAtivaViDao.findAllByAscendentePessoaJuridicaOrDescendentePessoaJuridica(empregador, empregador);
		if (!CollectionUtils.isEmpty(unidadeOrganizacionalHierarquiaList)) {
			for (UnidadeOrganizacionalHierarquiaAtivaVi uo : unidadeOrganizacionalHierarquiaList) {
				UnidadeOrganizacionalHierarquiaDto unidadeOrganizacionalHierarquiaDto = new UnidadeOrganizacionalHierarquiaDto();
				unidadeOrganizacionalHierarquiaDto.setId(++id);
				unidadeOrganizacionalHierarquiaDto.setId(++id);
				// captar pessoas lotadas
				List<LotacaoAtivaVi> lotacaoList = lotacaoAtivaViDao.findAllByUnidadeOrganizacionalId(uo.getId());
				if (!CollectionUtils.isEmpty(lotacaoList)) {
					for (LotacaoAtivaVi lotacao : lotacaoList) {

					}
				}

			}
		}

	}

}
