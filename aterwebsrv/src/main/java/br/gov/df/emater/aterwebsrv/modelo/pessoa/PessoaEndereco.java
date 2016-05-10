package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.io.Serializable;

import javax.persistence.Column;
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
 * The persistent class for the pessoa_endereco database table.
 * 
 */
@Entity
@Table(name = "pessoa_endereco", schema = EntidadeBase.PESSOA_SCHEMA)
public class PessoaEndereco extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "chave_sisater")
	private String chaveSisater;

	@ManyToOne
	@JoinColumn(name = "endereco_id")
	private Endereco endereco;

	private String finalidade;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private Integer ordem;

	@ManyToOne
	@JoinColumn(name = "pessoa_id")
	private Pessoa pessoa;

	public PessoaEndereco() {
	}

	public PessoaEndereco(Serializable id) {
		super(id);
	}

	public PessoaEndereco(Serializable id, Endereco endereco, String finalidade) {
		super(id);
		this.setEndereco(endereco);
		this.setFinalidade(finalidade);
	}

	public String getChaveSisater() {
		return chaveSisater;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public String getFinalidade() {
		return finalidade;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setChaveSisater(String chaveSisater) {
		this.chaveSisater = chaveSisater;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public void setFinalidade(String finalidade) {
		this.finalidade = finalidade;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

}