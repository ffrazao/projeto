package br.gov.df.emater.aterwebsrv.dao.funcional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.funcional.Cargo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Repository("CargoDao")
public interface CargoDao extends JpaRepository<Cargo, Integer> {

	Cargo findOneByPessoaJuridicaAndNome(PessoaJuridica pessoaJuridica, String nome);

}