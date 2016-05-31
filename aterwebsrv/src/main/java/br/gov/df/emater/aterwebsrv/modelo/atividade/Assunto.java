package br.gov.df.emater.aterwebsrv.modelo.atividade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

@Entity
@Table(name = "assunto", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class Assunto extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	private String finalidade;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "nome")
	private String nome;

	public Assunto() {
		super();
	}

	public Assunto(Integer id) {
		super(id);
	}

	public Assunto(Integer id, String nome) {
		this(id);
		setNome(nome);
	}

	public Assunto(Integer id, String nome, String finalidade) {
		this.setId(id);
		this.setNome(nome);
		this.setFinalidade(finalidade);
	}

	public String getFinalidade() {
		return finalidade;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Assunto infoBasica() {
		return new Assunto(getId(), getNome(), getFinalidade());
	}

	public void setFinalidade(String finalidade) {
		this.finalidade = finalidade;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}