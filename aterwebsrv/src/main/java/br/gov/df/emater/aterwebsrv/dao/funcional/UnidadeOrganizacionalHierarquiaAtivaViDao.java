package br.gov.df.emater.aterwebsrv.dao.funcional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacionalHierarquiaAtivaVi;

@Repository("UnidadeOrganizacionalHierarquiaAtivaViDao")
public interface UnidadeOrganizacionalHierarquiaAtivaViDao extends JpaRepository<UnidadeOrganizacionalHierarquiaAtivaVi, Integer> {

}