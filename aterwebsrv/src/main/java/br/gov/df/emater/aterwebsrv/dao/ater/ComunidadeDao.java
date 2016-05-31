package br.gov.df.emater.aterwebsrv.dao.ater;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;

@Repository("ComunidadeDao")
public interface ComunidadeDao extends JpaRepository<Comunidade, Integer> {

	List<Comunidade> findByNomeLikeOrderByNomeAsc(String nomeLike);

	List<Comunidade> findByNomeOrderByNomeAsc(String nome);

	List<Comunidade> findByUnidadeOrganizacionalIdInAndNomeLikeOrderByNomeAsc(Set<Integer> unidadeOrganizacionalList, String nomeLike);

	List<Comunidade> findByUnidadeOrganizacionalPessoaJuridicaIdInAndNomeLikeOrderByNomeAsc(Set<Integer> pessoaJuridicaList, String nomeLike);

}