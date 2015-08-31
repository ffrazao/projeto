package br.gov.df.emater.aterwebsrv.modelo.funcional;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Entity
@Table(name = "empregador", schema = EntidadeBase.FUNCIONAL_SCHEMA)
@PrimaryKeyJoinColumn(name = "id")
public class Empregador extends PessoaJuridica {

	private static final long serialVersionUID = 1L;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "empregador_cargo", schema = EntidadeBase.FUNCIONAL_SCHEMA, joinColumns = { @JoinColumn(name = "empregador_id") }, inverseJoinColumns = { @JoinColumn(name = "cargo_id") })
	private List<Cargo> cargoList;

	@Column(name = "padrao")
	@Enumerated(EnumType.STRING)
	private Confirmacao padrao;

	public Empregador() {
	}

	public List<Cargo> getCargoList() {
		return cargoList;
	}

	public Confirmacao getPadrao() {
		return padrao;
	}

	public void setCargoList(List<Cargo> cargoList) {
		this.cargoList = cargoList;
	}

	public void setPadrao(Confirmacao padrao) {
		this.padrao = padrao;
	}

}