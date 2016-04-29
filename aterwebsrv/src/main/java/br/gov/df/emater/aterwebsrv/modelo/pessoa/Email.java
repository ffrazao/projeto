package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

/**
 * The persistent class for the email database table.
 * 
 */
@Entity
@Table(name = "email", schema = EntidadeBase.PESSOA_SCHEMA)
// @Indexed
public class Email extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<Email> {

	private static final long serialVersionUID = 1L;

	// @Field(index = Index.YES, store = Store.YES)
	private String endereco;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	public Email() {
	}

	public Email(Integer id, String endereco) {
		setId(id);
		setEndereco(endereco);
	}

	public Email(String endereco) {
		setEndereco(endereco);
	}

	public String getEndereco() {
		return endereco;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Email infoBasica() {
		return new Email(getId(), getEndereco());
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
}