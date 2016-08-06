package br.gov.df.emater.aterwebsrv.dao;

import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.Dto;

public interface _FiltrarCustom<E extends Dto> {

	List<? extends Object> filtrar(E filtro);

}
