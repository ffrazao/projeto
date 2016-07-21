package br.gov.df.emater.aterwebsrv.dao.credito_rural;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.credito_rural.ProjetoCreditoRural;

@Repository("ProjetoCreditoRuralDao")
public interface ProjetoCreditoRuralDao extends JpaRepository<ProjetoCreditoRural, Integer>, ProjetoCreditoRuralDaoCustom {

}