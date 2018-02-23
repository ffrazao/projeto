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
          
public class SalvarAgroCmd {

	private IpaProducao ipaProdAgro;
	private IpaProducaoBemClassificado ipaBemAgro;
	private List<IpaProducaoForma> ipaFormaAgro;

	public SalvarAgroCmd() {
	}
	
	public void salvarAgro(IpaProducao ipAgro, Ipa ipa, ProducaoGravaDto result) throws BoException {


		
// TABELA IPA PRODUCAO

		List<IpaProducao> ipArrayList = new ArrayList<>();

		ipArrayList.add(ipAgro);
		
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
				
		this.setIpaProdAgro(ip);
		
// TABELA IPA PRODUCAO BEM CLASSIFICADO
					
				IpaProducaoBemClassificado ipbc = new IpaProducaoBemClassificado();
				
				if(ipAgro.getBemClassificado() == null){
					throw new BoException("Favor inserir a Cultura!");
				}
				
				ipbc.setBemClassificado(ipAgro.getBemClassificado());
				ipbc.setIpaProducao(ip);
				ipbc.setProducao(ipAgro.getProducao());
				ipbc.setProdutividade(ipAgro.getProdutividade());
				ipbc.setQuantidadeProdutores(ipAgro.getQuantidadeProdutores());
				ipbc.setValorUnitario(ipAgro.getValorUnitario());
	
				this.setIpaBemAgro(ipbc);

		
// TABELA IPA PRODUCAO FORMA
				
				ArrayList<IpaProducaoForma> list = new ArrayList<>();
				
				IpaProducaoForma ipf1 = new IpaProducaoForma();
				ipf1.setBemClassificacao(ipAgro.getTipo());
				ipf1.setIpaProducao(ip);
				ipf1.setOrdem(1);

				list.add(ipf1);
				
				IpaProducaoForma ipf2 = new IpaProducaoForma();
				ipf2.setBemClassificacao(ipAgro.getCategoria());
				ipf2.setIpaProducao(ip);
				ipf2.setOrdem(2);
	
				list.add(ipf2);
	
		//ordem = 0;
				for (IpaForma ipaProducaoForma : ipAgro.getProducaoComposicaoList()) {

					IpaProducaoForma ipf = new IpaProducaoForma();
					FormaProducaoValor fpv = new FormaProducaoValor();
					
					fpv.setId(ipaProducaoForma.getFormaProducaoValor().getId());
					
					ipf.setFormaProducaoValor(fpv);
					ipf.setIpaProducao(ip);
					ipf.setOrdem(3);
					
					list.add(ipf);	
				
			}
		
		this.setIpaFormaAgro(list);
			
	}

	public IpaProducao getIpaProdAgro() {
		return ipaProdAgro;
	}

	public void setIpaProdAgro(IpaProducao ipaProdAgro) {
		this.ipaProdAgro = ipaProdAgro;
	}

	public IpaProducaoBemClassificado getIpaBemAgro() {
		return ipaBemAgro;
	}

	public void setIpaBemAgro(IpaProducaoBemClassificado ipaBemAgro) {
		this.ipaBemAgro = ipaBemAgro;
	}

	public List<IpaProducaoForma> getIpaFormaAgro() {
		return ipaFormaAgro;
	}

	public void setIpaFormaAgro(List<IpaProducaoForma> ipaFormaAgro) {
		this.ipaFormaAgro = ipaFormaAgro;
	}
	
}