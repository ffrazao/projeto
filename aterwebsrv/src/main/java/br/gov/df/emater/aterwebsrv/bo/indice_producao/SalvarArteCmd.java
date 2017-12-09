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
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoBemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoForma;
          
@Service("IndiceProducaoSalvarArteCmd")
public class SalvarArteCmd extends _SalvarCmd {

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

	public SalvarArteCmd() {
	}
	
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		
		final ProducaoGravaDto result = (ProducaoGravaDto) contexto.getRequisicao();
		
		List<IpaProducao> ipArte = (List<IpaProducao>) result.getProducaoArtesanatoList();
		
	for (IpaProducao iprod : ipArte) { 
		
		
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
					
				IpaProducaoBemClassificado ipbc = new IpaProducaoBemClassificado();
				
				ipbc.setBemClassificado(iprod.getBemClassificado());
				ipbc.setIpaProducao(ip);
				ipbc.setProducao(iprod.getProducao());
				ipbc.setProdutividade(iprod.getProdutividade());
				ipbc.setQuantidadeProdutores(iprod.getQuantidadeProdutores());
				ipbc.setValorUnitario(iprod.getValorUnitario());
	
				IpaProducaoBemClassificadoDao.save(ipbc);

		
// TABELA IPA PRODUCAO FORMA
				
				IpaProducaoForma ipf1 = new IpaProducaoForma();

				ipf1.setBemClassificacao(iprod.getTipo());
				ipf1.setIpaProducao(ip);
				ipf1.setOrdem(1);
				IpaProducaoFormaDao.save(ipf1);
				
				IpaProducaoForma ipf2 = new IpaProducaoForma();

				ipf2.setBemClassificacao(iprod.getCategoria());
				ipf2.setIpaProducao(ip);
				ipf2.setOrdem(2);	
				IpaProducaoFormaDao.save(ipf2);
	
		iprod.getProducaoComposicaoList().forEach( (f) -> {
					
			IpaProducaoForma ipf = new IpaProducaoForma();
					
			System.out.println(f.getId());

			ipf.setFormaProducaoValor(formaDao.retornaForma(f.getId()));
			ipf.setIpaProducao(ip);
			ipf.setOrdem(3);
					
			IpaProducaoFormaDao.save(ipf);
					
		});
			
		}
		
		return false;
		
		
	}
}