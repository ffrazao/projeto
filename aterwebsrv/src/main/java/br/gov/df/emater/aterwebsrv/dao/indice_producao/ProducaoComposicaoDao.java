package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoComposicao;

@Repository("ProducaoComposicaoDao")
public interface ProducaoComposicaoDao extends JpaRepository<ProducaoComposicao, Integer> {

	List<ProducaoComposicao> findAllByProducao(Producao producao);

}