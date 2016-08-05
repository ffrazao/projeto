package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

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
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.GarantiaParticipacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;

@Entity
@Table(name = "projeto_credito_rural_garantia", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class ProjetoCreditoRuralGarantia extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<ProjetoCreditoRuralGarantia> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Enumerated(EnumType.STRING)
	private GarantiaParticipacao participacao;

	@ManyToOne
	@JoinColumn(name = "pessoa_fisica_id")
	private PessoaFisica pessoaFisica;

	@ManyToOne
	@JoinColumn(name = "projeto_credito_rural_id")
	private ProjetoCreditoRural projetoCreditoRural;

	@Column(name = "renda_liquida")
	private BigDecimal rendaLiquida;

	public ProjetoCreditoRuralGarantia() {
		super();
	}

	public ProjetoCreditoRuralGarantia(Integer id) {
		super(id);
	}

	public ProjetoCreditoRuralGarantia(Integer id, GarantiaParticipacao participacao, PessoaFisica pessoaFisica, BigDecimal rendaLiquida) {
		super();
		this.id = id;
		this.participacao = participacao;
		this.pessoaFisica = pessoaFisica;
		this.rendaLiquida = rendaLiquida;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public GarantiaParticipacao getParticipacao() {
		return participacao;
	}

	public PessoaFisica getPessoaFisica() {
		return pessoaFisica;
	}

	public ProjetoCreditoRural getProjetoCreditoRural() {
		return projetoCreditoRural;
	}

	public BigDecimal getRendaLiquida() {
		return rendaLiquida;
	}

	@Override
	public ProjetoCreditoRuralGarantia infoBasica() {
		return new ProjetoCreditoRuralGarantia(this.id, this.participacao, (PessoaFisica) infoBasicaReg(this.pessoaFisica), this.rendaLiquida);
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setParticipacao(GarantiaParticipacao participacao) {
		this.participacao = participacao;
	}

	public void setPessoaFisica(PessoaFisica pessoaFisica) {
		this.pessoaFisica = pessoaFisica;
	}

	public void setProjetoCreditoRural(ProjetoCreditoRural projetoCreditoRural) {
		this.projetoCreditoRural = projetoCreditoRural;
	}

	public void setRendaLiquida(BigDecimal rendaLiquida) {
		this.rendaLiquida = rendaLiquida;
	}

}