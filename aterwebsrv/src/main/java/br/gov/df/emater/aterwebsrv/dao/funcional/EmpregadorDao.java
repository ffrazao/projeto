package br.gov.df.emater.aterwebsrv.dao.funcional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.funcional.Empregador;

@Repository("EmpregadorDao")
public interface EmpregadorDao extends JpaRepository<Empregador, Integer> {

}