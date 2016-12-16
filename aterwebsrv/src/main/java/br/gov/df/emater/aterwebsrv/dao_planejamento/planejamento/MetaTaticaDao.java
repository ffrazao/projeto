package br.gov.df.emater.aterwebsrv.dao_planejamento.planejamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo_planejamento.planejamento.MetaTatica;

@Repository("MetaTaticaDao")
public interface MetaTaticaDao extends JpaRepository<MetaTatica, Integer>, MetaTaticaDaoCustom {


}