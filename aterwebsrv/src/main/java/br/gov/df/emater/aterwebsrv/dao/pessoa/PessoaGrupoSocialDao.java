package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaGrupoSocial;

@Repository("PessoaGrupoSocialDao")
public interface PessoaGrupoSocialDao extends JpaRepository<PessoaGrupoSocial, Integer> {

}
