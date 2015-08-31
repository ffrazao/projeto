package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Repository
@Qualifier(value = "pessoaDao")
public interface PessoaDao extends JpaRepository<Pessoa, Integer> {

}