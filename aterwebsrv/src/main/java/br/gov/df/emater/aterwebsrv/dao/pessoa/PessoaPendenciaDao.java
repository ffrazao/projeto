package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaPendencia;

@Repository("PessoaPendenciaDao")
public interface PessoaPendenciaDao extends JpaRepository<PessoaPendencia, Integer> {

	PessoaPendencia findOneByPessoaAndCodigo(Pessoa pessoa, String codigo);

}