package br.gov.df.emater.aterwebsrv.modelo.atividade;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo_planejamento.planejamento.MetaTatica;

@Entity
@Table(name = "atividade_meta_tatica", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class AtividadeMetaTatica extends EntidadeBase
		implements _ChavePrimaria<Integer>, InfoBasica<AtividadeMetaTatica> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "atividade_id")
	private Atividade atividade;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@Transient
	private MetaTatica metaTatica;

	@Column(name = "meta_tatica_id")
	private Integer metaTaticaId;

	@Column(name = "meta_tatica_nome")
	private String metaTaticaNome;

	public AtividadeMetaTatica() {
		super();
	}

	public AtividadeMetaTatica(Integer id, Atividade atividade, MetaTatica metaTatica, Integer metaTaticaId,
			String metaTaticaNome) {
		super();
		this.id = id;
		this.atividade = atividade;
		this.metaTatica = metaTatica;
		this.metaTaticaId = metaTaticaId;
		this.metaTaticaNome = metaTaticaNome;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public Integer getId() {
		return id;
	}

	public MetaTatica getMetaTatica() {
		return metaTatica;
	}

	public Integer getMetaTaticaId() {
		return metaTaticaId;
	}

	public String getMetaTaticaNome() {
		return metaTaticaNome;
	}

	@Override
	public AtividadeMetaTatica infoBasica() {
		return new AtividadeMetaTatica(this.id, new Atividade(this.atividade == null ? null : this.atividade.getId()),
				infoBasicaReg(this.metaTatica), this.metaTaticaId, this.metaTaticaNome);
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setMetaTatica(MetaTatica metaTatica) {
		this.metaTatica = metaTatica;
	}

	public void setMetaTaticaId(Integer metaTaticaId) {
		this.metaTaticaId = metaTaticaId;
	}

	public void setMetaTaticaNome(String metaTaticaNome) {
		this.metaTaticaNome = metaTaticaNome;
	}

}