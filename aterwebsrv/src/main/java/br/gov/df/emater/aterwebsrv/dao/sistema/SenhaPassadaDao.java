package br.gov.df.emater.aterwebsrv.dao.sistema;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.sistema.SenhaPassada;

@Repository("SenhaPassadaDao")
public interface SenhaPassadaDao extends JpaRepository<SenhaPassada, Integer> {

	SenhaPassada findOneByUsuarioIdAndSenha(Integer usuarioId, String senha);

}