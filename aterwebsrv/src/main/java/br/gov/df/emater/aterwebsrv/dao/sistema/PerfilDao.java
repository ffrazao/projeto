package br.gov.df.emater.aterwebsrv.dao.sistema;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.sistema.Perfil;

@Repository("PerfilDao")
public interface PerfilDao extends JpaRepository<Perfil, Integer>, PerfilDaoCustom {

	Perfil findOneByCodigo(String codigo);

}