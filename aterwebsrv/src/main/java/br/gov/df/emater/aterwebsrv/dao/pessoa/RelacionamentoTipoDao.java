package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoTipo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoTipo.Codigo;

@Repository("RelacionamentoTipoDao")
public interface RelacionamentoTipoDao extends JpaRepository<RelacionamentoTipo, Integer> {

	RelacionamentoTipo findOneByCodigo(Codigo codigo);

}
