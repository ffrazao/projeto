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
@Table(name = "assunto_acao", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class AssuntoAcao extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "acao_id")
	private Acao acao;

	@ManyToOne
	@JoinColumn(name = "assunto_id")
	private Assunto assunto;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	public AssuntoAcao() {
		super();
	}

	public AssuntoAcao(Integer id) {
		super(id);
	}

	public Acao getAcao() {
		return acao;
	}

	public Assunto getAssunto() {
		return assunto;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setAcao(Acao acao) {
		this.acao = acao;
	}

	public void setAssunto(Assunto assunto) {
		this.assunto = assunto;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

}
