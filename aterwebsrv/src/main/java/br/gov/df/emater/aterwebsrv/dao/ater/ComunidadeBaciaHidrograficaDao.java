package br.gov.df.emater.aterwebsrv.dao.ater;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.ater.BaciaHidrografica;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.ComunidadeBaciaHidrografica;

@Repository("ComunidadeBaciaHidrograficaDao")
public interface ComunidadeBaciaHidrograficaDao extends JpaRepository<ComunidadeBaciaHidrografica, Integer> {

	List<ComunidadeBaciaHidrografica> findByComunidadeAndBaciaHidrografica(Comunidade comunidade, BaciaHidrografica baciaHidrografica);

}