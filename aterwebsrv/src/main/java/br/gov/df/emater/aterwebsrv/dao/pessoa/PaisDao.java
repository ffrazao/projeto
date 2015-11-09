package br.gov.df.emater.aterwebsrv.dao.pessoa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pais;

@Repository("PaisDao")
public interface PaisDao extends JpaRepository<Pais, Integer> {

	List<Pais> findByPadrao(Confirmacao padrao);
	
}