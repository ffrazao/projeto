package br.gov.df.emater.aterwebsrv.modelo.sistema;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

@Entity
@Table(name = "comando", schema = EntidadeBase.SISTEMA_SCHEMA)
public class Comando extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	private String codigo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	public String getCodigo() {
		return codigo;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	public String getNome() {
		return nome;
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
}
