package br.gov.df.emater.aterwebsrv.modelo.formulario;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;

@Entity
@Table(schema = EntidadeBase.FORMULARIO_SCHEMA)
public class Observar extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private Confirmacao colecao;

	@ManyToOne
	@JoinColumn(name = "elemento_id")
	private Elemento elemento;

	@Lob
	private String funcao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	public Confirmacao getColecao() {
		return colecao;
	}

	public Elemento getElemento() {
		return elemento;
	}

	public String getFuncao() {
		return funcao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setColecao(Confirmacao colecao) {
		this.colecao = colecao;
	}

	public void setElemento(Elemento elemento) {
		this.elemento = elemento;
	}

	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}