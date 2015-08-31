package br.gov.df.emater.aterwebsrv.modelo.funcional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Relacionamento;

@Entity
@Table(name = "emprego", schema = EntidadeBase.FUNCIONAL_SCHEMA)
@PrimaryKeyJoinColumn(name = "id")
public class Emprego extends Relacionamento {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "cargo_id")
	private Cargo cargo;

	@Column(name = "matricula")
	private String matricula;

	public Cargo getCargo() {
		return cargo;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

}