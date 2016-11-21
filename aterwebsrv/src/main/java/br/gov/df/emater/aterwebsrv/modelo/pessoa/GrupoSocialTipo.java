package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;

/**
 * The persistent class for the grupo_social_tipo database table.
 * 
 */
@Entity
@Table(name = "grupo_social_tipo", schema = EntidadeBase.PESSOA_SCHEMA)
public class GrupoSocialTipo extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<GrupoSocialTipo> {

	public static enum Codigo {
		PROGRAMA_SOCIAL, UNIDADE_ORGANIZACIONAL;
	}

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private Codigo codigo;

	@Enumerated(EnumType.STRING)
	@Column(name = "existe_fisicamente")
	private Confirmacao existeFisicamente;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	public GrupoSocialTipo() {
	}

	public GrupoSocialTipo(Codigo codigo) {
		this.codigo = codigo;
	}

	public GrupoSocialTipo(Integer id) {
		super(id);
	}

	public GrupoSocialTipo(Integer id, String nome, Codigo codigo, Confirmacao existeFisicamente) {
		this.setId(id);
		this.setNome(nome);
		this.setCodigo(codigo);
		this.setExisteFisicamente(existeFisicamente);
	}

	public Codigo getCodigo() {
		return codigo;
	}

	public Confirmacao getExisteFisicamente() {
		return existeFisicamente;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public GrupoSocialTipo infoBasica() {
		return new GrupoSocialTipo(this.id, this.nome, this.codigo, this.existeFisicamente);
	}

	public void setCodigo(Codigo codigo) {
		this.codigo = codigo;
	}

	public void setExisteFisicamente(Confirmacao existeFisicamente) {
		this.existeFisicamente = existeFisicamente;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}