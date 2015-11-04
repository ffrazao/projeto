package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.Email;

@Repository("EmailDao")
public interface EmailDao extends JpaRepository<Email, Integer> {

	Email findByEndereco(String endereco);

}