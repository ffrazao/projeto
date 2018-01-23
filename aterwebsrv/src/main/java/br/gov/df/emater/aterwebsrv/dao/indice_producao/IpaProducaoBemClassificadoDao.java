package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.IpaProducaoBemClassificado;

@Repository("IpaProducaoBemClassificadoDao")
public interface IpaProducaoBemClassificadoDao extends JpaRepository<IpaProducaoBemClassificado, Integer> {
	
	@Query("SELECT p FROM IpaProducaoBemClassificado p WHERE p.bemClassificado = :bem")
	List<IpaProducaoBemClassificado> findByIpaProducaoBem(@Param("bem") BemClassificado bem);
	
	@Query("SELECT p FROM IpaProducao pp, IpaProducaoBemClassificado p WHERE p.ipaProducao = pp.id  AND p.bemClassificado = :bem")
	List<IpaProducaoBemClassificado> findByIpaProducaoClass(@Param("bem") BemClassificado bem);

	@Query("SELECT p FROM IpaProducaoBemClassificado p WHERE p.ipaProducao = :ip")
	List<IpaProducaoBemClassificado> findAllByIpaProducaoBemClassificado(@Param("ip") IpaProducao ip);
	
	@Query("SELECT p.id FROM IpaProducaoBemClassificado p WHERE p.ipaProducao = :ip")
	Integer retornaId(@Param("ip") IpaProducao ip);

	@Modifying
    @Transactional
	@Query("DELETE IpaProducaoBemClassificado WHERE ipaProducao.id = :ipaProducao")
	void deleteBem(@Param("ipaProducao") Integer ipaProducao);
	
	//void deleteByIpaProducao(IpaProducao ipaProducao);
	
	//p.bemClassificado, p.produtividade, p.producao, p.valorUnitario, p.valorTotal, pp.area 

}
