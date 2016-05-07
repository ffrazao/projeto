package br.gov.df.emater.aterwebsrv.modelo.pessoa;

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

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaTipo;

@Entity
@Table(name = "pessoa_pendencia", schema = EntidadeBase.PESSOA_SCHEMA)
public class PessoaPendencia extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

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

	public String getDescricao() {
		return descricao;
	}

	public Integer getId() {
		return id;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public PendenciaTipo getTipo() {
		return tipo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public void setTipo(PendenciaTipo tipo) {
		this.tipo = tipo;
	}

}
