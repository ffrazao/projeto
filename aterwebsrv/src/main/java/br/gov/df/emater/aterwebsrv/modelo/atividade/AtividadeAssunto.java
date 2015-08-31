package br.gov.df.emater.aterwebsrv.modelo.atividade;

import javax.persistence.Column;
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
@Table(name = "atividade_assunto", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class AtividadeAssunto extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "assunto_acao_id")
	private AssuntoAcao assuntoAcao;

	@ManyToOne
	@JoinColumn(name = "atividade_id")
	private Atividade atividade;

	@Column(name = "descricao")
	@Lob
	private String descricao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "transversal")
	@Enumerated(EnumType.STRING)
	private Confirmacao transversal;

	public AtividadeAssunto() {
		super();
	}

	public AtividadeAssunto(Integer id) {
		super(id);
	}

	public AssuntoAcao getAssuntoAcao() {
		return assuntoAcao;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Confirmacao getTransversal() {
		return transversal;
	}

	public void setAssuntoAcao(AssuntoAcao assuntoAcao) {
		this.assuntoAcao = assuntoAcao;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setTransversal(Confirmacao transversal) {
		this.transversal = transversal;
	}

}