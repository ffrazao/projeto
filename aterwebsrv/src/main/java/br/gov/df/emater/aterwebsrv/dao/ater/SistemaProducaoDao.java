package br.gov.df.emater.aterwebsrv.dao.ater;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.ater.SistemaProducao;

@Repository("SistemaProducaoDao")
public interface SistemaProducaoDao extends JpaRepository<SistemaProducao, Integer> {

	SistemaProducao findOneByNome(String sistemaProducao);

}