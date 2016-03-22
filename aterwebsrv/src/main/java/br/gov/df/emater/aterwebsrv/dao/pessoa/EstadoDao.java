package br.gov.df.emater.aterwebsrv.dao.pessoa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Estado;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pais;

@Repository("EstadoDao")
public interface EstadoDao extends JpaRepository<Estado, Integer> {

	List<Estado> findByPadraoAndPais(Confirmacao padrao, Pais pais);

	List<Estado> findByCapitalAndPais(Confirmacao padrao, Pais pais);

	Estado findOneByPaisAndSigla(Pais pais, String sigla);

}