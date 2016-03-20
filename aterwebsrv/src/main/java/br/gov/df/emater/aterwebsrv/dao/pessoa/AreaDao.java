package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.Area;

@Repository("AreaDao")
public interface AreaDao extends JpaRepository<Area, Integer> {

}