package br.gov.df.emater.aterwebsrv.dao.ater;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.ater.FormaUtilizacaoEspacoRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRuralFormaUtilizacaoEspacoRural;

@Repository("PropriedadeRuralFormaUtilizacaoEspacoRuralDao")
public interface PropriedadeRuralFormaUtilizacaoEspacoRuralDao extends JpaRepository<PropriedadeRuralFormaUtilizacaoEspacoRural, Integer> {

	PropriedadeRuralFormaUtilizacaoEspacoRural findOneByPropriedadeRuralAndFormaUtilizacaoEspacoRural(PropriedadeRural propriedadeRural, FormaUtilizacaoEspacoRural formaUtilizacaoEspacoRural);

}