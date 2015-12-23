package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Bem;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;

@Repository("ProducaoDao")
public interface ProducaoDao extends JpaRepository<Producao, Integer>, ProducaoDaoCustom {

	List<Producao> findByAnoAndBemAndPropriedadeRuralComunidade(Integer ano, Bem bem, Comunidade comunidade);

	Producao findOneByAnoAndBemAndComunidadeAndPublicoAlvoIsNullAndPropriedadeRuralIsNull(Integer ano, Bem bem, Comunidade comunidade);

}
