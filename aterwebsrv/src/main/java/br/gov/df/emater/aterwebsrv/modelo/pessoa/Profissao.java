package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

/**
 * The persistent class for the profissao database table.
 * 
 */
@Entity
@Table(name = "profissao", schema = EntidadeBase.PESSOA_SCHEMA)
public class Profissao extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<Profissao> {

	private static final long serialVersionUID = 1L;

	@Column(name = "codigo_cbo")
	private String codigoCbo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	public Profissao() {
	}

	public Profissao(Integer id, String nome) {
		this.setId(id);
		this.setNome(nome);
	}

	public String getCodigoCbo() {
		return codigoCbo;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setCodigoCbo(String codigoCbo) {
		this.codigoCbo = codigoCbo;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public Profissao infoBasica() {
		return new Profissao(this.id, this.nome);
	}

}