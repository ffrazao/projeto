package br.gov.df.emater.aterwebsrv.dao.funcional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.funcional.LotacaoAtivaVi;

@Repository("LotacaoAtivaViDao")
public interface LotacaoAtivaViDao extends JpaRepository<LotacaoAtivaVi, Integer> {

	List<LotacaoAtivaVi> findAllByUnidadeOrganizacionalId(Integer id);

}