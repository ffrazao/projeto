package br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.AgenteFinanceiro;

@Repository("AgenteFinanceiroDao")
public interface AgenteFinanceiroDao extends JpaRepository<AgenteFinanceiro, Integer> {

}