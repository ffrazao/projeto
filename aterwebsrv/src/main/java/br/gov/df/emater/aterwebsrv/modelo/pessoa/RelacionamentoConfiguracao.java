package br.gov.df.emater.aterwebsrv.modelo.pessoa;

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

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ConfirmacaoOpcional;
import br.gov.df.emater.aterwebsrv.modelo.dominio.RelacionamentoParticipacao;

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

	@Enumerated(EnumType.STRING)
	@Column(name = "relacionado_participacao")
	private RelacionamentoParticipacao relacionadoParticipacao;

	@ManyToOne
	@JoinColumn(name = "relacionador_funcao_id")
	private RelacionamentoFuncao relacionadorFuncao;

	@Enumerated(EnumType.STRING)
	@Column(name = "relacionador_participacao")
	private RelacionamentoParticipacao relacionadorParticipacao;

	@ManyToOne
	@JoinColumn(name = "relacionamento_tipo_id")
	private RelacionamentoTipo relacionamentoTipo;

	@Enumerated(EnumType.STRING)
	private ConfirmacaoOpcional temporario;

	public RelacionamentoConfiguracao() {
	}

	@Override
	public Integer getId() {
		return id;
	}

	public RelacionamentoFuncao getRelacionadoFuncao() {
		return relacionadoFuncao;
	}

	public RelacionamentoParticipacao getRelacionadoParticipacao() {
		return relacionadoParticipacao;
	}

	public RelacionamentoFuncao getRelacionadorFuncao() {
		return relacionadorFuncao;
	}

	public RelacionamentoParticipacao getRelacionadorParticipacao() {
		return relacionadorParticipacao;
	}

	public RelacionamentoTipo getRelacionamentoTipo() {
		return relacionamentoTipo;
	}

	public ConfirmacaoOpcional getTemporario() {
		return temporario;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setRelacionadoFuncao(RelacionamentoFuncao relacionadoFuncao) {
		this.relacionadoFuncao = relacionadoFuncao;
	}

	public void setRelacionadoParticipacao(RelacionamentoParticipacao relacionadoParticipacao) {
		this.relacionadoParticipacao = relacionadoParticipacao;
	}

	public void setRelacionadorFuncao(RelacionamentoFuncao relacionadorFuncao) {
		this.relacionadorFuncao = relacionadorFuncao;
	}

	public void setRelacionadorParticipacao(RelacionamentoParticipacao relacionadorParticipacao) {
		this.relacionadorParticipacao = relacionadorParticipacao;
	}

	public void setRelacionamentoTipo(RelacionamentoTipo relacionamentoTipo) {
		this.relacionamentoTipo = relacionamentoTipo;
	}

	public void setTemporario(ConfirmacaoOpcional temporario) {
		this.temporario = temporario;
	}

}