package br.gov.df.emater.aterwebsrv.dao.sistema;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.sistema.FuncionalidadeComando;

@Repository("FuncionalidadeComandoDao")
public interface FuncionalidadeComandoDao extends JpaRepository<FuncionalidadeComando, Integer> {

}