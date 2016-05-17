package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Bem;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;

@Repository("ProducaoDao")
public interface ProducaoDao extends JpaRepository<Producao, Integer>, ProducaoDaoCustom {

	List<Producao> findByAnoAndBemAndPropriedadeRuralComunidadeUnidadeOrganizacional(Integer ano, Bem bem, UnidadeOrganizacional unidadeOrganizacional);

	Producao findOneByAnoAndBemAndUnidadeOrganizacionalAndPublicoAlvoIsNullAndPropriedadeRuralIsNull(Integer ano, Bem bem, UnidadeOrganizacional unidadeOrganizacional);

	Producao findOneByChaveSisater(String chaveSisater);

	Producao findOneByAnoAndBemAndPublicoAlvoAndPropriedadeRuralAndUnidadeOrganizacionalIsNull(Integer ano, Bem bem, PublicoAlvo publicoAlvo, PropriedadeRural propriedadeRural);

}
