package br.gov.df.emater.aterwebsrv.dao.sistema;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Repository
public interface UsuarioDao extends JpaRepository<Usuario, Integer> {

	Usuario findByNomeUsuario(String nomeUsuario);

}