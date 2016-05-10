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

@Entity
@Table(name = "pessoa_grupo_social", schema = EntidadeBase.PESSOA_SCHEMA)
public class PessoaGrupoSocial extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "chave_sisater")
	private String chaveSisater;

	@ManyToOne
	@JoinColumn(name = "grupo_social_id")
	private GrupoSocial grupoSocial;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "pessoa_id")
	private Pessoa pessoa;

	public PessoaGrupoSocial() {
	}

	public PessoaGrupoSocial(Serializable id) {
		super(id);
	}

	public PessoaGrupoSocial(Serializable id, GrupoSocial grupoSocial) {
		super(id);
		this.setGrupoSocial(grupoSocial);
	}

	public String getChaveSisater() {
		return chaveSisater;
	}

	public GrupoSocial getGrupoSocial() {
		return grupoSocial;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setChaveSisater(String chaveSisater) {
		this.chaveSisater = chaveSisater;
	}

	public void setGrupoSocial(GrupoSocial grupoSocial) {
		this.grupoSocial = grupoSocial;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

}
