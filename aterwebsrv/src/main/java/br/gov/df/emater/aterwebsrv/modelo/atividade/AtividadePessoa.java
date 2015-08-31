package br.gov.df.emater.aterwebsrv.modelo.atividade;

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
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadePessoaFuncao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Entity
@Table(name = "atividade_pessoa", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class AtividadePessoa extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "atividade_id")
	private Atividade atividade;

	@Enumerated(EnumType.STRING)
	@Column(name = "funcao")
	private AtividadePessoaFuncao funcao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "pessoa_id")
	private Pessoa pessoa;

	public Atividade getAtividade() {
		return atividade;
	}

	public AtividadePessoaFuncao getFuncao() {
		return funcao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public void setFuncao(AtividadePessoaFuncao funcao) {
		this.funcao = funcao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

}