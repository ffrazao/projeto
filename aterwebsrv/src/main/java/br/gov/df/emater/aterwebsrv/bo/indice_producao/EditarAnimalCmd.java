package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo._SalvarCmd;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.FormaProducaoValorDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.IpaProducaoBemClassificadoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.IpaProducaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.IpaProducaoFormaDao;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.ProducaoCadDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Ipa;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoBemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoForma;
          
@Service("IndiceProducaoEditarAnimalCmd")
public class EditarAnimalCmd extends _SalvarCmd {

	@Autowired
	private IpaProducaoDao ipaProducaoDao;
	
	@Autowired
	private IpaProducaoBemClassificadoDao IpaProducaoBemClassificadoDao;
	
	@Autowired
	private FormaProducaoValorDao formaDao;

	@Autowired
	private IpaProducaoFormaDao IpaProducaoFormaDao;

	private int cont;

	private int ordem;

	public EditarAnimalCmd() {
	}
	
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		
		final ProducaoCadDto result = (ProducaoCadDto) contexto.getRequisicao();
		
// TABELA IPA
		
		Ipa ipa2 = result.getIpa();
	
// TABELA IPA PRODUCAO

		IpaProducao ip = new IpaProducao();
		ip.setId(result.getId());
		ip.setArea(result.getArea());
		ip.setIpaId(ipa2);
			
			if(result.getMatriz() == null){
				ip.setMatriz(0);
			}else{
				ip.setMatriz(result.getMatriz());
			}
			if(result.getRebanho() == null){
				ip.setRebanho(0);
			}else{
				ip.setRebanho(result.getRebanho());
			}
			
			if(result.getCultura() == null){
				ip.setCultura(null);
			}else{
				ip.setCultura(result.getCultura());
			}
			
			ipaProducaoDao.save(ip);
			
// TABELA IPA PRODUCAO BEM CLASSIFICADO		
			List<IpaProducaoBemClassificado> ListaAnimalBem =  result.getProdutoList();
			
			for (IpaProducaoBemClassificado bemClassificado : ListaAnimalBem) {
				
				IpaProducaoBemClassificado ipbc = new IpaProducaoBemClassificado();
				BemClassificado bc = new BemClassificado();
				bc.setId(bemClassificado.getBemClassificado().getId());
				
				ipbc.setBemClassificado(bc);
				ipbc.setId(bemClassificado.getId());
				ipbc.setIpaProducao(ip);
				ipbc.setProducao(bemClassificado.getProducao());
				ipbc.setQuantidadeProdutores(result.getQuantidadeProdutores());
				ipbc.setValorUnitario(bemClassificado.getValorUnitario());
				IpaProducaoBemClassificadoDao.save(ipbc);
		}

		
		List<Integer> idForma = IpaProducaoFormaDao.retornaId(ip);
		
		Integer[] idpf = new Integer[3];
	
		int y = 0;
		for (Integer idForma2 : idForma) {
			idpf[y] = idForma2;
			y++;
		}
		cont = 0;
		ordem = 0;
		result.getProducaoComposicaoList().forEach( (f) -> {
			
			System.out.println(f.getFormaProducaoValor().getId());
			System.out.println(f.getFormaProducaoValor().getNome());	
			
			System.out.println(idpf[cont]);
			
			IpaProducaoForma ipf = new IpaProducaoForma();
			ipf.setId(idpf[cont]);
			ipf.setFormaProducaoValor(formaDao.retornaForma(f.getFormaProducaoValor().getId()));
			ipf.setIpaProducao(ip);
			ipf.setOrdem(++ordem);
			
			IpaProducaoFormaDao.save(ipf);
			cont++;
		});
		
		
//		---
		
		return false;	
		
	}
}