package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

/**
 * The persistent class for the cnae database table.
 * 
 */
@Entity
@Table(name = "cnae_subclasse", schema = EntidadeBase.PESSOA_SCHEMA)
public class CnaeSubclasse extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	private String codigo;

	private String descricao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	public CnaeSubclasse() {
	}

	public CnaeSubclasse(Serializable id) {
		super(id);
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getId() {
		return id;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}