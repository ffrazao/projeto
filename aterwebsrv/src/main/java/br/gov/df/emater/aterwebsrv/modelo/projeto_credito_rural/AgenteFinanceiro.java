package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaList;
import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Entity
@Table(name = "agente_financeiro", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class AgenteFinanceiro extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<AgenteFinanceiro> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToMany(mappedBy = "agenteFinanceiro")
	private List<AgenteFinanceiroLinhaCredito> linhaCreditoList;

	@ManyToOne
	@JoinColumn(name = "pessoa_juridica_id")
	private PessoaJuridica pessoaJuridica;

	public AgenteFinanceiro() {
		super();
	}

	public AgenteFinanceiro(Integer id) {
		super(id);
	}

	public AgenteFinanceiro(Integer id, PessoaJuridica pessoaJuridica, List<AgenteFinanceiroLinhaCredito> linhaCreditoList) {
		this.id = id;
		this.pessoaJuridica = pessoaJuridica;
		this.linhaCreditoList = linhaCreditoList;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public List<AgenteFinanceiroLinhaCredito> getLinhaCreditoList() {
		return linhaCreditoList;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	@Override
	public AgenteFinanceiro infoBasica() {
		return new AgenteFinanceiro(getId(), (PessoaJuridica) infoBasicaReg(getPessoaJuridica()), infoBasicaList(getLinhaCreditoList()));
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setLinhaCreditoList(List<AgenteFinanceiroLinhaCredito> linhaCreditoList) {
		this.linhaCreditoList = linhaCreditoList;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

}