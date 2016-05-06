package br.gov.df.emater.aterwebsrv.modelo.ater;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Cidade;

/**
 * The persistent class for the comunidade database table.
 * 
 */
@Entity
@Table(name = "comunidade", schema = EntidadeBase.ATER_SCHEMA)
public class Comunidade extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<Comunidade> {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	protected Confirmacao assentamento;

	@Column(name = "chave_sisater")
	private String chaveSisater;

	@ManyToOne
	@JoinColumn(name = "cidade_id")
	private Cidade cidade;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	private String sigla;

	@ManyToOne
	@JoinColumn(name = "unidade_organizacional_id")
	private UnidadeOrganizacional unidadeOrganizacional;

	public Comunidade() {
	}

	public Comunidade(Integer id) {
		super(id);
	}

	public Comunidade(Integer id, String nome, UnidadeOrganizacional unidadeOrganizacional) {
		setId(id);
		setNome(nome);
		setUnidadeOrganizacional(unidadeOrganizacional);
	}

	public Confirmacao getAssentamento() {
		return assentamento;
	}

	public String getChaveSisater() {
		return chaveSisater;
	}

	public Cidade getCidade() {
		return cidade;
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

	@Override
	public Comunidade infoBasica() {
		return new Comunidade(this.getId(), this.getNome(), this.unidadeOrganizacional == null ? null : this.unidadeOrganizacional.infoBasica());
	}

	public void setAssentamento(Confirmacao assentamento) {
		this.assentamento = assentamento;
	}

	public void setChaveSisater(String chaveSisater) {
		this.chaveSisater = chaveSisater;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
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

}