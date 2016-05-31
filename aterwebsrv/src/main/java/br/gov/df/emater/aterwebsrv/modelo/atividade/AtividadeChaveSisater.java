package br.gov.df.emater.aterwebsrv.modelo.atividade;

import javax.persistence.Column;
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
@Table(name = "atividade_chave_sisater", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class AtividadeChaveSisater extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "atividade_id")
	private Atividade atividade;

	@Column(name = "chave_sisater")
	private String chaveSisater;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	public AtividadeChaveSisater() {
		super();
	}

	public AtividadeChaveSisater(Integer id) {
		super(id);
	}

	public AtividadeChaveSisater(String chaveSisater) {
		this.setChaveSisater(chaveSisater);
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public String getChaveSisater() {
		return chaveSisater;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public void setChaveSisater(String chaveSisater) {
		this.chaveSisater = chaveSisater;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

}