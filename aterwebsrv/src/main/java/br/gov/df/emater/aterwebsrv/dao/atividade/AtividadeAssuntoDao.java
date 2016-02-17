package br.gov.df.emater.aterwebsrv.dao.atividade;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadeAssunto;

@Repository("AtividadeAssuntoDao")
public interface AtividadeAssuntoDao extends JpaRepository<AtividadeAssunto, Integer> {

	List<AtividadeAssunto> findTop10ByAtividadeIdOrderByAssuntoNomeAsc(Integer atividadeId);

}