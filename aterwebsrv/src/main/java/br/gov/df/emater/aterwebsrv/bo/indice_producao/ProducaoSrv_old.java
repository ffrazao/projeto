package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoComposicaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.ProducaoCadDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;

//@Service("ProducaoSrv")
public class ProducaoSrv_old {
//
//	@Autowired
//	private ProducaoDao daoP;
//
//	@Autowired
//	private ProducaoComposicaoDao daoPc;
//
//	@Cacheable("Producao")
	public Object geraProducao(ProducaoProprietario producaoProprietario ) {
//		
		ProducaoCadDto result = new ProducaoCadDto();
//	    List<Producao> producaoList = new ArrayList<>();
//	    List<Producao> producaoAgricolaList = new ArrayList<>();;
//	    List<Producao> producaoFloriculturaList = new ArrayList<>();;
//	    List<Producao> producaoAnimalList = new ArrayList<>();;
//	    List<Producao> producaoNaoAgricolaList = new ArrayList<>();;
//	    
//	    producaoList = daoP.findByAnoUnidadeOrganizacional(producaoProprietario.getAno(), producaoProprietario.getUnidadeOrganizacional().getId());
//		producaoAgricolaList = new ArrayList<>();
//		producaoFloriculturaList = new ArrayList<>();
//		producaoAnimalList = new ArrayList<>();
//		producaoNaoAgricolaList = new ArrayList<>();
//
//		if (producaoList != null) {
//			
//			for ( Producao producao : producaoList) {
//				producao.setProducaoComposicaoList(daoPc.findAllByProducao(producao));
//				switch (producao.getBemClassificado().getTipo()) { 
//	             	case "1":  producaoAgricolaList.add(producao);      break;
//	             	case "2":  producaoFloriculturaList.add(producao);  break;
//	             	case "3":  producaoAnimalList.add(producao);        break;
//	             	case "4":  producaoNaoAgricolaList.add(producao);   break;
//				}
//			}
//		}
//		
//		result.setProducaoList(producaoList);
//		result.setProducaoAgricolaList(producaoAgricolaList);
//		result.setProducaoFloriculturaList(producaoFloriculturaList);
//		result.setProducaoAnimalList(producaoAnimalList);
//		result.setProducaoNaoAgricolaList(producaoNaoAgricolaList);

		return result;
	}
//
//
}
