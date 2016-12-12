package br.gov.df.emater.aterwebsrv.dao.ater;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;

@Repository("ComunidadeDao")
public interface ComunidadeDao extends JpaRepository<Comunidade, Integer> {

	//@Cacheable("findByNomeLikeOrderByNomeAsc")
	List<Comunidade> findByNomeLikeOrderByNomeAsc(String nomeLike);

	//@Cacheable("findByNomeOrderByNomeAsc")
	List<Comunidade> findByNomeOrderByNomeAsc(String nome);

	//@Cacheable("findByUnidadeOrganizacionalIdInAndNomeLikeOrderByNomeAsc")
	List<Comunidade> findByUnidadeOrganizacionalIdInAndNomeLikeOrderByNomeAsc(Set<Integer> unidadeOrganizacionalList, String nomeLike);

	//@Cacheable("findByUnidadeOrganizacionalPessoaJuridicaIdInAndNomeLikeOrderByNomeAsc")
	List<Comunidade> findByUnidadeOrganizacionalPessoaJuridicaIdInAndNomeLikeOrderByNomeAsc(Set<Integer> pessoaJuridicaList, String nomeLike);

}