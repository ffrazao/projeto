package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaTipo;
import br.gov.df.emater.aterwebsrv.modelo.pendencia.Pendencia;

@Entity
@Table(name = "pessoa_pendencia", schema = EntidadeBase.PESSOA_SCHEMA)
public class PessoaPendencia extends EntidadeBase implements _ChavePrimaria<Integer>, Pendencia<PessoaPendencia> {

	private static final long serialVersionUID = 1L;

	private String codigo;

	@Lob
	private String descricao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "pessoa_id")
	private Pessoa pessoa;

	@Enumerated(EnumType.STRING)
	private PendenciaTipo tipo;

	public PessoaPendencia() {
	}

	public PessoaPendencia(Integer id, PendenciaCodigo codigo, String descricao) {
		this.setId(id);
		this.setCodigo(codigo.name());
		this.setTipo(codigo.getTipo());
		this.setDescricao(descricao);
	}

	public PessoaPendencia(PendenciaCodigo codigo, String descricao) {
		this(null, codigo, descricao);
	}

	public PessoaPendencia(Serializable id) {
		super(id);
	}

	@Override
	public String getCodigo() {
		return codigo;
	}

	@Override
	public String getDescricao() {
		return descricao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	@Transient
	public EntidadeBase getPendenciaDono() {
		return this.getPessoa();
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	@Override
	public PendenciaTipo getTipo() {
		return tipo;
	}

	@Override
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Override
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@Override
	public void setTipo(PendenciaTipo tipo) {
		this.tipo = tipo;
	}

}
