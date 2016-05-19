package br.gov.df.emater.aterwebsrv.dao.atividade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.atividade.Assunto;

@Repository("AssuntoDao")
public interface AssuntoDao extends JpaRepository<Assunto, Integer> {

	Assunto findOneByNome(String nome);

}