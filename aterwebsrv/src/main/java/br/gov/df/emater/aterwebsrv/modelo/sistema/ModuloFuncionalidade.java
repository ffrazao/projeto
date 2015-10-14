package br.gov.df.emater.aterwebsrv.modelo.sistema;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

/**
 * The persistent class for the modulo database table.
 * 
 */
@Entity
@Table(name = "modulo_funcionalidade", schema = EntidadeBase.SISTEMA_SCHEMA)
public class ModuloFuncionalidade extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "funcionalidade_id")
	private Funcionalidade funcionalidade;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "modulo_id")
	private Modulo modulo;

	public Funcionalidade getFuncionalidade() {
		return funcionalidade;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	public Modulo getModulo() {
		return modulo;
	}

	public void setFuncionalidade(Funcionalidade funcionalidade) {
		this.funcionalidade = funcionalidade;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

}