package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoItem;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoValor;

@Repository("FormaProducaoValorDao")
public interface FormaProducaoValorDao extends JpaRepository<FormaProducaoValor, Integer> {

	List<FormaProducaoValor> findByFormaProducaoItemAndNomeLikeIgnoreCase(FormaProducaoItem formaProducaoItem, String nome);
	
	@Query("SELECT p FROM FormaProducaoValor p WHERE p.id =:id")
	FormaProducaoValor retornaForma(@Param("id") Integer id);
}
