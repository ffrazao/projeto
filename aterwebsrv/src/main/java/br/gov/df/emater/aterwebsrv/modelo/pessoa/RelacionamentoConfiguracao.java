package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

/**
 * The persistent class for the relacionamento_configuracao database table.
 * 
 */
@Entity
@Table(name = "relacionamento_configuracao", schema = EntidadeBase.PESSOA_SCHEMA)
public class RelacionamentoConfiguracao extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "relacionado_funcao_id")
	private RelacionamentoFuncao relacionadoFuncao;

	@ManyToOne
	@JoinColumn(name = "relacionador_funcao_id")
	private RelacionamentoFuncao relacionadorFuncao;

	@ManyToOne
	@JoinColumn(name = "relacionamento_tipo_id")
	private RelacionamentoTipo relacionamentoTipo;

	public RelacionamentoConfiguracao() {
	}

	public RelacionamentoConfiguracao(RelacionamentoTipo relacionamentoTipo) {
		setRelacionamentoTipo(relacionamentoTipo);
	}

	public RelacionamentoConfiguracao(RelacionamentoTipo relacionamentoTipo, RelacionamentoFuncao relacionadoFuncao) {
		this(relacionamentoTipo);
		setRelacionadoFuncao(relacionadoFuncao);
	}

	@Override
	public Integer getId() {
		return id;
	}

	public RelacionamentoFuncao getRelacionadoFuncao() {
		return relacionadoFuncao;
	}

	public RelacionamentoFuncao getRelacionadorFuncao() {
		return relacionadorFuncao;
	}

	public RelacionamentoTipo getRelacionamentoTipo() {
		return relacionamentoTipo;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setRelacionadoFuncao(RelacionamentoFuncao relacionadoFuncao) {
		this.relacionadoFuncao = relacionadoFuncao;
	}

	public void setRelacionadorFuncao(RelacionamentoFuncao relacionadorFuncao) {
		this.relacionadorFuncao = relacionadorFuncao;
	}

	public void setRelacionamentoTipo(RelacionamentoTipo relacionamentoTipo) {
		this.relacionamentoTipo = relacionamentoTipo;
	}

}