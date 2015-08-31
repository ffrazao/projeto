package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;

/**
 * The persistent class for the pessoa_grupo_estado_vi database table.
 * 
 */
@Entity
@Table(name = "pessoa_grupo_estado_vi", schema = EntidadeBase.PESSOA_SCHEMA)
public class PessoaGrupoEstadoVi extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	private String codigo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pessoa_grupo_id")
	private PessoaGrupoPaisVi pessoaGrupoPaisVi;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pessoa_grupo_tipo_id")
	private PessoaGrupoTipo pessoaGrupoTipo;

	private String nome;

	@Column(name = "padrao")
	@Enumerated(EnumType.STRING)
	private Confirmacao padrao;

	private String sigla;

	public PessoaGrupoEstadoVi() {
	}

	public PessoaGrupoEstadoVi(Integer id) {
		setId(id);
	}

	public PessoaGrupoEstadoVi(Integer id, String nome) {
		this(id);
		setNome(nome);
	}

	public String getCodigo() {
		return codigo;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public PessoaGrupoPaisVi getPessoaGrupoPaisVi() {
		return pessoaGrupoPaisVi;
	}

	public PessoaGrupoTipo getPessoaGrupoTipo() {
		return pessoaGrupoTipo;
	}

	public String getNome() {
		return nome;
	}

	public Confirmacao getPadrao() {
		return padrao;
	}

	public String getSigla() {
		return sigla;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPessoaGrupoPaisVi(PessoaGrupoPaisVi pessoaGrupoPaisVi) {
		this.pessoaGrupoPaisVi = pessoaGrupoPaisVi;
	}

	public void setPessoaGrupoTipo(PessoaGrupoTipo pessoaGrupoTipo) {
		this.pessoaGrupoTipo = pessoaGrupoTipo;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPadrao(Confirmacao padrao) {
		this.padrao = padrao;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

}