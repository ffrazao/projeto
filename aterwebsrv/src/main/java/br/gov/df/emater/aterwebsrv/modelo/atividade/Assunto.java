package br.gov.df.emater.aterwebsrv.modelo.atividade;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeFinalidade;

@Entity
@Table(name = "assunto", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class Assunto extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "pai", fetch = FetchType.LAZY)
	private List<Assunto> filhos;

	@Column(name = "finalidade")
	@Enumerated(EnumType.STRING)
	private AtividadeFinalidade finalidade;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "nome")
	private String nome;

	@ManyToOne
	@JoinColumn(name = "pai_id")
	private Assunto pai;

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

	public List<Assunto> getFilhos() {
		return filhos;
	}

	public AtividadeFinalidade getFinalidade() {
		return finalidade;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Assunto getPai() {
		return pai;
	}

	public void setFilhos(List<Assunto> filhos) {
		this.filhos = filhos;
	}

	public void setFinalidade(AtividadeFinalidade finalidade) {
		this.finalidade = finalidade;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPai(Assunto pai) {
		this.pai = pai;
	}

}