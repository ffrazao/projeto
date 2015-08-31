package br.gov.df.emater.aterwebsrv.modelo;

import java.io.Serializable;

/**
 * Interface que define m�todos padr�o para uso de chave prim�ria em entidades.
 * 
 * @param <CP>
 *            tipo de chave prim�ria, deve ser serializ�vel
 * 
 * @author frazao
 * @since 1.0
 */
public interface _ChavePrimaria<CP extends Serializable> extends Serializable {

	/**
	 * Propriedade que representa o id.
	 */
	String P_ID = "id";

	/**
	 * Get da chave primaria.
	 * 
	 * @return Chave Primaria
	 */
	CP getId();

	/**
	 * Set da chave prim�ria.
	 * 
	 * @param id
	 *            chave prim�ria
	 */
	void setId(CP id);
}
