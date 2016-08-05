package br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralPublicoAlvoPropriedadeRural;

@Repository("ProjetoCreditoRuralPublicoAlvoPropriedadeRuralDao")
public interface ProjetoCreditoRuralPublicoAlvoPropriedadeRuralDao extends JpaRepository<ProjetoCreditoRuralPublicoAlvoPropriedadeRural, Integer> {

	List<ProjetoCreditoRuralPublicoAlvoPropriedadeRural> findAllByProjetoCreditoRural(ProjetoCreditoRural projetoCreditoRural);

}