package br.gov.df.emater.aterwebsrv.dao.indice_producao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Bem;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemFormaProducaoMedia;

@Repository("BemFormaProducaoMediaDao")
public interface BemFormaProducaoMediaDao extends JpaRepository<BemFormaProducaoMedia, Integer> {

	List<BemFormaProducaoMedia> findByBemAndPropriedadeRuralAndPublicoAlvo(Bem bem, PropriedadeRural propriedadeRural, PublicoAlvo publicoAlvo);

}
