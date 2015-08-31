package br.gov.df.emater.aterwebsrv.dao.enquete;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.teste.Teste;

@Repository
@Qualifier(value = "enqueteDao")
public interface EnqueteDao extends JpaRepository<Teste, Long> {

}