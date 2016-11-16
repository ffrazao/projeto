package br.gov.df.emater.aterwebsrv.dao.funcional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacionalHierarquia;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacionalHierarquiaAtivaVi;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Repository("UnidadeOrganizacionalHierarquiaAtivaViDao")
public interface UnidadeOrganizacionalHierarquiaAtivaViDao extends JpaRepository<UnidadeOrganizacionalHierarquiaAtivaVi, Integer> {

	List<UnidadeOrganizacionalHierarquia> findAllByAscendentePessoaJuridicaOrDescendentePessoaJuridica(PessoaJuridica pessoaJuridica);

}