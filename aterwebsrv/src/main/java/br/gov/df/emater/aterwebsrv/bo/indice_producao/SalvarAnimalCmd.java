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
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoValor;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Ipa;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoBemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoForma;
          
//@Service("IndiceProducaoSalvarAnimalCmd")
public class SalvarAnimalCmd {

	private IpaProducao ipaProdAnimal;
	private List<IpaProducaoBemClassificado> ipaBemAnimal;
	private List<IpaProducaoForma> ipaFormaAnimal;

	private Integer ordem;

	public SalvarAnimalCmd() {
	}

	public void salvarAnimal(IpaProducao ipAnimal, Ipa ipa, ProducaoGravaDto result) throws Exception {

		
// TABELA IPA PRODUCAO

		List<IpaProducao> ipArrayList = new ArrayList<>();

		ipArrayList.add(ipAnimal);
		
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
			
			if(i.getCultura() == null){
				ip.setCultura(null);
			}else{
				ip.setCultura(i.getCultura());
			}
	
		});
		
		this.setIpaProdAnimal(ip);
		
// TABELA IPA PRODUCAO BEM CLASSIFICADO
		
		List<IpaProducaoBemClassificado> ListaAnimalBem =  ipAnimal.getProdutoList();
		ArrayList<IpaProducaoBemClassificado> listBem = new ArrayList<>();
		
		for (IpaProducaoBemClassificado bemClassificado : ListaAnimalBem) {
			
			IpaProducaoBemClassificado ipbc = new IpaProducaoBemClassificado();
			
			ipbc.setBemClassificado(bemClassificado.getBemClassificado());
			ipbc.setIpaProducao(ip);
			ipbc.setProducao(bemClassificado.getProducao());
			ipbc.setQuantidadeProdutores(ipAnimal.getQuantidadeProdutores());
			ipbc.setProdutividade(bemClassificado.getProdutividade());
			ipbc.setValorUnitario(bemClassificado.getValorUnitario());

			listBem.add(ipbc);
	}
		
		this.setIpaBemAnimal(listBem);
		
		
// TABELA IPA PRODUCAO FORMA
		
		ArrayList<IpaProducaoForma> list = new ArrayList<>();
		ordem = 0;
		
		for (IpaForma ipaProducaoForma : ipAnimal.getProducaoComposicaoList()) {

				IpaProducaoForma ipf = new IpaProducaoForma();
				FormaProducaoValor fpv = new FormaProducaoValor();
				
				fpv.setId(ipaProducaoForma.getFormaProducaoValor().getId());
				
				ipf.setFormaProducaoValor(fpv);
				ipf.setIpaProducao(ip);
				ipf.setOrdem(++ordem);
				
				list.add(ipf);	
			
		}
		
				this.setIpaFormaAnimal(list);
		
	}

	public IpaProducao getIpaProdAnimal() {
		return ipaProdAnimal;
	}

	public void setIpaProdAnimal(IpaProducao ipaProdAnimal) {
		this.ipaProdAnimal = ipaProdAnimal;
	}

	public List<IpaProducaoBemClassificado> getIpaBemAnimal() {
		return ipaBemAnimal;
	}

	public void setIpaBemAnimal(List<IpaProducaoBemClassificado> ipaBemAnimal) {
		this.ipaBemAnimal = ipaBemAnimal;
	}

	public List<IpaProducaoForma> getIpaFormaAnimal() {
		return ipaFormaAnimal;
	}

	public void setIpaFormaAnimal(List<IpaProducaoForma> ipaFormaAnimal) {
		this.ipaFormaAnimal = ipaFormaAnimal;
	}


	
}