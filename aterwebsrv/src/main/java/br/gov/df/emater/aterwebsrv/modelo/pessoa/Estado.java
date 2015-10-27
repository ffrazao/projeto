package br.gov.df.emater.aterwebsrv.modelo.pessoa;

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
 * The persistent class for the estado database table.
 * 
 */
@Entity
@Table(name = "estado", schema = EntidadeBase.PESSOA_SCHEMA)
public class Estado extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	private String codigo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	@ManyToOne
	@JoinColumn(name = "pais_id")
	private Pais pais;

	private String sigla;

	public Estado() {
	}

	public String getCodigo() {
		return codigo;
	}

	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Pais getPais() {
		return pais;
	}

	public String getSigla() {
		return sigla;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

}