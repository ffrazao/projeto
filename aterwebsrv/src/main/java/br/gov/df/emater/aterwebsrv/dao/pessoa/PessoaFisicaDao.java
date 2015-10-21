package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.teste.Teste;

@Repository("PessoaFisicaDao")
public interface PessoaFisicaDao extends JpaRepository<Teste, Long> {

}