package br.gov.df.emater.aterwebsrv.modelo.pessoa;

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

/**
 * The persistent class for the pessoa_relacionamento database table.
 * 
 */
@Entity
@Table(name = "pessoa_relacionamento", schema = EntidadeBase.PESSOA_SCHEMA)
public class PessoaRelacionamento extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	/**
	 * campo fake para manter informa��es do cpf/cnpj do relacionado enquanto o
	 * seu registro principal n�o gerado
	 */
	@Column(name = "cpf_cnpj")
	private String cpfCnpj;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	/**
	 * campo fake para manter informa��es do cpf/cnpj do relacionado enquanto o
	 * seu registro principal n�o gerado
	 */
	private String nome;

	@ManyToOne
	@JoinColumn(name = "pessoa_id")
	private Pessoa pessoa;

	@Column(name = "pode_modificar")
	@Enumerated(EnumType.STRING)
	private Confirmacao podeModificar = Confirmacao.N;

	@Enumerated(EnumType.STRING)
	private Confirmacao proprietario = Confirmacao.N;

	@ManyToOne
	@JoinColumn(name = "relacionamento_id")
	private Relacionamento relacionamento;

	@ManyToOne
	@JoinColumn(name = "relacionamento_funcao_id")
	private RelacionamentoFuncao relacionamentoFuncao;

	public PessoaRelacionamento() {
	}

	public PessoaRelacionamento(Integer id) {
		super(id);
	}

	public PessoaRelacionamento(Integer id, Relacionamento relacionamento, Pessoa pes, RelacionamentoFuncao relacionamentoFuncao, Confirmacao podeModificar, Confirmacao proprietario) {
		this(id);
		setRelacionamento(relacionamento);
		setPessoa(pes);
		setRelacionamentoFuncao(relacionamentoFuncao);
		setPodeModificar(podeModificar);
		setProprietario(proprietario);
	}

	public PessoaRelacionamento(Pessoa pessoa) {
		this();
		setPessoa(pessoa);
	}
	public PessoaRelacionamento(Pessoa pessoa, Relacionamento relacionamento) {
		this(pessoa);
		setRelacionamento(relacionamento);
	}

	public PessoaRelacionamento(Pessoa pessoa, Relacionamento relacionamento, RelacionamentoFuncao funcao) {
		this(pessoa);
		setRelacionamento(relacionamento);
		setRelacionamentoFuncao(funcao);
	}

	public PessoaRelacionamento(Relacionamento relacionamento) {
		this();
		setRelacionamento(relacionamento);
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public Confirmacao getPodeModificar() {
		return podeModificar;
	}

	public Confirmacao getProprietario() {
		return proprietario;
	}

	public Relacionamento getRelacionamento() {
		return relacionamento;
	}

	public RelacionamentoFuncao getRelacionamentoFuncao() {
		return relacionamentoFuncao;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public void setPodeModificar(Confirmacao podeModificar) {
		this.podeModificar = podeModificar;
	}

	public void setProprietario(Confirmacao proprietario) {
		this.proprietario = proprietario;
	}

	public void setRelacionamento(Relacionamento relacionamento) {
		this.relacionamento = relacionamento;
	}

	public void setRelacionamentoFuncao(RelacionamentoFuncao relacionamentoFuncao) {
		this.relacionamentoFuncao = relacionamentoFuncao;
	}

}