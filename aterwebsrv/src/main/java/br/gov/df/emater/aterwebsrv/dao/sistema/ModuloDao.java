package br.gov.df.emater.aterwebsrv.dao.sistema;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Modulo;

@Repository("ModuloDao")
public interface ModuloDao extends JpaRepository<Modulo, Integer> {
	
	List<Modulo> findByPrincipal(Confirmacao principal);

	Modulo findOneByPrincipal(Confirmacao confirmacao);
	
}