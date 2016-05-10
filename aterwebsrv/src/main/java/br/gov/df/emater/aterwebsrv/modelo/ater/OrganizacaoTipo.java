package br.gov.df.emater.aterwebsrv.modelo.ater;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

/**
 * The persistent class for the organizacao_tipo database table.
 * 
 */
@Entity
@Table(name = "organizacao_tipo", schema = EntidadeBase.ATER_SCHEMA)
public class OrganizacaoTipo extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	public OrganizacaoTipo() {
	}

	public OrganizacaoTipo(Serializable id) {
		super(id);
		this.setNome(nome);
	}

	public OrganizacaoTipo(Serializable id, String nome) {
		this(id);
		this.setNome(nome);
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}