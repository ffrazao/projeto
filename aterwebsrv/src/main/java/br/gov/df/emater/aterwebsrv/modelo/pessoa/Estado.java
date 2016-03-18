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
@Table(name = "estado", schema = EntidadeBase.PESSOA_SCHEMA)
public class Estado extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<Estado> {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private Confirmacao capital;

	private String codigo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	@Enumerated(EnumType.STRING)
	private Confirmacao padrao;

	@ManyToOne
	@JoinColumn(name = "pais_id")
	private Pais pais;

	private String sigla;

	public Estado() {
	}

	public Estado(Integer id, String nome) {
		this();
		this.setId(id);
		this.setNome(nome);
	}

	public Estado(Integer id, String nome, String sigla, Confirmacao padrao, Confirmacao capital) {
		this(id, nome);
		this.setSigla(sigla);
		this.setPadrao(padrao);
		this.setCapital(capital);
	}

	public Confirmacao getCapital() {
		return capital;
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

	public Pais getPais() {
		return pais;
	}

	public String getSigla() {
		return sigla;
	}

	@Override
	public Estado infoBasica() {
		return new Estado(this.id, this.nome, this.sigla, this.padrao, this.capital);
	}

	public void setCapital(Confirmacao capital) {
		this.capital = capital;
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

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

}