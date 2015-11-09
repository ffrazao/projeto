package br.gov.df.emater.aterwebsrv.modelo.pessoa;

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
@Table(name = "cidade", schema = EntidadeBase.PESSOA_SCHEMA)
public class Cidade extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<Cidade> {

	private static final long serialVersionUID = 1L;

	private String codigo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "municipio_id")
	private Municipio municipio;

	private String nome;

	@Enumerated(EnumType.STRING)
	private Confirmacao padrao;

	@Enumerated(EnumType.STRING)
	private Confirmacao principal;

	private String sigla;

	public Cidade() {
	}

	public Cidade(Integer id, String nome, String sigla) {
		this(id, nome);
		this.setSigla(sigla);
	}

	public Cidade(Integer id, String nome) {
		this();
		this.setId(id);
		this.setNome(nome);
	}

	public String getCodigo() {
		return codigo;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public String getNome() {
		return nome;
	}

	public Confirmacao getPadrao() {
		return padrao;
	}

	public Confirmacao getPrincipal() {
		return principal;
	}

	public String getSigla() {
		return sigla;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPadrao(Confirmacao padrao) {
		this.padrao = padrao;
	}

	public void setPrincipal(Confirmacao principal) {
		this.principal = principal;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	@Override
	public Cidade infoBasica() {
		return new Cidade(this.id, this.nome, this.sigla);
	}

}