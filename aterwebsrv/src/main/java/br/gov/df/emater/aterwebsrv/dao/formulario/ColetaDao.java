package br.gov.df.emater.aterwebsrv.dao.formulario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Coleta;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

public interface ColetaDao extends JpaRepository<Coleta, Integer> {

	List<Coleta> findByFormularioVersaoAndPessoa(FormularioVersao fv, Pessoa pessoa);

	List<Coleta> findByFormularioVersaoAndPropriedadeRural(FormularioVersao fv, PropriedadeRural propriedadeRural);

	Coleta findOneByChaveSisater(String chaveColetaFormulario);
	
	@Query("SELECT c.id FROM Coleta c WHERE c.pessoa.id =:pessoa")
	List<Integer> findOneByPessoa(@Param("pessoa") Integer pessoa);
	
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Coleta SET pessoa.id =:pessoa WHERE id =:id")
	void UpdatePessoa(@Param("pessoa") Integer pessoa, @Param("id") Integer id);

}