package br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.CronogramaPagamento;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;

@Repository("CronogramaPagamentoDao")
public interface CronogramaPagamentoDao extends JpaRepository<CronogramaPagamento, Integer> {
	
	@Query("SELECT  sum(prestacao) FROM CronogramaPagamento "
			+ "WHERE projetoCreditoRuralCronogramaPagamento.id in "
			+ "(SELECT id FROM ProjetoCreditoRuralCronogramaPagamento "
			+ "WHERE projetoCreditoRural =:projeto) AND ano =:ano")
	BigDecimal retornaPrestacao(@Param("projeto") ProjetoCreditoRural projeto, @Param("ano") Integer ano);

}