package br.gov.df.emater.aterwebsrv.modelo.funcional;

import java.io.Serializable;

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
import br.gov.df.emater.aterwebsrv.modelo.dominio.Escolaridade;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Entity
@Table(name = "cargo", schema = EntidadeBase.FUNCIONAL_SCHEMA)
public class Cargo extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<Cargo> {

	private static final long serialVersionUID = 1L;

	@Column(name = "codigo_externo")
	private String codigoExterno;

	@Enumerated(EnumType.STRING)
	private Confirmacao efetivo;

	@Enumerated(EnumType.STRING)
	private Escolaridade escolaridade;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	@ManyToOne
	@JoinColumn(name = "pessoa_juridica_id")
	private PessoaJuridica pessoaJuridica;

	@ManyToOne
	@JoinColumn(name = "tabela_cbo_id")
	private TabelaCbo tabelaCbo;

	public Cargo() {
		super();
	}

	public Cargo(Integer id, PessoaJuridica pessoaJuridica, String nome, String codigoExterno, Confirmacao efetivo, Escolaridade escolaridade) {
		super();
		this.id = id;
		this.pessoaJuridica = pessoaJuridica;
		this.nome = nome;
		this.codigoExterno = codigoExterno;
		this.efetivo = efetivo;
		this.escolaridade = escolaridade;
	}

	public Cargo(Serializable id) {
		super(id);
	}

	public String getCodigoExterno() {
		return codigoExterno;
	}

	public Confirmacao getEfetivo() {
		return efetivo;
	}

	public Escolaridade getEscolaridade() {
		return escolaridade;
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

	public TabelaCbo getTabelaCbo() {
		return tabelaCbo;
	}

	@Override
	public Cargo infoBasica() {
		return new Cargo(this.id, this.pessoaJuridica == null ? null : new PessoaJuridica(this.pessoaJuridica.getId()), this.nome, this.codigoExterno, this.efetivo, this.escolaridade);
	}

	public void setCodigoExterno(String codigoExterno) {
		this.codigoExterno = codigoExterno;
	}

	public void setEfetivo(Confirmacao efetivo) {
		this.efetivo = efetivo;
	}

	public void setEscolaridade(Escolaridade escolaridade) {
		this.escolaridade = escolaridade;
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

	public void setTabelaCbo(TabelaCbo tabelaCbo) {
		this.tabelaCbo = tabelaCbo;
	}

}