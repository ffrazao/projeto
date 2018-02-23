package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.ArrayList;
import java.util.List;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.ProducaoGravaDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoValor;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Ipa;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoBemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoForma;
          
public class SalvarArteCmd{

	private IpaProducao ipaProdArte;
	private IpaProducaoBemClassificado ipaBemArte;
	private List<IpaProducaoForma> ipaFormaArte;

	public SalvarArteCmd() {
	}

	public void salvarArte(IpaProducao ipArte, Ipa ipa, ProducaoGravaDto result) throws Exception {
		
// TABELA IPA PRODUCAO

		List<IpaProducao> ipArrayList = new ArrayList<>();

		ipArrayList.add(ipArte);
		
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
		
		this.setIpaProdArte(ip);
		
// TABELA IPA PRODUCAO BEM CLASSIFICADO
					
				IpaProducaoBemClassificado ipbc = new IpaProducaoBemClassificado();
				
				if(ipArte.getBemClassificado() == null){
					throw new BoException("Favor inserir a Cultura!");
				}
				
				ipbc.setBemClassificado(ipArte.getBemClassificado());
				ipbc.setIpaProducao(ip);
				ipbc.setProducao(ipArte.getProducao());
				ipbc.setProdutividade(ipArte.getProdutividade());
				ipbc.setQuantidadeProdutores(ipArte.getQuantidadeProdutores());
				ipbc.setValorUnitario(ipArte.getValorUnitario());
					
				this.setIpaBemArte(ipbc);

		
// TABELA IPA PRODUCAO FORMA
				
				ArrayList<IpaProducaoForma> list = new ArrayList<>();
				
				IpaProducaoForma ipf1 = new IpaProducaoForma();

				ipf1.setBemClassificacao(ipArte.getTipo());
				ipf1.setIpaProducao(ip);
				ipf1.setOrdem(1);
				list.add(ipf1);
				
				IpaProducaoForma ipf2 = new IpaProducaoForma();

				ipf2.setBemClassificacao(ipArte.getCategoria());
				ipf2.setIpaProducao(ip);
				ipf2.setOrdem(2);	
				list.add(ipf2);
				
		for (IpaForma ipaProducaoForma : ipArte.getProducaoComposicaoList()) {

			System.out.println("entrou no composicao");
					
			IpaProducaoForma ipf = new IpaProducaoForma();
					
			System.out.println(ipaProducaoForma.getId());
			FormaProducaoValor fpv = new FormaProducaoValor();
			fpv.setId(ipaProducaoForma.getId());
			ipf.setFormaProducaoValor(fpv);
			ipf.setIpaProducao(ip);
			ipf.setOrdem(3);
					
			list.add(ipf);
	}
		
		this.setIpaFormaArte(list);
		
	}

	public IpaProducao getIpaProdArte() {
		return ipaProdArte;
	}

	public void setIpaProdArte(IpaProducao ipaProdArte) {
		this.ipaProdArte = ipaProdArte;
	}

	public IpaProducaoBemClassificado getIpaBemArte() {
		return ipaBemArte;
	}

	public void setIpaBemArte(IpaProducaoBemClassificado ipaBemArte) {
		this.ipaBemArte = ipaBemArte;
	}

	public List<IpaProducaoForma> getIpaFormaArte() {
		return ipaFormaArte;
	}

	public void setIpaFormaArte(List<IpaProducaoForma> ipaFormaArte) {
		this.ipaFormaArte = ipaFormaArte;
	}
	
}