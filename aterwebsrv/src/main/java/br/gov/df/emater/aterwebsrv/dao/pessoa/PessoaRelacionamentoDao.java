package br.gov.df.emater.aterwebsrv.dao.pessoa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.funcional.Emprego;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaRelacionamento;

@Repository("PessoaRelacionamentoDao")
public interface PessoaRelacionamentoDao extends JpaRepository<PessoaRelacionamento, Integer> {

	List<PessoaRelacionamento> findByRelacionamento(Emprego emprego);

}
