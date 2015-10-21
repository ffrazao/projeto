package br.gov.df.emater.aterwebsrv.dao.ipa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.teste.Teste;

@Repository("IpaDao")
public interface IpaDao extends JpaRepository<Teste, Long> {

}