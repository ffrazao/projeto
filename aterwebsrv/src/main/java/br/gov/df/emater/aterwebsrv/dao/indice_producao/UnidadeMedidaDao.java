package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.indice_producao.UnidadeMedida;

@Repository("UnidadeMedidaDao")
public interface UnidadeMedidaDao extends JpaRepository<UnidadeMedida, Integer> {

	List<UnidadeMedida> findByNomeLikeIgnoreCase(String nome);

}
