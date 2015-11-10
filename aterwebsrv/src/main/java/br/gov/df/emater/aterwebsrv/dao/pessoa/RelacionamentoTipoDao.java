package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoTipo;

@Repository("RelacionamentoTipoDao")
public interface RelacionamentoTipoDao extends JpaRepository<RelacionamentoTipo, Integer> {

	RelacionamentoTipo findByCodigo(String codigo);

}
