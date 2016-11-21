package br.gov.df.emater.aterwebsrv.modelo.funcional;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UnidadeOrganizacionalClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.GrupoSocial;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.GrupoSocialTipo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

/*
 * ATENÇÃO! Qualquer alteração na estrutura desta classe também deve ser feita na interface UnidadeOrganizacionalBase 
 */
@Entity
@Table(name = "unidade_organizacional", schema = EntidadeBase.FUNCIONAL_SCHEMA)
@PrimaryKeyJoinColumn(name = "id")
public class UnidadeOrganizacional extends GrupoSocial implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "chave_sisater")
	private String chaveSisater;

	@Enumerated(EnumType.STRING)
	private UnidadeOrganizacionalClassificacao classificacao;

	@OneToMany(mappedBy = "unidadeOrganizacional")
	private List<Comunidade> comunidadeList;

	@ManyToOne
	@JoinColumn(name = "pessoa_juridica_id")
	private PessoaJuridica pessoaJuridica;

	public UnidadeOrganizacional() {
	}

	public UnidadeOrganizacional(Serializable id) {
		super(id);
	}

	public UnidadeOrganizacional(Serializable id, String nome, String apelidoSigla, PessoaJuridica pessoaJuridica, UnidadeOrganizacionalClassificacao classificacao) {
		this(id);
		setNome(nome);
		setApelidoSigla(apelidoSigla);
		setPessoaJuridica(pessoaJuridica);
		setClassificacao(classificacao);
		setPessoaTipo(PessoaTipo.GS);
		setGrupoSocialTipo(new GrupoSocialTipo(GrupoSocialTipo.Codigo.UNIDADE_ORGANIZACIONAL));
	}

	@Override
	public String getChaveSisater() {
		return chaveSisater;
	}

	public UnidadeOrganizacionalClassificacao getClassificacao() {
		return classificacao;
	}

	public List<Comunidade> getComunidadeList() {
		return comunidadeList;
	}

	@Override
	public String getNome() {
		return nome;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	@Override
	public UnidadeOrganizacional infoBasica() {
		return new UnidadeOrganizacional(this.id, this.nome, this.apelidoSigla, this.pessoaJuridica == null ? null : (PessoaJuridica) pessoaJuridica.infoBasica(), this.classificacao);
	}

	@Override
	public void setChaveSisater(String chaveSisater) {
		this.chaveSisater = chaveSisater;
	}

	public void setClassificacao(UnidadeOrganizacionalClassificacao classificacao) {
		this.classificacao = classificacao;
	}

	public void setComunidadeList(List<Comunidade> comunidadeList) {
		this.comunidadeList = comunidadeList;
	}

	@Override
	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

}