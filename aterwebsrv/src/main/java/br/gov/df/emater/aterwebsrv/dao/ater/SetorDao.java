package br.gov.df.emater.aterwebsrv.dao.ater;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.ater.Setor;

@Repository("SetorDao")
public interface SetorDao extends JpaRepository<Setor, Integer> {

	Setor findOneByNome(String registro);

}