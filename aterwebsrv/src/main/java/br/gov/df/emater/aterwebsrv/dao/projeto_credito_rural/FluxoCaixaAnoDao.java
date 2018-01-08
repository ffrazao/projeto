package br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.dominio.FluxoCaixaTipo;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.FluxoCaixaAno;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;

@Repository("FluxoCaixaAnoDao")
public interface FluxoCaixaAnoDao extends JpaRepository<FluxoCaixaAno, Integer> {
	
	@Query("SELECT sum(a.valor) FROM ProjetoCreditoRuralFluxoCaixa c, FluxoCaixaAno a WHERE"
			+ " c.id = a.projetoCreditoRuralFluxoCaixa AND c.projetoCreditoRural =:projeto AND a.ano =:ano AND c.tipo =:tipo")
	BigDecimal retornaValor(@Param("projeto") ProjetoCreditoRural projeto, @Param("ano") Integer ano, @Param("tipo") FluxoCaixaTipo tipo);

}