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
          
@Service("IndiceProducaoEditarArteCmd")
public class EditarArteCmd extends _SalvarCmd {

	@Autowired
	private IpaProducaoDao ipaProducaoDao;
	
	@Autowired
	private IpaProducaoBemClassificadoDao IpaProducaoBemClassificadoDao;
	
	@Autowired
	private IpaProducaoFormaDao IpaProducaoFormaDao;
	
	@Autowired
	private FormaProducaoValorDao formaDao;


	public EditarArteCmd() {
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
			
			ipaProducaoDao.save(ip);
			
// TABELA IPA PRODUCAO BEM CLASSIFICADO
			
				IpaProducaoBemClassificado ipbc = new IpaProducaoBemClassificado();
				BemClassificado bc = new BemClassificado();
				bc.setId(result.getBemClassificado().getId());
				ipbc.setBemClassificado(bc);
				ipbc.setId(IpaProducaoBemClassificadoDao.retornaId(ip));
				ipbc.setIpaProducao(ip);
				ipbc.setProducao(result.getProducao());
				ipbc.setQuantidadeProdutores(result.getQuantidadeProdutores());
				ipbc.setProdutividade(result.getProdutividade());
				ipbc.setValorUnitario(result.getValorUnitario());

				IpaProducaoBemClassificadoDao.save(ipbc);
		
		
// TABELA IPA PRODUCAO FORMA
		
//				---
				
				List<Integer> idForma = IpaProducaoFormaDao.retornaId(ip);
				
				Integer[] idpf = new Integer[3];
			
				int y = 0;
				for (Integer idForma2 : idForma) {
					idpf[y] = idForma2;
					y++;
				}
		
				IpaProducaoForma ipf1 = new IpaProducaoForma();

				ipf1.setBemClassificacao(result.getTipo());
				ipf1.setId(idpf[0]);
				ipf1.setIpaProducao(ip);
				ipf1.setOrdem(1);

				IpaProducaoFormaDao.save(ipf1);
				
				IpaProducaoForma ipf2 = new IpaProducaoForma();

				ipf2.setBemClassificacao(result.getCategoria());
				ipf2.setId(idpf[1]);
				ipf2.setIpaProducao(ip);
				ipf2.setOrdem(2);	
				IpaProducaoFormaDao.save(ipf2);
				
				IpaProducaoForma ipf = new IpaProducaoForma();
				ipf.setId(idpf[2]);
				
				result.getProducaoComposicaoList().forEach( (i) -> {
					ipf.setFormaProducaoValor(formaDao.retornaForma(i.getId()));
				});
				
				ipf.setIpaProducao(ip);
				ipf.setOrdem(3);
				IpaProducaoFormaDao.save(ipf);

		return false;	
		
	}
}