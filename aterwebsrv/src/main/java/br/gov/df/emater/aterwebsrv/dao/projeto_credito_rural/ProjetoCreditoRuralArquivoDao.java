package br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralArquivo;

@Repository("ProjetoCreditoRuralArquivoDao")
public interface ProjetoCreditoRuralArquivoDao extends JpaRepository<ProjetoCreditoRuralArquivo, Integer> {

	List<ProjetoCreditoRuralArquivo> findByProjetoCreditoRural(ProjetoCreditoRural projetoCreditoRural);

	void deleteByProjetoCreditoRural(ProjetoCreditoRural projetoCreditoRural);

}