package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.dominio.RelacionamentoParticipacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoConfiguracaoVi;

@Repository("RelacionamentoConfiguracaoViDao")
public interface RelacionamentoConfiguracaoViDao extends JpaRepository<RelacionamentoConfiguracaoVi, Integer> {

	RelacionamentoConfiguracaoVi findByTipoIdAndRelacionadorId(Integer tipoId, Integer RelacionadorId);

	RelacionamentoConfiguracaoVi findOneByTipoCodigoAndRelacionadorParticipacao(String tipoCodigo, RelacionamentoParticipacao participacao);

}
