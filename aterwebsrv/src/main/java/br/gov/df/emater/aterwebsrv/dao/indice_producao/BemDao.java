package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Bem;

@Repository("BemDao")
public interface BemDao extends JpaRepository<Bem, Integer>, BemDaoCustom {

}
