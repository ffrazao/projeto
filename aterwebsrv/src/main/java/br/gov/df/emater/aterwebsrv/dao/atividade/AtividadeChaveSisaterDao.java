package br.gov.df.emater.aterwebsrv.dao.atividade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadeChaveSisater;

@Repository("AtividadeChaveSisaterDao")
public interface AtividadeChaveSisaterDao extends JpaRepository<AtividadeChaveSisater, Integer> {

}