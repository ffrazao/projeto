package br.gov.df.emater.aterwebsrv.dao.ater;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;

@Repository("PropriedadeRuralDao")
public interface PropriedadeRuralDao extends JpaRepository<PropriedadeRural, Integer> {

	List<PropriedadeRural> findByAreaIrrigadaGotejamento(String valor);

	@Query(value = "SELECT count(*) FROM pessoa.pessoa WHERE nome like :valor", nativeQuery = true)
	Integer filtroPorCount(@Param("valor") String valor);
}
