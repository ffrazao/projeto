package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.GrupoSocialTipo;

@Repository("GrupoSocialTipoDao")
public interface GrupoSocialTipoDao extends JpaRepository<GrupoSocialTipo, Integer> {

	GrupoSocialTipo findOneByNome(String nome);

	GrupoSocialTipo findOneByCodigo(GrupoSocialTipo.Codigo codigo);

}
