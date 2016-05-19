package br.gov.df.emater.aterwebsrv.dao.atividade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.atividade.Metodo;

@Repository("MetodoDao")
public interface MetodoDao extends JpaRepository<Metodo, Integer> {

	Metodo findOneByNome(String nome);

}