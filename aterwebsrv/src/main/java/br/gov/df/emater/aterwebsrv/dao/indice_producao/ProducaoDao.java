package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;

@Repository("ProducaoDao")
public interface ProducaoDao extends JpaRepository<Producao, Integer> {

	List<Producao> findByProducaoProprietario(ProducaoProprietario producaoProprietario);
	
	@Query("SELECT p FROM Producao p WHERE p.producaoProprietario.ano = :ano AND  p.producaoProprietario.unidadeOrganizacional.id = :unidadeOrganizacionalId")
	List<Producao> findByAnoUnidadeOrganizacional(@Param("ano") Integer ano, @Param("unidadeOrganizacionalId") Integer unidadeOrganizacionalId);
	
}
