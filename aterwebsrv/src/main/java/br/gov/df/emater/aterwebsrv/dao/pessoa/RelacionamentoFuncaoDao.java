package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoFuncao;

@Repository("RelacionamentoFuncaoDao")
public interface RelacionamentoFuncaoDao extends JpaRepository<RelacionamentoFuncao, Integer> {

}
