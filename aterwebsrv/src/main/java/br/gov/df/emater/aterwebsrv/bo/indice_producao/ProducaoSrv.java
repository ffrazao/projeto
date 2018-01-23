package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.dao.indice_producao.IpaProducaoBemClassificadoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.IpaProducaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.IpaProducaoFormaDao;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.ProducaoCadDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Ipa;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducao;

@Service("ProducaoSrv")
public class ProducaoSrv {
	
	@Autowired
	private IpaProducaoDao IpaProducaoDao;
	
	@Autowired
	private IpaProducaoFormaDao IpaProducaoFormaDao;
	
	@Autowired
	private IpaProducaoBemClassificadoDao IpaProducaoBemClassificadoDao;
	
	//@Cacheable("IpaProducao")
	public Object geraProducao(Ipa ipa) {
	
		ProducaoCadDto result = new ProducaoCadDto();

		List<IpaProducao> producaoList = null;
		List<IpaProducao> producaoAgricolaList = new ArrayList<>();
		List<IpaProducao> producaoFloriculturaList = new ArrayList<>();
		List<IpaProducao> producaoArtesanatoList = new ArrayList<>();
		List<IpaProducao> producaoAgroindustriaList= new ArrayList<>();
		List<IpaProducao> producaoAnimalList= new ArrayList<>();
		
		if(ipa.getPublicoAlvo() != null){
			if(ipa.getUnidadeOrganizacional() != null){
				producaoList = IpaProducaoDao.findByIpaProd(ipa.getPublicoAlvo().getId(), ipa.getPropriedadeRural().getId(), ipa.getUnidadeOrganizacional().getId(), ipa.getAno());
			}else {
				producaoList = IpaProducaoDao.findByIpaProdSemUO(ipa.getPublicoAlvo().getId(), ipa.getPropriedadeRural().getId(), ipa.getAno());
			}
		}else{			
			producaoList = IpaProducaoDao.findByIpaProducao(ipa.getAno(), ipa.getUnidadeOrganizacional().getId());
		}

		if (producaoList != null){
			for (IpaProducao ipaProducao : producaoList) {
				//ipaProducao.setIpaProducaoList(IpaProducaoDao.findByIp(ipaProducao.getId()));
				ipaProducao.setIpaProducaoBemClassificadoList(IpaProducaoBemClassificadoDao.findAllByIpaProducaoBemClassificado(ipaProducao));
				ipaProducao.setIpaProducaoFormaList(IpaProducaoFormaDao.ListaIpaProducaoForma(ipaProducao));
			//	producaoAgricolaList.add(ipaProducao);
				
				ipaProducao.getIpaProducaoBemClassificadoList().forEach( (i) -> {
					
					switch (i.getBemClassificado().getTipo()) {
             		case "1":  producaoAgricolaList.add(ipaProducao);      break;
             		case "2":  producaoFloriculturaList.add(ipaProducao);  break;
             		case "3":  producaoAgroindustriaList.add(ipaProducao);   break;
             		case "4":  producaoArtesanatoList.add(ipaProducao);   break;
             		case "5":  producaoAnimalList.add(ipaProducao);   break;
				}
					
				});
				
			}
		}
			
		result.setProducaoAgricolaList(producaoAgricolaList);
		result.setProducaoFloriculturaList(producaoFloriculturaList);
		result.setProducaoArtesanatoList(producaoArtesanatoList);
		result.setProducaoAgroindustriaList(producaoAgroindustriaList);
		result.setProducaoAnimalList(producaoAnimalList);
		
		return result;
		
	}
}