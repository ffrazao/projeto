package br.gov.df.emater.aterwebsrv.dao.pessoa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Repository("PessoaJuridicaDao")
public interface PessoaJuridicaDao extends JpaRepository<PessoaJuridica, Integer> {

	List<PessoaJuridica> findByCnpj(String cnpj);

}