package br.gov.df.emater.aterwebsrv.modelo.pessoa;

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
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.TelefoneTipo;

/**
 * The persistent class for the pessoa_telefone database table.
 * 
 */
@Entity
@Table(name = "pessoa_telefone", schema = EntidadeBase.PESSOA_SCHEMA)
public class PessoaTelefone extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "chave_sisater")
	private String chaveSisater;

	private String finalidade;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private Integer ordem;

	@ManyToOne
	@JoinColumn(name = "pessoa_id")
	private Pessoa pessoa;

	@Enumerated(EnumType.STRING)
	protected Confirmacao principal;

	@ManyToOne
	@JoinColumn(name = "telefone_id")
	private Telefone telefone;

	@Enumerated(EnumType.STRING)
	private TelefoneTipo tipo;

	public PessoaTelefone() {
	}

	public PessoaTelefone(Serializable id) {
		super(id);
	}

	public PessoaTelefone(Serializable id, Telefone telefone, String finalidade, TelefoneTipo tipo) {
		this(id);
		setTelefone(telefone);
		setFinalidade(finalidade);
		setTipo(tipo);
	}

	public String getChaveSisater() {
		return chaveSisater;
	}

	public String getFinalidade() {
		return finalidade;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public Confirmacao getPrincipal() {
		return principal;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public TelefoneTipo getTipo() {
		return tipo;
	}

	public void setChaveSisater(String chaveSisater) {
		this.chaveSisater = chaveSisater;
	}

	public void setFinalidade(String finalidade) {
		this.finalidade = finalidade;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public void setPrincipal(Confirmacao principal) {
		this.principal = principal;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public void setTipo(TelefoneTipo tipo) {
		this.tipo = tipo;
	}

}