package br.gov.df.emater.aterwebsrv.dao.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.Telefone;

@Repository("TelefoneDao")
public interface TelefoneDao extends JpaRepository<Telefone, Integer> {
	
	Telefone findByNumero(String numero);
	
}