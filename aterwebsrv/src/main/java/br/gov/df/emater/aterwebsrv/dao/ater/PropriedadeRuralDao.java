package br.gov.df.emater.aterwebsrv.dao.ater;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;

@Repository("PropriedadeRuralDao")
public interface PropriedadeRuralDao extends JpaRepository<PropriedadeRural, Integer>, PropriedadeRuralDaoCustom {

	PropriedadeRural findOneByChaveSisater(String chaveSisater);

}
