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
import br.gov.df.emater.aterwebsrv.dao.funcional.UnidadeOrganizacionalAtivaViDao;
import br.gov.df.emater.aterwebsrv.dao.funcional.UnidadeOrganizacionalHierarquiaAtivaViDao;
import br.gov.df.emater.aterwebsrv.dto.funcional.UnidadeOrganizacionalHierarquiaDto;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Empregador;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Lotacao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacionalHierarquia;

@Service("ColaboradorListaCmd")
public class ListaCmd extends _Comando {

	@Autowired
	private EmpregadorDao empregadorDao;

	@Autowired
	private UnidadeOrganizacionalAtivaViDao unidadeOrganizacionalAtivaViDao;

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
		List<UnidadeOrganizacionalHierarquia> unidadeOrganizacionalHierarquiaList = 
		unidadeOrganizacionalHierarquiaAtivaViDao.findAllByAscendentePessoaJuridicaOrDescendentePessoaJuridica(empregador);
		if (!CollectionUtils.isEmpty(unidadeOrganizacionalList)) {
			for (UnidadeOrganizacional unidadeOrganizacional : unidadeOrganizacionalList) {
				UnidadeOrganizacionalHierarquiaDto unidadeOrganizacionalHierarquiaDto = new UnidadeOrganizacionalHierarquiaDto();
				unidadeOrganizacionalHierarquiaDto.setId(++id);
				unidadeOrganizacionalHierarquiaDto.setId(++id);
				// captar pessoas lotadas
				List<Lotacao> lotacaoList = lotacaoAtivaViDao.findAllByUnidadeOrganizacional(unidadeOrganizacional);
				if (!CollectionUtils.isEmpty(lotacaoList)) {
					for (Lotacao lotacao : lotacaoList) {
						
					}
				}
				
			}
		}
		
	}

}
