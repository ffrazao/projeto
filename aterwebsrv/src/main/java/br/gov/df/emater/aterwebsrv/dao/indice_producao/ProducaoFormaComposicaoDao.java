package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoFormaComposicao;

@Repository("ProducaoFormaComposicaoDao")
public interface ProducaoFormaComposicaoDao extends JpaRepository<ProducaoFormaComposicao, Integer> {

	List<ProducaoFormaComposicao> findAllByProducaoForma(ProducaoForma producaoForma);

}