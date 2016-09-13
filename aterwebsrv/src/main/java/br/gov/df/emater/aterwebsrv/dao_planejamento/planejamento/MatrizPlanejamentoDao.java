package br.gov.df.emater.aterwebsrv.dao_planejamento.planejamento;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.df.emater.aterwebsrv.modelo_planejamento.planejamento.MatrizPlanejamento;

//@Repository("MatrizPlanejamentoDao")
public interface MatrizPlanejamentoDao extends JpaRepository<MatrizPlanejamento, Integer> {

}
