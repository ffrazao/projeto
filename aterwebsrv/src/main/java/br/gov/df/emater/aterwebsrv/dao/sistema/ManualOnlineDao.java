package br.gov.df.emater.aterwebsrv.dao.sistema;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.sistema.ManualOnline;

@Repository("ManualOnlineDao")
public interface ManualOnlineDao extends JpaRepository<ManualOnline, Integer> {

	ManualOnline findOneByCodigo(String codigo);

}