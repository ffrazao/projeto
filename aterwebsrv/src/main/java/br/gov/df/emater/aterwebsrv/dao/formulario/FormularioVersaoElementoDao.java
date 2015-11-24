package br.gov.df.emater.aterwebsrv.dao.formulario;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersaoElemento;

public interface FormularioVersaoElementoDao extends JpaRepository<FormularioVersaoElemento, Integer> {

}