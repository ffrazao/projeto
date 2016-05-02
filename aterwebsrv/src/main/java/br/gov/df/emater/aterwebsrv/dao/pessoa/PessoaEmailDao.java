package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.Email;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEmail;

@Repository("PessoaEmailDao")
public interface PessoaEmailDao extends JpaRepository<PessoaEmail, Integer> {

	PessoaEmail findOneByPessoaAndEmail(Pessoa result, Email email);

}