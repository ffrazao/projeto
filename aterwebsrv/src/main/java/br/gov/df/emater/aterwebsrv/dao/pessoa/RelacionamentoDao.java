package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.Relacionamento;

@Repository("RelacionamentoDao")
public interface RelacionamentoDao extends JpaRepository<Relacionamento, Integer> {
	Relacionamento findById(Integer id);
}
