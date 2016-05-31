package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.io.Serializable;

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
 * The persistent class for the cnae database table.
 * 
 */
@Entity
@Table(name = "pessoa_juridica_cnae", schema = EntidadeBase.PESSOA_SCHEMA)
public class PessoaJuridicaCnae extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "cnae_id")
	private Cnae cnae;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "pessoa_juridica_id")
	private PessoaJuridica pessoaJuridica;

	public PessoaJuridicaCnae() {
	}

	public PessoaJuridicaCnae(Serializable id) {
		super(id);
	}

	public Cnae getCnae() {
		return cnae;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setCnae(Cnae cnae) {
		this.cnae = cnae;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

}