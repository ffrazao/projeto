package br.gov.df.emater.aterwebsrv.dao.ater;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;

public interface PublicoAlvoPropriedadeRuralDao extends JpaRepository<PublicoAlvoPropriedadeRural, Integer> {

	List<PublicoAlvoPropriedadeRural> findByPropriedadeRuralInAndComunidadeUnidadeOrganizacionalInAndPublicoAlvoIdIsNotNull(List<PropriedadeRural> propriedadeRuralList, List<UnidadeOrganizacional> unidadeOrganizacionalList);

	List<PublicoAlvoPropriedadeRural> findByPublicoAlvoInAndComunidadeUnidadeOrganizacionalInAndPropriedadeRuralIdIsNotNull(List<PublicoAlvo> publicoAlvoList, List<UnidadeOrganizacional> unidadeOrganizacionalList);

	List<PublicoAlvoPropriedadeRural> findByPublicoAlvoInAndPropriedadeRuralIdIsNotNull(List<PublicoAlvo> publicoAlvoList);

	PublicoAlvoPropriedadeRural findOneByChaveSisater(String chaveSisater);

}