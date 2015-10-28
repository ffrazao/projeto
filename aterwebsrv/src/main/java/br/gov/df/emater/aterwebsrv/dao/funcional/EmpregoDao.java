package br.gov.df.emater.aterwebsrv.dao.funcional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.funcional.Emprego;

@Repository("EmpregoDao")
public interface EmpregoDao extends JpaRepository<Emprego, Long> {

	List<Emprego> findByPessoaFisicaId(Integer id);

}