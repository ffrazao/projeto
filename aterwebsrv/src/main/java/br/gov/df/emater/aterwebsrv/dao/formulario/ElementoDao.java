package br.gov.df.emater.aterwebsrv.dao.formulario;

import org.springframework.data.jpa.repository.JpaRepository;

import br.gov.df.emater.aterwebsrv.modelo.formulario.Elemento;

public interface ElementoDao extends JpaRepository<Elemento, Integer> {

}