package br.gov.df.emater.aterwebsrv.dao.ater;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.teste.Teste;

@Repository("AterDao")
public interface AterDao extends JpaRepository<Teste, Long> {

}