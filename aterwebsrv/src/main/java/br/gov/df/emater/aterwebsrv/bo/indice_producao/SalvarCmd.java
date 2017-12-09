package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo._SalvarCmd;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.FormaProducaoValorDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.IpaDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.IpaProducaoBemClassificadoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.IpaProducaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.IpaProducaoFormaDao;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.ProducaoGravaDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Ipa;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoBemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoForma;
          
@Service("IndiceProducaoSalvarCmd")
public class SalvarCmd extends _SalvarCmd {

	@Autowired
	private IpaDao dao;

	@Autowired
	private IpaProducaoDao ipaProducaoDao;
	
	@Autowired
	private IpaProducaoBemClassificadoDao IpaProducaoBemClassificadoDao;
	
	@Autowired
	private IpaProducaoFormaDao IpaProducaoFormaDao;
	
	@Autowired
	private FormaProducaoValorDao formaDao;

	private Integer ordem;

	public SalvarCmd() {
	}
	
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		
		final ProducaoGravaDto result = (ProducaoGravaDto) contexto.getRequisicao();
		
	for (IpaProducao iprod : result.getProducaoIpaList()) { //Insere Agricola
		
// TABELA IPA
		
		Ipa ipa2 = result.getIpa();
		
		Ipa ipa = new Ipa();

		ipa.setUnidadeOrganizacional(ipa2.getUnidadeOrganizacional());	
		ipa.setAno(ipa2.getAno());
		ipa.setPropriedadeRural(ipa2.getPropriedadeRural());
		ipa.setPublicoAlvo(ipa2.getPublicoAlvo());
		
		dao.save(ipa);
		
// TABELA IPA PRODUCAO

		List<IpaProducao> ipArrayList = new ArrayList<>();

		ipArrayList.add(iprod);
		
		IpaProducao ip = new IpaProducao();
		
		ipArrayList.forEach((i) -> {
			
			ip.setArea(i.getArea());
			ip.setIpaId(ipa);
			
			if(i.getMatriz() == null){
				ip.setMatriz(0);
			}else{
				ip.setMatriz(i.getMatriz());
			}
			if(i.getRebanho() == null){
				ip.setRebanho(0);
			}else{
				ip.setRebanho(i.getRebanho());
			}
	
		});
		
		ipaProducaoDao.save(ip);
		
// TABELA IPA PRODUCAO BEM CLASSIFICADO
		
		for (IpaProducaoBemClassificado b : result.getProducaoAgricolaList()) {
		System.out.println(b.getId());
			if(b.getId() == iprod.getId()){
			
				IpaProducaoBemClassificado ipbc = new IpaProducaoBemClassificado();
				
				ipbc.setBemClassificado(b.getBemClassificado());
				ipbc.setIpaProducao(ip);
				ipbc.setProducao(b.getProducao());
				ipbc.setProdutividade(b.getProdutividade());
				ipbc.setQuantidadeProdutores(b.getQuantidadeProdutores());
				ipbc.setValorUnitario(b.getValorUnitario());
				ipbc.setValorTotal(100f);
	
				IpaProducaoBemClassificadoDao.save(ipbc);
			}
		}
			
// TABELA IPA PRODUCAO FORMA
		
		List<IpaForma> iff = result.getProducaoComposicaoList();
		ordem = 0;
		iff.forEach( (a) -> {
			a.getProducaoComposicaoList().forEach( (b) -> {
				
				IpaProducaoForma ipf = new IpaProducaoForma();
				
				ipf.setFormaProducaoValor(formaDao.retornaForma(b.getId()));
				ipf.setIpaProducao(ip);
				ipf.setOrdem(++ordem);
				
				IpaProducaoFormaDao.save(ipf);
					
			});
		});
			
		}
		
		return false;
		
		
	}
}