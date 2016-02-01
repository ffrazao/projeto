package br.gov.df.emater.aterwebsrv.modelo.ater;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacionalComunidade;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Cidade;

/**
 * The persistent class for the comunidade database table.
 * 
 */
@Entity
@Table(name = "comunidade", schema = EntidadeBase.ATER_SCHEMA)
public class Comunidade extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<Comunidade> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "cidade_id")
	private Cidade cidade;

	@Column(name = "codigo_sisater")
	private String codigoSisater;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	private String sigla;

	@ManyToOne
	@JoinColumn(name = "unidade_organizacional_id")
	private UnidadeOrganizacional unidadeOrganizacional;

	@OneToMany(mappedBy = "comunidade")
	private List<UnidadeOrganizacionalComunidade> unidadeOrganizacionalComunidadeList;

	public Comunidade() {
	}

	public Comunidade(Integer id) {
		super(id);
	}

	public Comunidade(Integer id, String nome) {
		setId(id);
		setNome(nome);
	}

	public Cidade getCidade() {
		return cidade;
	}

	public String getCodigoSisater() {
		return codigoSisater;
	}

	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getSigla() {
		return sigla;
	}

	public UnidadeOrganizacional getUnidadeOrganizacional() {
		return unidadeOrganizacional;
	}

	public List<UnidadeOrganizacionalComunidade> getUnidadeOrganizacionalComunidadeList() {
		return unidadeOrganizacionalComunidadeList;
	}

	@Override
	public Comunidade infoBasica() {
		return new Comunidade(this.getId(), this.getNome());
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public void setCodigoSisater(String codigoSisater) {
		this.codigoSisater = codigoSisater;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public void setUnidadeOrganizacional(UnidadeOrganizacional unidadeOrganizacional) {
		this.unidadeOrganizacional = unidadeOrganizacional;
	}

	public void setUnidadeOrganizacionalComunidadeList(List<UnidadeOrganizacionalComunidade> unidadeOrganizacionalComunidadeList) {
		this.unidadeOrganizacionalComunidadeList = unidadeOrganizacionalComunidadeList;
	}

}