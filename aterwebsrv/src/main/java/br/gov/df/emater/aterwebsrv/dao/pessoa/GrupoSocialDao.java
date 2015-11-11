package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.GrupoSocial;

@Repository("GrupoSocialDao")
public interface GrupoSocialDao extends JpaRepository<GrupoSocial, Integer> {

	GrupoSocial findByNome(String nome);

}
