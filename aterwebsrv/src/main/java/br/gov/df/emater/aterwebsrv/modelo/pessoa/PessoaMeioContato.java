package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.ater.Exploracao;

/**
 * The persistent class for the pessoa_meio_contato database table.
 * 
 */
@Entity
@Table(name = "pessoa_meio_contato", schema = EntidadeBase.PESSOA_SCHEMA)
public class PessoaMeioContato extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@OneToOne(mappedBy = "pessoaMeioContato", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Exploracao exploracao;

	private String finalidade;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "meio_contato_id")
	private MeioContato meioContato;

	private Integer ordem;

	@ManyToOne
	@JoinColumn(name = "pessoa_id")
	private Pessoa pessoa;

	public PessoaMeioContato() {
	}

	public Exploracao getExploracao() {
		return exploracao;
	}

	public String getFinalidade() {
		return finalidade;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public MeioContato getMeioContato() {
		return meioContato;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setExploracao(Exploracao exploracao) {
		this.exploracao = exploracao;
	}

	public void setFinalidade(String finalidade) {
		this.finalidade = finalidade;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setMeioContato(MeioContato meioContato) {
		this.meioContato = meioContato;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

}