package br.gov.df.emater.aterwebsrv.dao.funcional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.funcional.LotacaoVi;

@Repository
public interface LotacaoViDao extends JpaRepository<LotacaoVi, Long> {

	List<LotacaoVi> findByEmpregadoId(Integer empregadoId);

}