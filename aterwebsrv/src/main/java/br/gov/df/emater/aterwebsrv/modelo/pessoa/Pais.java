package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;

/**
 * The persistent class for the pais database table.
 * 
 */
@Entity
@Table(name = "pais", schema = EntidadeBase.PESSOA_SCHEMA)
public class Pais extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<Pais> {

	private static final long serialVersionUID = 1L;

	private String codigo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	@Enumerated(EnumType.STRING)
	private Confirmacao padrao;

	private String sigla;

	public Pais() {
	}

	public Pais(Integer id, String nome) {
		this();
		this.setId(id);
		this.setNome(nome);
	}

	public Pais(Integer id, String nome, String sigla) {
		this(id, nome);
		this.setSigla(sigla);
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

	public Confirmacao getPadrao() {
		return padrao;
	}

	public String getSigla() {
		return sigla;
	}

	@Override
	public Pais infoBasica() {
		return new Pais(this.id, this.nome, this.sigla);
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

	public void setPadrao(Confirmacao padrao) {
		this.padrao = padrao;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

}