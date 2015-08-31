package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.RelacionamentoFuncaoParticipacao;

/**
 * The persistent class for the relacionamento_funcao database view.
 * 
 */
@Entity
@Table(name = "relacionamento_configuracao_vi", schema = EntidadeBase.PESSOA_SCHEMA)
public class RelacionamentoConfiguracaoVi extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "config_id")
	private Integer id;

	@Column(name = "relacionado_id")
	private Integer relacionadoId;

	@Column(name = "relacionado_nome")
	private String relacionadoNome;

	@Column(name = "relacionado_nome_se_feminino")
	private String relacionadoNomeSeFeminino;

	@Column(name = "relacionado_nome_se_masculino")
	private String relacionadoNomeSeMasculino;

	@Column(name = "relacionado_participacao")
	@Enumerated(EnumType.STRING)
	private RelacionamentoFuncaoParticipacao relacionadoParticipacao;

	@Column(name = "relacionador_id")
	private Integer relacionadorId;

	@Column(name = "relacionador_nome")
	private String relacionadorNome;

	@Column(name = "relacionador_nome_se_feminino")
	private String relacionadorNomeSeFeminino;

	@Column(name = "relacionador_nome_se_masculino")
	private String relacionadorNomeSeMasculino;

	@Column(name = "relacionador_participacao")
	@Enumerated(EnumType.STRING)
	private RelacionamentoFuncaoParticipacao relacionadorParticipacao;

	@Column(name = "tipo_codigo")
	private String tipoCodigo;

	@Column(name = "tipo_gerado_pelo_sistema")
	@Enumerated(EnumType.STRING)
	private Confirmacao tipoGeradoPeloSistema;

	@Column(name = "tipo_id")
	private Integer tipoId;

	@Column(name = "tipo_nome")
	private String tipoNome;

	@Column(name = "tipo_relacionamento_tipo_id")
	private Integer tipoRelacionamentoTipoId;

	@Column(name = "tipo_temporario")
	@Enumerated(EnumType.STRING)
	private Confirmacao tipoTemporario;

	public RelacionamentoConfiguracaoVi() {
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Integer getRelacionadoId() {
		return relacionadoId;
	}

	public String getRelacionadoNome() {
		return relacionadoNome;
	}

	public String getRelacionadoNomeSeFeminino() {
		return relacionadoNomeSeFeminino;
	}

	public String getRelacionadoNomeSeMasculino() {
		return relacionadoNomeSeMasculino;
	}

	public Integer getRelacionadorId() {
		return relacionadorId;
	}

	public String getRelacionadorNome() {
		return relacionadorNome;
	}

	public String getRelacionadorNomeSeFeminino() {
		return relacionadorNomeSeFeminino;
	}

	public String getRelacionadorNomeSeMasculino() {
		return relacionadorNomeSeMasculino;
	}

	public RelacionamentoFuncaoParticipacao getRelacionadorParticipacao() {
		return relacionadorParticipacao;
	}

	public String getTipoCodigo() {
		return tipoCodigo;
	}

	public Integer getTipoId() {
		return tipoId;
	}

	public String getTipoNome() {
		return tipoNome;
	}

	public Integer getTipoRelacionamentoTipoId() {
		return tipoRelacionamentoTipoId;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setRelacionadoId(Integer relacionadoId) {
		this.relacionadoId = relacionadoId;
	}

	public void setRelacionadoNome(String relacionadoNome) {
		this.relacionadoNome = relacionadoNome;
	}

	public void setRelacionadoNomeSeFeminino(String relacionadoNomeSeFeminino) {
		this.relacionadoNomeSeFeminino = relacionadoNomeSeFeminino;
	}

	public void setRelacionadoNomeSeMasculino(String relacionadoNomeSeMasculino) {
		this.relacionadoNomeSeMasculino = relacionadoNomeSeMasculino;
	}

	public void setRelacionadorId(Integer relacionadorId) {
		this.relacionadorId = relacionadorId;
	}

	public void setRelacionadorNome(String relacionadorNome) {
		this.relacionadorNome = relacionadorNome;
	}

	public void setRelacionadorNomeSeFeminino(String relacionadorNomeSeFeminino) {
		this.relacionadorNomeSeFeminino = relacionadorNomeSeFeminino;
	}

	public void setRelacionadorNomeSeMasculino(String relacionadorNomeSeMasculino) {
		this.relacionadorNomeSeMasculino = relacionadorNomeSeMasculino;
	}

	public void setRelacionadorParticipacao(RelacionamentoFuncaoParticipacao relacionadorParticipacao) {
		this.relacionadorParticipacao = relacionadorParticipacao;
	}

	public void setTipoCodigo(String tipoCodigo) {
		this.tipoCodigo = tipoCodigo;
	}

	public void setTipoId(Integer tipoId) {
		this.tipoId = tipoId;
	}

	public void setTipoNome(String tipoNome) {
		this.tipoNome = tipoNome;
	}

	public void setTipoRelacionamentoTipoId(Integer tipoRelacionamentoTipoId) {
		this.tipoRelacionamentoTipoId = tipoRelacionamentoTipoId;
	}

}