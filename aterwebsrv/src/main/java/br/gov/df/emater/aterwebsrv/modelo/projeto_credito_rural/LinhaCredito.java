package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

@Entity
@Table(name = "linha_credito", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class LinhaCredito extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<LinhaCredito> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	public LinhaCredito() {
		super();
	}

	public LinhaCredito(Integer id) {
		super(id);
	}

	public LinhaCredito(Integer id, String nome) {
		this.id = id;
		this.nome = nome;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public LinhaCredito infoBasica() {
		return new LinhaCredito(this.id, this.nome);
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}