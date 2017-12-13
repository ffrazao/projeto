package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Ipa;

@Repository("IpaDao")
public interface IpaDao extends JpaRepository<Ipa, Integer>{


	@Query("SELECT p FROM Ipa p, IpaProducao ip WHERE p.ano = :ano AND p.unidadeOrganizacional.id = :unidadeOrganizacionalId")
	List<Ipa> findByAnoUnidadeOrganizacional(@Param("ano") Integer ano, @Param("unidadeOrganizacionalId") Integer unidadeOrganizacionalId);


	@Modifying(clearAutomatically = true)
    @Transactional
	@Query("DELETE Ipa p WHERE p.id =:ipa")
	void deleteIpa(@Param("ipa") Integer ipa);

	//@Query("SELECT p FROM Ipa p WHERE p.ano = :ano AND p.unidadeOrganizacional = :uo AND p.propriedadeRural = :pr AND p.publicoAlvo = :pa" )
	//Ipa retornaId(@Param("ano") Integer ano, @Param("uo") UnidadeOrganizacional uo, @Param("pr") PropriedadeRural pr, @Param("pa") PublicoAlvo pa);

	@Query("SELECT p FROM Ipa p WHERE p.ano = :ano AND p.unidadeOrganizacional = :uo AND p.propriedadeRural = :pr AND p.publicoAlvo = :pa" )
	Ipa retornaId(@Param("ano") Integer ano, @Param("uo") UnidadeOrganizacional uo, @Param("pr") PropriedadeRural pr, @Param("pa") PublicoAlvo pa);
	
	Ipa findOneByAnoAndUnidadeOrganizacionalAndPropriedadeRuralIsNullAndPublicoAlvoIsNull(Integer ano, UnidadeOrganizacional uo);
}
