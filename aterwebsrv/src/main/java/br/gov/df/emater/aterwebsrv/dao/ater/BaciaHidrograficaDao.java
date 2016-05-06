package br.gov.df.emater.aterwebsrv.dao.ater;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.ater.BaciaHidrografica;

@Repository("BaciaHidrograficaDao")
public interface BaciaHidrograficaDao extends JpaRepository<BaciaHidrografica, Integer> {

	List<BaciaHidrografica> findByNomeOrderByNomeAsc(String nome);

}