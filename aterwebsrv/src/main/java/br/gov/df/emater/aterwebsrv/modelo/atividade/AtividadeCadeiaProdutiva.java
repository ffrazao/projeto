package br.gov.df.emater.aterwebsrv.modelo.atividade;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

@Entity
@Table(name = "atividade_cadeia_produtiva", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class AtividadeCadeiaProdutiva extends EntidadeBase
		implements _ChavePrimaria<Integer>, InfoBasica<AtividadeCadeiaProdutiva> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "atividade_id")
	private Atividade atividade;

	@ManyToOne
	@JoinColumn(name = "cadeia_produtiva_id")
	private CadeiaProdutiva cadeiaProdutiva;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	public AtividadeCadeiaProdutiva() {
		super();
	}

	public AtividadeCadeiaProdutiva(CadeiaProdutiva cadeiaProdutiva) {
		this(null, cadeiaProdutiva);
	}

	public AtividadeCadeiaProdutiva(Integer id) {
		super(id);
	}

	public AtividadeCadeiaProdutiva(Integer id, CadeiaProdutiva cadeiaProdutiva) {
		super(id);
		setCadeiaProdutiva(cadeiaProdutiva);
	}

	public AtividadeCadeiaProdutiva(Integer id, CadeiaProdutiva cadeiaProdutiva, Atividade atividade) {
		super();
		this.id = id;
		this.cadeiaProdutiva = cadeiaProdutiva;
		this.atividade = atividade;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public CadeiaProdutiva getCadeiaProdutiva() {
		return cadeiaProdutiva;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public AtividadeCadeiaProdutiva infoBasica() {
		return new AtividadeCadeiaProdutiva(this.id, infoBasicaReg(this.cadeiaProdutiva),
				new Atividade(this.atividade == null ? null : this.atividade.getId()));
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public void setCadeiaProdutiva(CadeiaProdutiva cadeiaProdutiva) {
		this.cadeiaProdutiva = cadeiaProdutiva;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

}