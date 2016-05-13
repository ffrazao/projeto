package br.gov.df.emater.aterwebsrv.dao.formulario;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.df.emater.aterwebsrv.modelo.formulario.Formulario;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersao;

public interface FormularioVersaoDao extends JpaRepository<FormularioVersao, Integer> {

	Integer countByFormulario(Formulario formulario);

	FormularioVersao findOneByFormularioCodigoAndVersao(String codigoFormulario, int i);

}