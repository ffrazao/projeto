package br.gov.df.emater.aterwebsrv.modelo.atividade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeRestricaoTempo;

@Entity
@Table(name = "atividade_restricao", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class AtividadeRestricao extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "atividade_id")
	@NotNull
	private Atividade atividade;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "referenciada_atividade_id")
	@NotNull
	private Atividade referenciadaAtividade;

	@Enumerated(EnumType.STRING)
	@Column(name="restricao_tempo")
	@NotNull
	private AtividadeRestricaoTempo restricaoTempo;

	public Atividade getAtividade() {
		return atividade;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Atividade getReferenciadaAtividade() {
		return referenciadaAtividade;
	}

	public AtividadeRestricaoTempo getRestricaoTempo() {
		return restricaoTempo;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setReferenciadaAtividade(Atividade referenciadaAtividade) {
		this.referenciadaAtividade = referenciadaAtividade;
	}

	public void setRestricaoTempo(AtividadeRestricaoTempo restricaoTempo) {
		this.restricaoTempo = restricaoTempo;
	}

}