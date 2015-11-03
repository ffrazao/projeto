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
 * The persistent class for the meio_contato_email database table.
 * 
 */
@Entity
@Table(name = "email", schema = EntidadeBase.PESSOA_SCHEMA)
@Indexed
public class Email extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Field(index = Index.YES, store = Store.YES)
	private String email;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	public Email() {
	}

	public Email(String email) {
		setEmail(email);
	}

	public String getEmail() {
		return this.email;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
}