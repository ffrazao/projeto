package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

/**
 * The persistent class for the pessoa_relacionamento database table.
 * 
 */
@Entity
@Table(name = "pessoa_relacionamento_cidade_bacia_hidrografica_vi", schema = EntidadeBase.PESSOA_SCHEMA)
public class PessoaRelacionamentoCidadeBaciaHidrograficaVi extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "cid_id")
	private Integer cidId;

	@Column(name = "cid_nome")
	private String cidNome;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "relacionamento_id")
	private Integer id;

	@Column(name = "x_id")
	private Integer xId;

	@Column(name = "x_nome")
	private String xNome;

	public PessoaRelacionamentoCidadeBaciaHidrograficaVi() {
	}

	public Integer getCidId() {
		return cidId;
	}

	public String getCidNome() {
		return cidNome;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Integer getxId() {
		return xId;
	}

	public String getxNome() {
		return xNome;
	}

	public void setCidId(Integer cidId) {
		this.cidId = cidId;
	}

	public void setCidNome(String cidNome) {
		this.cidNome = cidNome;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setxId(Integer xId) {
		this.xId = xId;
	}

	public void setxNome(String xNome) {
		this.xNome = xNome;
	}

}