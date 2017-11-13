package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacaoFormaProducaoBemClassificado;

@Repository("BemClassificacaoFormaProducaoBemClassificadoDao")
public class BemClassificacaoFormaProducaoBemClassificadoDao {
	
	@Autowired
	private EntityManager em;
	
public List<BemClassificacaoFormaProducaoBemClassificado> listaFormaProducaoBemClassificado(){
		
		List<BemClassificacaoFormaProducaoBemClassificado> forma = new ArrayList<BemClassificacaoFormaProducaoBemClassificado>();
		
			
		
		return forma;
	}

}
