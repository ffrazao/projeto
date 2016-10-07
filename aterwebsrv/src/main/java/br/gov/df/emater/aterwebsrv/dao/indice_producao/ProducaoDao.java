package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;

@Repository("ProducaoDao")
public interface ProducaoDao extends JpaRepository<Producao, Integer> {

}
