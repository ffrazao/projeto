package br.gov.df.emater.aterwebsrv.dao.sistema;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.sistema.Funcionalidade;

@Repository("FuncionalidadeDao")
public interface FuncionalidadeDao extends JpaRepository<Funcionalidade, Integer>, FuncionalidadeDaoCustom {

}