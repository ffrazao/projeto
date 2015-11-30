package br.gov.df.emater.aterwebsrv.dao.formulario;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.df.emater.aterwebsrv.modelo.formulario.Formulario;

public interface FormularioDao extends JpaRepository<Formulario, Integer>, FormularioDaoCustom {

	Formulario findByCodigo(String codigo);
	
}