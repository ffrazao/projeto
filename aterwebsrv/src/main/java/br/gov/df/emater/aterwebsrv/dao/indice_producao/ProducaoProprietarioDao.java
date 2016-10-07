package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;

@Repository("ProducaoProprietarioDao")
public interface ProducaoProprietarioDao extends JpaRepository<ProducaoProprietario, Integer>, ProducaoProprietarioDaoCustom {

	List<ProducaoProprietario> findByAnoAndBemClassificadoAndPropriedadeRuralComunidadeUnidadeOrganizacional(Integer ano, BemClassificado bemClassificado, UnidadeOrganizacional unidadeOrganizacional);

	ProducaoProprietario findOneByAnoAndBemClassificadoAndPublicoAlvoAndPropriedadeRuralAndUnidadeOrganizacionalIsNull(Integer ano, BemClassificado bemClassificado, PublicoAlvo publicoAlvo, PropriedadeRural propriedadeRural);

	ProducaoProprietario findOneByAnoAndBemClassificadoAndUnidadeOrganizacionalAndPublicoAlvoIsNullAndPropriedadeRuralIsNull(Integer ano, BemClassificado bemClassificado, UnidadeOrganizacional unidadeOrganizacional);

	ProducaoProprietario findOneByChaveSisater(String chaveSisater);

}
