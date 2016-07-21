package br.gov.df.emater.aterwebsrv.modelo.credito_rural;

import java.math.BigDecimal;

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
import br.gov.df.emater.aterwebsrv.modelo.dominio.GarantiaParticipacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Entity
@Table(name = "garantia", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class Garantia extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Enumerated(EnumType.STRING)
	private GarantiaParticipacao participacao;

	@ManyToOne
	@JoinColumn(name = "pessoa_id")
	private Pessoa pessoa;

	@ManyToOne
	@JoinColumn(name = "projeto_credito_id")
	private ProjetoCreditoRural projetoCredito;

	@Column(name = "renda_liquida")
	private BigDecimal rendaLiquida;

	public Garantia() {
		super();
	}

	public Garantia(Integer id) {
		super(id);
	}

	@Override
	public Integer getId() {
		return id;
	}

	public GarantiaParticipacao getParticipacao() {
		return participacao;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public ProjetoCreditoRural getProjetoCredito() {
		return projetoCredito;
	}

	public BigDecimal getRendaLiquida() {
		return rendaLiquida;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setParticipacao(GarantiaParticipacao participacao) {
		this.participacao = participacao;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public void setProjetoCredito(ProjetoCreditoRural projetoCredito) {
		this.projetoCredito = projetoCredito;
	}
	public void setRendaLiquida(BigDecimal rendaLiquida) {
		this.rendaLiquida = rendaLiquida;
	}

}