package br.gov.df.emater.aterwebsrv.modelo.funcional;

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
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UnidadeOrganizacionalClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Entity
@Table(name = "unidade_organizacional", schema = EntidadeBase.FUNCIONAL_SCHEMA)
public class UnidadeOrganizacional extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private UnidadeOrganizacionalClassificacao classificacao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	@ManyToOne
	@JoinColumn(name = "pessoa_juridica_id")
	private PessoaJuridica pessoaJuridica;

	private String sigla;

	public UnidadeOrganizacionalClassificacao getClassificacao() {
		return classificacao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public String getSigla() {
		return sigla;
	}

	public void setClassificacao(UnidadeOrganizacionalClassificacao classificacao) {
		this.classificacao = classificacao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

}
