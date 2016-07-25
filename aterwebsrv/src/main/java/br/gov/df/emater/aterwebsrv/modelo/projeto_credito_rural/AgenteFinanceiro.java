package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Entity
@Table(name = "agente_financeiro", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class AgenteFinanceiro extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "pessoa_juridica_id")
	private PessoaJuridica pessoaJuridica;

	public AgenteFinanceiro() {
		super();
	}

	public AgenteFinanceiro(Integer id) {
		super(id);
	}

	@Override
	public Integer getId() {
		return id;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

}