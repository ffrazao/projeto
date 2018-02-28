package br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoAntigo;

@Repository("ProjetoCreditoAntigoDao")
public interface ProjetoCreditoAntigoDao extends JpaRepository<ProjetoCreditoAntigo, Integer> {

	@Query("SELECT p FROM ProjetoCreditoAntigo p, ProjetoCreditoAntigoCusteio pc "
			+ "WHERE p.id = :id"
			)
	ProjetoCreditoAntigo findById(@Param("id") Integer id);
	List<ProjetoCreditoAntigo> findByNomeUnidade(String und);

}