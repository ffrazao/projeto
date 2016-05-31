package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaTelefone;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Telefone;

@Repository("PessoaTelefoneDao")
public interface PessoaTelefoneDao extends JpaRepository<PessoaTelefone, Integer> {

	PessoaTelefone findOneByPessoaAndTelefone(Pessoa result, Telefone telefone);

}