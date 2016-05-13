package br.gov.df.emater.aterwebsrv.modelo.funcional;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(name = "emprego_vi", schema = EntidadeBase.FUNCIONAL_SCHEMA)
public class EmpregoVi extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar admissao;

	@ManyToOne
	@JoinColumn(name = "cargo_id")
	private Cargo cargo;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar demissao;

	@Column(name = "empregado_apelido")
	private String empregadoApelido;

	@Column(name = "empregado_id")
	private Integer empregadoId;

	@Column(name = "empregado_nome")
	private String empregadoNome;

	@Column(name = "empregador_id")
	private Integer empregadorId;

	@Column(name = "empregador_nome")
	private String empregadorNome;

	@Column(name = "empregador_sigla")
	private String empregadorSigla;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String matricula;

	public Calendar getAdmissao() {
		return admissao;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public Calendar getDemissao() {
		return demissao;
	}

	public String getEmpregadoApelido() {
		return empregadoApelido;
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

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public void setDemissao(Calendar demissao) {
		this.demissao = demissao;
	}

	public void setEmpregadoApelido(String empregadoApelido) {
		this.empregadoApelido = empregadoApelido;
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