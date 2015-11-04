package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

/**
 * The persistent class for the telefone database table.
 * 
 */
@Entity
@Table(name = "telefone", schema = EntidadeBase.PESSOA_SCHEMA)
@Indexed
public class Telefone extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Field(index = Index.YES, store = Store.YES)
	private String numero;

	public Telefone() {
	}

	public Telefone(String numero) {
		setNumero(numero);
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNumero() {
		return this.numero;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
}