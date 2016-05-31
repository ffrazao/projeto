package br.gov.df.emater.aterwebsrv.dao.funcional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.funcional.Emprego;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Repository("EmpregoDao")
public interface EmpregoDao extends JpaRepository<Emprego, Integer> {

	List<Emprego> findByMatricula(String matricula);

	List<Emprego> findByPessoaRelacionamentoListPessoaIn(Pessoa pessoa);

}