package br.gov.df.emater.aterwebsrv.modelo.ater;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

/**
 * The persistent class for the bacia_hidrografica database table.
 * 
 */
@Entity
@Table(name = "bacia_hidrografica", schema = EntidadeBase.ATER_SCHEMA)
public class BaciaHidrografica extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<BaciaHidrografica> {

	private static final long serialVersionUID = 1L;

	private String codigo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	private String sigla;

	public BaciaHidrografica() {
	}

	public BaciaHidrografica(Serializable id) {
		super(id);
	}

	public BaciaHidrografica(Serializable id, String nome) {
		this(id);
		setNome(nome);
	}

	public String getCodigo() {
		return codigo;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getSigla() {
		return sigla;
	}

	@Override
	public BaciaHidrografica infoBasica() {
		// TODO Auto-generated method stub
		return new BaciaHidrografica(this.id, this.nome);
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

}