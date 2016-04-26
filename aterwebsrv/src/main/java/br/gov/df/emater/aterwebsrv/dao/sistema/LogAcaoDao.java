package br.gov.df.emater.aterwebsrv.dao.sistema;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.sistema.LogAcao;

@Repository("LogAcaoDao")
public interface LogAcaoDao extends JpaRepository<LogAcao, Integer>, LogAcaoDaoCustom {

}