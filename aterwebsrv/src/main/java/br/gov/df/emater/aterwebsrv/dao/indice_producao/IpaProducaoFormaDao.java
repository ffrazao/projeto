package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoForma;

@Repository("IpaProducaoFormaDao")
public interface IpaProducaoFormaDao extends JpaRepository<IpaProducaoForma, Integer>{
	
	@Query("SELECT p FROM IpaProducaoForma p WHERE p.ipaProducao = :ip")
	List<IpaProducaoForma> ListaIpaProducaoForma(@Param("ip") IpaProducao ip);

	//@Query("DELETE IpaProducaoForma p WHERE p.ipaProducao.id = :ipaProducao")
	//void deleteByIpaProducao(@Param("ipaProducao") Integer ipaProducao);
	@Modifying(clearAutomatically = true)
    @Transactional
    @Query("delete IpaProducaoForma where ipaProducao =:ipaProducao")
	void deleteForma(@Param("ipaProducao") IpaProducao ipaProducao);
	
	@Query("SELECT p.id FROM IpaProducaoForma p WHERE p.ipaProducao = :ip")
	List<Integer> retornaId(@Param("ip") IpaProducao ip);

}
