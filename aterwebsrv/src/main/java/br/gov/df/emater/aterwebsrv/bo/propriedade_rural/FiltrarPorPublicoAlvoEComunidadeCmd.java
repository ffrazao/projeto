package br.gov.df.emater.aterwebsrv.bo.propriedade_rural;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.ComunidadeDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PublicoAlvoPropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.dto.PropriedadeRuralPorComunidadePublicoAlvoDto;

@Service("PropriedadeRuralFiltrarPorPublicoAlvoEComunidadeCmd")
public class FiltrarPorPublicoAlvoEComunidadeCmd extends _Comando {

	@Autowired
	private PublicoAlvoPropriedadeRuralDao dao;
	
	@Autowired
	private PublicoAlvoDao publicoAlvoDao;

	@Autowired
	private ComunidadeDao comunidadeDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		List<Object[]> result = null;
		PropriedadeRuralPorComunidadePublicoAlvoDto requisicao = (PropriedadeRuralPorComunidadePublicoAlvoDto) contexto.getRequisicao();
		
		Comunidade comunidade = comunidadeDao.findOne(requisicao.getComunidade().getId());

		// percorrer o publico alvo informado
		for (PublicoAlvo publicoAlvo : requisicao.getPublicoAlvoList()) {
			PublicoAlvo pa = publicoAlvoDao.findOneByPessoa(publicoAlvo.getPessoa());
			List<PublicoAlvoPropriedadeRural> publicoAlvoPropriedadeRuralList = dao.findByComunidadeAndPublicoAlvoAndPropriedadeRuralIdIsNotNull(comunidade, pa);
			List<Object> linha = new ArrayList<Object>();
			linha.add(comunidade.getNome());
			linha.add(pa.getPessoa().getNome());
			List<PropriedadeRural> propriedadeRuralList = new ArrayList<PropriedadeRural>();
			if (publicoAlvoPropriedadeRuralList == null) {
				linha.add(null);
			} else {
				for (PublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural : publicoAlvoPropriedadeRuralList) {
					propriedadeRuralList.add(publicoAlvoPropriedadeRural.getPropriedadeRural().infoBasica());
				}
				linha.add(propriedadeRuralList.toArray());
			}

			if (result == null) {
				result = new ArrayList<Object[]>();
			}
			result.add(linha.toArray());
		}
		contexto.setResposta(result);
		return false;
	}

}
