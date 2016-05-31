package br.gov.df.emater.aterwebsrv.dao.formulario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Coleta;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

public interface ColetaDao extends JpaRepository<Coleta, Integer> {

	List<Coleta> findByFormularioVersaoAndPessoa(FormularioVersao fv, Pessoa pessoa);

	List<Coleta> findByFormularioVersaoAndPropriedadeRural(FormularioVersao fv, PropriedadeRural propriedadeRural);

	Coleta findOneByChaveSisater(String chaveColetaFormulario);

}