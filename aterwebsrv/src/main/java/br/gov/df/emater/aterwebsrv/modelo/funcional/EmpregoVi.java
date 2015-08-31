package br.gov.df.emater.aterwebsrv.modelo.funcional;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "emprego_vi", schema = EntidadeBase.FUNCIONAL_SCHEMA)
public class EmpregoVi extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar admissao;

	@Column(name = "cargo_id")
	private Integer cargoId;

	@Column(name = "cargo_nome")
	private String cargoNome;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar demissao;

	@Column(name = "empregado_id")
	private Integer empregadoId;

	@Column(name = "empregado_nome")
	private String empregadoNome;

	@Column(name = "empregador_id")
	private Integer empregadorId;

	@Column(name = "empregador_nome")
	private String empregadorNome;

	@Column(name = "empregador_padrao")
	@Enumerated(EnumType.STRING)
	private Confirmacao empregadorPadrao;

	@Column(name = "empregador_sigla")
	private String empregadorSigla;

	@Id
	private Integer id;

	@Column(name = "matricula")
	private String matricula;

	public EmpregoVi() {
	}

	public Calendar getAdmissao() {
		return admissao;
	}

	public Integer getCargoId() {
		return cargoId;
	}

	public String getCargoNome() {
		return cargoNome;
	}

	public Calendar getDemissao() {
		return demissao;
	}

	public Integer getEmpregadoId() {
		return empregadoId;
	}

	public String getEmpregadoNome() {
		return empregadoNome;
	}

	public Integer getEmpregadorId() {
		return empregadorId;
	}

	public String getEmpregadorNome() {
		return empregadorNome;
	}

	public Confirmacao getEmpregadorPadrao() {
		return empregadorPadrao;
	}

	public String getEmpregadorSigla() {
		return empregadorSigla;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setAdmissao(Calendar admissao) {
		this.admissao = admissao;
	}

	public void setCargoId(Integer cargoId) {
		this.cargoId = cargoId;
	}

	public void setCargoNome(String cargoNome) {
		this.cargoNome = cargoNome;
	}

	public void setDemissao(Calendar demissao) {
		this.demissao = demissao;
	}

	public void setEmpregadoId(Integer empregadoId) {
		this.empregadoId = empregadoId;
	}

	public void setEmpregadoNome(String empregadoNome) {
		this.empregadoNome = empregadoNome;
	}

	public void setEmpregadorId(Integer empregadorId) {
		this.empregadorId = empregadorId;
	}

	public void setEmpregadorNome(String empregadorNome) {
		this.empregadorNome = empregadorNome;
	}

	public void setEmpregadorPadrao(Confirmacao empregadorPadrao) {
		this.empregadorPadrao = empregadorPadrao;
	}

	public void setEmpregadorSigla(String empregadorSigla) {
		this.empregadorSigla = empregadorSigla;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

}