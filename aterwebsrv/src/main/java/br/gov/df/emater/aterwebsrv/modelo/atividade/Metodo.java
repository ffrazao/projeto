package br.gov.df.emater.aterwebsrv.modelo.atividade;

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
import br.gov.df.emater.aterwebsrv.modelo.dominio.MetodoCodigo;

@Entity
@Table(name = "metodo", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class Metodo extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<Metodo> {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private Confirmacao ativo;

	@Enumerated(EnumType.STRING)
	private MetodoCodigo codigo;

	private String finalidade;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "nome")
	private String nome;

	public Metodo() {
		super();
	}

	public Metodo(Integer id) {
		super(id);
	}

	public Metodo(Integer id, String nome) {
		super(id);
		setNome(nome);
	}

	public Metodo(Integer id, String nome, MetodoCodigo codigo, Confirmacao ativo, String finalidade) {
		this(id, nome);
		setCodigo(codigo);
		setAtivo(ativo);
		setFinalidade(finalidade);
	}

	public Confirmacao getAtivo() {
		return ativo;
	}

	public MetodoCodigo getCodigo() {
		return codigo;
	}

	public String getFinalidade() {
		return finalidade;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public Metodo infoBasica() {
		return new Metodo(getId(), getNome(), getCodigo(), getAtivo(), getFinalidade());
	}

	public void setAtivo(Confirmacao ativo) {
		this.ativo = ativo;
	}

	public void setCodigo(MetodoCodigo codigo) {
		this.codigo = codigo;
	}

	public void setFinalidade(String finalidade) {
		this.finalidade = finalidade;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}