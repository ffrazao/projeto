package br.gov.df.emater.aterwebsrv.dao.pessoa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Repository("PessoaDao")
public interface PessoaDao extends JpaRepository<Pessoa, Integer>, PessoaDaoCustom {

	List<Pessoa> findByInscricaoEstadual(String numero);

	List<Pessoa> findByInscricaoEstadualUfAndInscricaoEstadual(String uf, String numero);

	Pessoa findByNome(String nome);

	Pessoa findOneByApelidoSigla(String string);

	Pessoa findOneByChaveSisater(String chaveSisater);

	@Modifying
	@Query("update Pessoa p set p.inscricaoEstadual = null, p.inscricaoEstadualUf = null where p = ?1")
	int setInscricaoEstadualNull(Pessoa pessoa);

}