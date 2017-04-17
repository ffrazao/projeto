package br.gov.df.emater.aterwebsrv.dao.atividade;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadeCadeiaProdutiva;

@Repository("AtividadeCadeiaProdutivaDao")
public interface AtividadeCadeiaProdutivaDao extends JpaRepository<AtividadeCadeiaProdutiva, Integer> {

	List<AtividadeCadeiaProdutiva> findTop10ByAtividadeIdOrderByCadeiaProdutivaNomeAsc(Integer atividadeId);

}