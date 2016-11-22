package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;

/**
 * The persistent class for the estado database table.
 * 
 */
@Entity
@Table(name = "municipio", schema = EntidadeBase.PESSOA_SCHEMA)
public class Municipio extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<Municipio> {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private Confirmacao capital;

	private String codigo;

	@ManyToOne
	@JoinColumn(name = "estado_id")
	private Estado estado;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	@Enumerated(EnumType.STRING)
	private Confirmacao padrao;

	private String sigla;

	public Municipio() {
	}

	public Municipio(Integer id, String nome) {
		this();
		this.setId(id);
		this.setNome(nome);
	}

	public Municipio(Integer id, String nome, String sigla, Confirmacao padrao, Confirmacao capital) {
		this(id, nome);
		this.setSigla(sigla);
		this.setPadrao(padrao);
		this.setCapital(capital);
	}

	public Municipio(Serializable id) {
		super(id);
	}

	public Confirmacao getCapital() {
		return capital;
	}

	public String getCodigo() {
		return codigo;
	}

	public Estado getEstado() {
		return estado;
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
	public Municipio infoBasica() {
		return new Municipio(this.id, this.nome, this.sigla, this.padrao, this.capital);
	}

	public void setCapital(Confirmacao capital) {
		this.capital = capital;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
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