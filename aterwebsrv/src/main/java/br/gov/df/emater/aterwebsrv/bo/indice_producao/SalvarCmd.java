package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo._SalvarCmd;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.IpaDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.IpaProducaoBemClassificadoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.IpaProducaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.IpaProducaoFormaDao;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.ProducaoGravaDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Ipa;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducao;
          
@Service("IndiceProducaoSalvarCmd")
public class SalvarCmd extends _SalvarCmd {

	@Autowired
	private IpaDao dao;
	@Autowired
	private IpaProducaoDao ipaProducaoDao;
	@Autowired
	private IpaProducaoBemClassificadoDao ipaProducaoBemClassificadoDao;
	@Autowired
	private IpaProducaoFormaDao ipaProducaoFormaDao;

	public SalvarCmd() {
	}
	
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		
	final ProducaoGravaDto result = (ProducaoGravaDto) contexto.getRequisicao();
	List<IpaProducao> ipFlor = (List<IpaProducao>) result.getProducaoFloriculturaList();
	List<IpaProducao> ipAgri = (List<IpaProducao>) result.getProducaoIpaList();
	List<IpaProducao> ipArte = (List<IpaProducao>) result.getProducaoArtesanatoList();
	List<IpaProducao> ipAgro = (List<IpaProducao>) result.getProducaoAgroindustriaList();
	List<IpaProducao> ipAnimal = (List<IpaProducao>) result.getProducaoAnimalList();
		
// TABELA IPA
	
		Ipa ipaResult = result.getIpa();
		Ipa ipa = new Ipa();
		
		if((ipaResult.getPropriedadeRural() == null) || (ipaResult.getPublicoAlvo() == null)){
			Ipa rs = dao.findOneByAnoAndUnidadeOrganizacionalAndPropriedadeRuralIsNullAndPublicoAlvoIsNull(ipaResult.getAno(), ipaResult.getUnidadeOrganizacional());	
			if (rs != null){
				ipa.setId(rs.getId());
			}
			
		}else if((ipaResult.getPropriedadeRural() != null) || (ipaResult.getPublicoAlvo() != null)){
			Ipa rs = dao.retornaId(ipaResult.getAno(), ipaResult.getUnidadeOrganizacional(), ipaResult.getPropriedadeRural(), ipaResult.getPublicoAlvo());
			if (rs != null){
				ipa.setId(rs.getId());
				System.out.println("tem rs 2: " + rs.getId());
			}
		}
		
		ipa.setUnidadeOrganizacional(ipaResult.getUnidadeOrganizacional());	
		ipa.setAno(ipaResult.getAno());
		ipa.setPropriedadeRural(ipaResult.getPropriedadeRural());
		ipa.setPublicoAlvo(ipaResult.getPublicoAlvo());
		
		dao.save(ipa);

		for(IpaProducao ipaFlor : ipFlor){
			SalvarFlorCmd sflor = new SalvarFlorCmd();
			sflor.salvarFlor(ipaFlor, ipa, result);
			ipaProducaoDao.save(sflor.getIpaProdFlor());
			ipaProducaoBemClassificadoDao.save(sflor.getIpaBemFlor());
			ipaProducaoFormaDao.save(sflor.getIpaFormaFlor());
		}
		
		for(IpaProducao ipaAgri : ipAgri){
			SalvarAgriCmd sagri = new SalvarAgriCmd();
			sagri.salvarAgri(ipaAgri, ipa, result);
			ipaProducaoDao.save(sagri.getIpaProdAgri());
			ipaProducaoBemClassificadoDao.save(sagri.getIpaBemAgri());
			ipaProducaoFormaDao.save(sagri.getIpaFormaAgri());
		}
		
		for(IpaProducao ipaArte:  ipArte){
			SalvarArteCmd sArte = new SalvarArteCmd();
			sArte.salvarArte(ipaArte, ipa, result);
			ipaProducaoDao.save(sArte.getIpaProdArte());
			ipaProducaoBemClassificadoDao.save(sArte.getIpaBemArte());
			ipaProducaoFormaDao.save(sArte.getIpaFormaArte());
		}
		
		for(IpaProducao ipaAgro:  ipAgro){
			SalvarAgroCmd sAgro = new SalvarAgroCmd();
			sAgro.salvarAgro(ipaAgro, ipa, result);
			ipaProducaoDao.save(sAgro.getIpaProdAgro());
			ipaProducaoBemClassificadoDao.save(sAgro.getIpaBemAgro());
			ipaProducaoFormaDao.save(sAgro.getIpaFormaAgro());
		}
		
		for(IpaProducao ipaAnimal:  ipAnimal){
			SalvarAnimalCmd sAnimal = new SalvarAnimalCmd();
			sAnimal.salvarAnimal(ipaAnimal, ipa, result);
			ipaProducaoDao.save(sAnimal.getIpaProdAnimal());
			ipaProducaoBemClassificadoDao.save(sAnimal.getIpaBemAnimal());
			ipaProducaoFormaDao.save(sAnimal.getIpaFormaAnimal());
		}
		
		return false;
			
	}

}