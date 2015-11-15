package br.gov.df.emater.aterwebsrv.modelo.funcional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;

@Entity
@Table(name = "unidade_organizacional_comunidade", schema = EntidadeBase.FUNCIONAL_SCHEMA)
public class UnidadeOrganizacionalComunidade extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "unidade_organizacional_id")
	private UnidadeOrganizacional unidadeOrganizacional;

	@ManyToOne
	@JoinColumn(name = "comunidade_id")
	private Comunidade comunidade;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public UnidadeOrganizacional getUnidadeOrganizacional() {
		return unidadeOrganizacional;
	}

	public void setUnidadeOrganizacional(UnidadeOrganizacional unidadeOrganizacional) {
		this.unidadeOrganizacional = unidadeOrganizacional;
	}

	public Comunidade getComunidade() {
		return comunidade;
	}

	public void setComunidade(Comunidade comunidade) {
		this.comunidade = comunidade;
	}

}