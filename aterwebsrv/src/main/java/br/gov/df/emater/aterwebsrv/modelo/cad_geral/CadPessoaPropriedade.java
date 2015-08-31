package br.gov.df.emater.aterwebsrv.modelo.cad_geral;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

@Entity
@Table(name = "cad_propriedade", schema = EntidadeBase.CAD_GERAL_SCHEMA)
public class CadPessoaPropriedade extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cad_pessoa_id")
	private CadPessoa cadPessoa;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cad_propriedade_id")
	private CadPropriedade cadPropriedade;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	public CadPessoaPropriedade() {
	}

	public CadPessoa getCadPessoa() {
		return cadPessoa;
	}

	public CadPropriedade getCadPropriedade() {
		return cadPropriedade;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setCadPessoa(CadPessoa cadPessoa) {
		this.cadPessoa = cadPessoa;
	}

	public void setCadPropriedade(CadPropriedade cadPropriedade) {
		this.cadPropriedade = cadPropriedade;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

}