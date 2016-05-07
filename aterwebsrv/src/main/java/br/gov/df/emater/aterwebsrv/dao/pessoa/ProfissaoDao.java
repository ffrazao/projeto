package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.Profissao;

@Repository("ProfissaoDao")
public interface ProfissaoDao extends JpaRepository<Profissao, Integer> {

	Profissao findOneByNome(String nome);

}
