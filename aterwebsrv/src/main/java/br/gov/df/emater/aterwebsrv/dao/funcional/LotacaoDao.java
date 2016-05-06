package br.gov.df.emater.aterwebsrv.dao.funcional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.funcional.Emprego;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Lotacao;

@Repository("LotacaoDao")
public interface LotacaoDao extends JpaRepository<Lotacao, Integer> {

	List<Lotacao> findByEmprego(Emprego emprego);

}