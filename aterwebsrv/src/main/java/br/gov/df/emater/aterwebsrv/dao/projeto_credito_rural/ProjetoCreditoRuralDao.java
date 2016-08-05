package br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.atividade.Atividade;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;

@Repository("ProjetoCreditoRuralDao")
public interface ProjetoCreditoRuralDao extends JpaRepository<ProjetoCreditoRural, Integer>, ProjetoCreditoRuralDaoCustom {

	ProjetoCreditoRural findOneByAtividade(Atividade atividade);

}