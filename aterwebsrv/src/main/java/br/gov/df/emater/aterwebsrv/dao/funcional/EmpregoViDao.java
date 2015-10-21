package br.gov.df.emater.aterwebsrv.dao.funcional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.funcional.EmpregoVi;

@Repository("EmpregoViDao")
public interface EmpregoViDao extends JpaRepository<EmpregoVi, Long> {

	List<EmpregoVi> findByEmpregadoId(Integer empregadoId);

}