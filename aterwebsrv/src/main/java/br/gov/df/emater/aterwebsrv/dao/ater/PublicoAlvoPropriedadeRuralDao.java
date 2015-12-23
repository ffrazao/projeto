package br.gov.df.emater.aterwebsrv.dao.ater;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;

public interface PublicoAlvoPropriedadeRuralDao extends JpaRepository<PublicoAlvoPropriedadeRural, Integer> {

	List<PublicoAlvoPropriedadeRural> findByComunidadeAndPublicoAlvoAndPropriedadeRuralIdIsNotNull(Comunidade comunidade, PublicoAlvo publicoAlvo);

}