package br.gov.df.emater.aterwebsrv.dao.funcional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacionalComunidade;

@Repository("UnidadeOrganizacionalComunidadeDao")
public interface UnidadeOrganizacionalComunidadeDao extends JpaRepository<UnidadeOrganizacionalComunidade, Integer> {

	List<UnidadeOrganizacionalComunidade> findByUnidadeOrganizacionalPessoaJuridicaId(Integer id);

}
