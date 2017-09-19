package br.gov.df.emater.aterwebsrv.bo.unidade_organizacional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.funcional.LotacaoAtivaViDao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Emprego;
import br.gov.df.emater.aterwebsrv.modelo.funcional.LotacaoAtivaVi;

@Service("UnidadeOrganizacionalEmpregadoPorUnidadeOrganizacionalCmd")
public class EmpregadoPorUnidadeOrganizacionalCmd extends _Comando {

	@Autowired
	private LotacaoAtivaViDao lotacaoAtivaViDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		List<Emprego> result = null;
		Integer[] unidadeOrganizacionalIdList = (Integer[]) contexto.getRequisicao();
		if (unidadeOrganizacionalIdList != null) {
			Arrays.sort(unidadeOrganizacionalIdList);
			Integer uoAnterior = null;
			for (Integer unidadeOrganizacionalId : unidadeOrganizacionalIdList) {
				if (!unidadeOrganizacionalId.equals(uoAnterior)) {					
					List<LotacaoAtivaVi> lotacaoAtivaViList = null;
					lotacaoAtivaViList = lotacaoAtivaViDao.findAllByUnidadeOrganizacionalId(unidadeOrganizacionalId);
					if (lotacaoAtivaViList.size() > 0) {
						if (result == null) {
							result = new ArrayList<>();
						}
						for (LotacaoAtivaVi lotacaoAtivaVi : lotacaoAtivaViList) {
							result.add(lotacaoAtivaVi.getEmprego().infoBasica());
						}
					}
					uoAnterior = unidadeOrganizacionalId;
				}
			}
		}
		contexto.setResposta(result);
		return false;
	}

}
