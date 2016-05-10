package br.gov.df.emater.aterwebsrv.dao.ater;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.ater.OrganizacaoTipo;

@Repository("OrganizacaoTipoDao")
public interface OrganizacaoTipoDao extends JpaRepository<OrganizacaoTipo, Integer> {

	OrganizacaoTipo findOneByNome(String registro);

}