package br.gov.df.emater.aterwebsrv.modelo.funcional;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaList;
import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Relacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoTipo;

@Entity
@Table(name = "emprego", schema = EntidadeBase.FUNCIONAL_SCHEMA)
@PrimaryKeyJoinColumn(name = "id")
public class Emprego extends Relacionamento {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "cargo_id")
	private Cargo cargo;

	private String matricula;

	private String registro;

	public Emprego() {
		super();
	}

	public Emprego(Cargo cargo, String matricula) {
		super();
		this.cargo = cargo;
		this.matricula = matricula;
	}

	public Emprego(Integer id, Calendar inicio, Calendar termino, RelacionamentoTipo relacionamentoTipo) {
		super(id, inicio, termino, relacionamentoTipo);
	}

	public Cargo getCargo() {
		return cargo;
	}

	public String getMatricula() {
		return matricula;
	}

	@Override
	public Emprego infoBasica() {
		Emprego result = new Emprego(getId(), getInicio(), getTermino(), getRelacionamentoTipo().infoBasica());
		result.setCargo(infoBasicaReg(this.cargo));
		result.setMatricula(this.matricula);
		result.setRegistro(this.registro);
		result.setPessoaRelacionamentoList(infoBasicaList(this.getPessoaRelacionamentoList()));
		
		return result;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getRegistro() {
		return registro;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
	}

}