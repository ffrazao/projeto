package br.gov.df.emater.aterwebsrv.modelo.funcional;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.GestaoTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;

@Entity
@Table(name = "lotacao_vi", schema = EntidadeBase.FUNCIONAL_SCHEMA)
public class LotacaoVi extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "empregado_id")
	private Integer empregadoId;

	@Column(name = "empregado_nome")
	private String empregadoNome;

	@Column(name = "empregado_situacao")
	@Enumerated(EnumType.STRING)
	private PessoaSituacao empregadoSituacao;

	@Column(name = "emprego_id")
	private Integer empregoId;

	@Column(name = "gestor")
	@Enumerated(EnumType.STRING)
	private GestaoTipo gestor;

	@Id
	private Integer id;

	@Column(name = "inicio")
	private Calendar inicio;

	@Column(name = "termino")
	private Calendar termino;

	@Column(name = "unidade_organizacional_id")
	private Integer unidadeOrganizacionalId;

	@Column(name = "unidade_organizacional_nome")
	private String unidadeOrganizacionalNome;

	@Column(name = "unidade_organizacional_sigla")
	private String unidadeOrganizacionalSigla;

	@Column(name = "unidade_organizacional_situacao")
	@Enumerated(EnumType.STRING)
	private PessoaSituacao unidadeOrganizacionalSituacao;

	public LotacaoVi() {

	}

	public Integer getEmpregadoId() {
		return empregadoId;
	}

	public String getEmpregadoNome() {
		return empregadoNome;
	}

	public PessoaSituacao getEmpregadoSituacao() {
		return empregadoSituacao;
	}

	public Integer getEmpregoId() {
		return empregoId;
	}

	public GestaoTipo getGestor() {
		return gestor;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public Calendar getTermino() {
		return termino;
	}

	public Integer getUnidadeOrganizacionalId() {
		return unidadeOrganizacionalId;
	}

	public String getUnidadeOrganizacionalNome() {
		return unidadeOrganizacionalNome;
	}

	public String getUnidadeOrganizacionalSigla() {
		return unidadeOrganizacionalSigla;
	}

	public PessoaSituacao getUnidadeOrganizacionalSituacao() {
		return unidadeOrganizacionalSituacao;
	}

	public void setEmpregadoId(Integer empregadoId) {
		this.empregadoId = empregadoId;
	}

	public void setEmpregadoNome(String empregadoNome) {
		this.empregadoNome = empregadoNome;
	}

	public void setEmpregadoSituacao(PessoaSituacao empregadoSituacao) {
		this.empregadoSituacao = empregadoSituacao;
	}

	public void setEmpregoId(Integer empregoId) {
		this.empregoId = empregoId;
	}

	public void setGestor(GestaoTipo gestor) {
		this.gestor = gestor;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public void setTermino(Calendar termino) {
		this.termino = termino;
	}

	public void setUnidadeOrganizacionalId(Integer unidadeOrganizacionalId) {
		this.unidadeOrganizacionalId = unidadeOrganizacionalId;
	}

	public void setUnidadeOrganizacionalNome(String unidadeOrganizacionalNome) {
		this.unidadeOrganizacionalNome = unidadeOrganizacionalNome;
	}

	public void setUnidadeOrganizacionalSigla(String unidadeOrganizacionalSigla) {
		this.unidadeOrganizacionalSigla = unidadeOrganizacionalSigla;
	}

	public void setUnidadeOrganizacionalSituacao(PessoaSituacao unidadeOrganizacionalSituacao) {
		this.unidadeOrganizacionalSituacao = unidadeOrganizacionalSituacao;
	}

}