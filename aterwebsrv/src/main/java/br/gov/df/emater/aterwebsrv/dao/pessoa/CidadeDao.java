package br.gov.df.emater.aterwebsrv.dao.pessoa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Cidade;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Municipio;

@Repository("CidadeDao")
public interface CidadeDao extends JpaRepository<Cidade, Integer> {

	List<Cidade> findByPadraoAndMunicipio(Confirmacao padrao, Municipio municipio);

	List<Cidade> findByPrincipalAndMunicipio(Confirmacao padrao, Municipio municipio);

}