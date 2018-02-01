package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.ArrayList;
import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.indice_producao.ProducaoGravaDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoValor;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Ipa;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoBemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoForma;
          
public class SalvarFlorCmd {
	
	private IpaProducao ipaProdFlor;
	private IpaProducaoBemClassificado ipaBemFlor;
	private List<IpaProducaoForma> ipaFormaFlor;

	private Integer ordem;

	public SalvarFlorCmd() {
	}
	
	public void salvarFlor(IpaProducao ipflor, Ipa ipa, ProducaoGravaDto result) throws Exception {
		
// TABELA IPA PRODUCAO
		
		IpaProducao ip = new IpaProducao();

			ip.setArea(ipflor.getArea());
			ip.setIpaId(ipa);
			
			if(ipflor.getMatriz() == null){
				ip.setMatriz(0f);
			}else{
				ip.setMatriz(ipflor.getMatriz());
			}
			if(ipflor.getRebanho() == null){
				ip.setRebanho(0);
			}else{
				ip.setRebanho(ipflor.getRebanho());
			}
		
			this.setIpaProdFlor(ip);
		
// TABELA IPA PRODUCAO BEM CLASSIFICADO
					
				IpaProducaoBemClassificado ipbc = new IpaProducaoBemClassificado();
				
				ipbc.setBemClassificado(ipflor.getBemClassificado());
				ipbc.setIpaProducao(ip);
				ipbc.setProducao(ipflor.getProducao());
				ipbc.setProdutividade(ipflor.getProdutividade());
				ipbc.setQuantidadeProdutores(ipflor.getQuantidadeProdutores());
				ipbc.setValorUnitario(ipflor.getValorUnitario());
	
				this.setIpaBemFlor(ipbc);

		
// TABELA IPA PRODUCAO FORMA	
		
		List<IpaForma> iff = ipflor.getProducaoComposicaoList();
		ordem = 0;

		ArrayList<IpaProducaoForma> list = new ArrayList<>();
		
		for (IpaForma ipaForma : iff) {
			
				IpaProducaoForma ipf = new IpaProducaoForma();
				FormaProducaoValor fpv = new FormaProducaoValor();
				
				fpv.setId(ipaForma.getId());
				ipf.setFormaProducaoValor(fpv);
				ipf.setIpaProducao(ip);
				ipf.setOrdem(++ordem);
				
				list.add(ipf);	
			
		}
		this.setIpaFormaFlor(list);	
		
	}
	
	public IpaProducao getIpaProdFlor() {
		return ipaProdFlor;
	}

	public void setIpaProdFlor(IpaProducao ipaProdFlor) {
		this.ipaProdFlor = ipaProdFlor;
	}

	public IpaProducaoBemClassificado getIpaBemFlor() {
		return ipaBemFlor;
	}

	public void setIpaBemFlor(IpaProducaoBemClassificado ipaBemFlor) {
		this.ipaBemFlor = ipaBemFlor;
	}

	public List<IpaProducaoForma> getIpaFormaFlor() {
		return ipaFormaFlor;
	}

	public void setIpaFormaFlor(List<IpaProducaoForma> ipaFormaFlor) {
		this.ipaFormaFlor = ipaFormaFlor;
	}
}