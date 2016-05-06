package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Repository("PessoaDao")
public interface PessoaDao extends JpaRepository<Pessoa, Integer>, PessoaDaoCustom {

	Pessoa findByNome(String nome);

	Pessoa findOneByApelidoSigla(String string);

}