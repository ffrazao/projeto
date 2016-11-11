package br.gov.df.emater.aterwebsrv.dao.funcional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacionalHierarquia;

@Repository("UnidadeOrganizacionalHierarquiaDao")
public interface UnidadeOrganizacionalHierarquiaDao extends JpaRepository<UnidadeOrganizacionalHierarquia, Integer> {

}
