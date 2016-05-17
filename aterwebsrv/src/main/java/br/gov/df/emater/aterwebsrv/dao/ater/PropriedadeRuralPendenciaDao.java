package br.gov.df.emater.aterwebsrv.dao.ater;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRuralPendencia;

@Repository("PropriedadeRuralPendenciaDao")
public interface PropriedadeRuralPendenciaDao extends JpaRepository<PropriedadeRuralPendencia, Integer> {

	PropriedadeRuralPendencia findOneByPropriedadeRuralAndCodigo(PropriedadeRural propriedadeRural, String codigo);

}