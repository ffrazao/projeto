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
import br.gov.df.emater.aterwebsrv.dto.indice_producao.ProducaoCadDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoValor;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Ipa;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoBemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoForma;
          
@Service("IndiceProducaoEditarCmd")
public class EditarCmd extends _SalvarCmd {

	@Autowired
	private IpaProducaoDao ipaProducaoDao;
	
	@Autowired
	private IpaProducaoBemClassificadoDao IpaProducaoBemClassificadoDao;
	
	@Autowired
	private IpaProducaoFormaDao IpaProducaoFormaDao;

	private Integer ordem;

	private int cont;

	public EditarCmd() {
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
			
			if(ip.getMatriz() == null){
				ip.setMatriz(0f);
			}else{
				ip.setMatriz(result.getMatriz());
			}
			if(ip.getRebanho() == null){
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
//				ipbc.setValorTotal(100f);
//	
				IpaProducaoBemClassificadoDao.save(ipbc);
		
		
// TABELA IPA PRODUCAO FORMA
		
		List<IpaProducaoForma> lif1 = result.getProducaoComposicaoList(); //Tem id do forma produto valor
		
		List<Integer> idForma = IpaProducaoFormaDao.retornaId(ip);
		Integer[] idpf = new Integer[3];
	
		int y = 0;
		for (Integer idForma2 : idForma) {
			idpf[y] = idForma2;
			y++;
		}
		
		ordem = 0;
		cont = 0;
		lif1.forEach( (a) -> {
			
			IpaProducaoForma ipf = new IpaProducaoForma();
			FormaProducaoValor fpv = new FormaProducaoValor();
	
			fpv.setId(a.getId());
			ipf.setId(idpf[cont]);
			ipf.setFormaProducaoValor(fpv);
			ipf.setIpaProducao(ip);
			ipf.setOrdem(++ordem);
			
		IpaProducaoFormaDao.save(ipf);
		cont++;
		
		});
		
		return false;	
		
	}
}