package br.gov.df.emater.aterwebsrv.modelo.atividade;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

@Entity
@Table(name = "atividade_assunto", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class AtividadeAssunto extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "assunto_id")
	private Assunto assunto;

	@ManyToOne
	@JoinColumn(name = "atividade_id")
	private Atividade atividade;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String observacao;

	public AtividadeAssunto() {
		super();
	}

	public AtividadeAssunto(Assunto assunto) {
		this(null, assunto);
	}

	public AtividadeAssunto(Integer id) {
		super(id);
	}

	public AtividadeAssunto(Integer id, Assunto assunto) {
		super(id);
		setAssunto(assunto);
	}

	public Assunto getAssunto() {
		return assunto;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setAssunto(Assunto assunto) {
		this.assunto = assunto;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

}