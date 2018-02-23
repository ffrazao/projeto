package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.ArrayList;
import java.util.List;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.ProducaoGravaDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoItem;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoValor;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Ipa;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoBemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoForma;
          
public class SalvarAgriCmd{

	private Integer ordem;
	
	private IpaProducao ipaProdAgri;
	private IpaProducaoBemClassificado ipaBemAgri;
	private List<IpaProducaoForma> ipaFormaAgri;

	public SalvarAgriCmd() {
	}
	
	public void salvarAgri(IpaProducao ipAgri, Ipa ipa, ProducaoGravaDto result) throws Exception {
				
// TABELA IPA PRODUCAO

		List<IpaProducao> ipArrayList = new ArrayList<>();

		ipArrayList.add(ipAgri);
		
		IpaProducao ip = new IpaProducao();
		
		ipArrayList.forEach((i) -> {
			
			ip.setArea(i.getArea());
			ip.setIpaId(ipa);
			
			if(i.getMatriz() == null){
				ip.setMatriz(0f);
			}else{
				ip.setMatriz(i.getMatriz());
			}
			if(i.getRebanho() == null){
				ip.setRebanho(0);
			}else{
				ip.setRebanho(i.getRebanho());
			}
	
		});
		
		this.setIpaProdAgri(ip);
		
// TABELA IPA PRODUCAO BEM CLASSIFICADO
		
		for (IpaProducaoBemClassificado b : result.getProducaoAgricolaList()) {
			if(b.getId() == ipAgri.getId()){
			
				IpaProducaoBemClassificado ipbc = new IpaProducaoBemClassificado();
				
				if(b.getBemClassificado() == null){
					throw new BoException("Favor inserir a Cultura!");
				}
				
				
				ipbc.setBemClassificado(b.getBemClassificado());
				ipbc.setIpaProducao(ip);
				ipbc.setProducao(b.getProducao());
				ipbc.setProdutividade(b.getProdutividade());
				ipbc.setQuantidadeProdutores(b.getQuantidadeProdutores());
				ipbc.setValorUnitario(b.getValorUnitario());
	
				this.setIpaBemAgri(ipbc);
			}
		}
			
// TABELA IPA PRODUCAO FORMA

		List<IpaForma> iff = result.getProducaoComposicaoList();
		ordem = 0;
		ArrayList<IpaProducaoForma> list = new ArrayList<>();
		
		for (IpaForma ipaForma : iff) {
			
				for (FormaProducaoItem iForma : ipaForma.getProducaoComposicaoList()) {
					
					if(ipAgri.getId() == ipaForma.getId()){
						IpaProducaoForma ipf = new IpaProducaoForma();
						FormaProducaoValor fpv = new FormaProducaoValor();
						
							fpv.setId(iForma.getId());
							ipf.setFormaProducaoValor(fpv);
							ipf.setIpaProducao(ip);
							ipf.setOrdem(++ordem);
							list.add(ipf);
					}					
			}	
			
			this.setIpaFormaAgri(list);
		}
		
		
				
	}
	
	public IpaProducao getIpaProdAgri() {
		return ipaProdAgri;
	}

	public void setIpaProdAgri(IpaProducao ipaProdAgri) {
		this.ipaProdAgri = ipaProdAgri;
	}

	public IpaProducaoBemClassificado getIpaBemAgri() {
		return ipaBemAgri;
	}

	public void setIpaBemAgri(IpaProducaoBemClassificado ipaBemAgri) {
		this.ipaBemAgri = ipaBemAgri;
	}

	public List<IpaProducaoForma> getIpaFormaAgri() {
		return ipaFormaAgri;
	}

	public void setIpaFormaAgri(List<IpaProducaoForma> ipaFormaAgri) {
		this.ipaFormaAgri = ipaFormaAgri;
	}
	
	
}