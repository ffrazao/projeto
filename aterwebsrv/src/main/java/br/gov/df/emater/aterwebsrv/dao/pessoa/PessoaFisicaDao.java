package br.gov.df.emater.aterwebsrv.dao.pessoa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;

@Repository("PessoaFisicaDao")
public interface PessoaFisicaDao extends JpaRepository<PessoaFisica, Integer> {

	PessoaFisica findOneByNomeAndGenero(String nomeExcel, PessoaGenero genero);
	
	List<PessoaFisica> findByNomeIgnoreCaseAndGenero(String nome, PessoaGenero genero);

	List<PessoaFisica> findByCpf(String numero);

	List<PessoaFisica> findByRgNumero(String numero);

	List<PessoaFisica> findByRgNumeroAndRgUf(String numero, String uf);

	List<PessoaFisica> findByNisNumero(String numero);

}