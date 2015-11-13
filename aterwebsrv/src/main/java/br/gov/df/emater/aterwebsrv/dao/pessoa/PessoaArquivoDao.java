package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaArquivo;

@Repository("PessoaArquivoDao")
public interface PessoaArquivoDao extends JpaRepository<PessoaArquivo, Integer> {

}