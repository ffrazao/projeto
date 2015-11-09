package br.gov.df.emater.aterwebsrv.dao.pessoa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Estado;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Municipio;

@Repository("MunicipioDao")
public interface MunicipioDao extends JpaRepository<Municipio, Integer> {

	List<Municipio> findByPadraoAndEstado(Confirmacao padrao, Estado estado);

	List<Municipio> findByCapitalAndEstado(Confirmacao padrao, Estado estado);

}