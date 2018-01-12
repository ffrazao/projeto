package br.gov.df.emater.aterwebsrv.dao.pessoa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaRelacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Relacionamento;

@Repository("PessoaRelacionamentoDao")
public interface PessoaRelacionamentoDao extends JpaRepository<PessoaRelacionamento, Integer> {

	List<PessoaRelacionamento> findByCpf(String numero);

	List<PessoaRelacionamento> findByRelacionamento(Relacionamento relacionamento);
	
	@Query("FROM PessoaRelacionamento where relacionamento "
			+ "in (SELECT relacionamento FROM PessoaRelacionamento WHERE pessoa.id =:idPessoa )")
	List<PessoaRelacionamento> retornaListaRel(@Param("idPessoa") Integer idPessoa);
	
	@Query("FROM PessoaRelacionamento where relacionamento "
			+ "in (SELECT relacionamento FROM PessoaRelacionamento WHERE pessoa.id =:idPessoa) AND nome is not null")
	List<PessoaRelacionamento> retornaListaRelConj(@Param("idPessoa") Integer idPessoa);
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("UPDATE PessoaRelacionamento r SET r.pessoa =:pessoa WHERE r.id =:id")
	void updatePessoaId(@Param("pessoa") Pessoa pessoa ,@Param("id") Integer id);

}
