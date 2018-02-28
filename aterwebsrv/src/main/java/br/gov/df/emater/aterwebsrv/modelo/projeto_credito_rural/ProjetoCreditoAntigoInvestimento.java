package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;

@Entity
@Table(name = "antigo_projeto_credito_investimento", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class ProjetoCreditoAntigoInvestimento extends EntidadeBase {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "projeto_antigo_id")
	private ProjetoCreditoAntigo projetoCreditoAntigo;
	
	@Column(name = "idund")
	private String idUnidade;
	
	@Column(name = "idben")
	private String idBeneficiario;
	
	@Column(name = "idprp")
	private String idPropriedade;
	
	@Column(name = "idcdt")
	private String idCdt;
	
	@Column(name = "unidade")
	private String unidade;
	
	private Float quantidade;

	private String descricao;
	
	@Column(name = "unidade_medida")
	private String unidadeDeMedida;

	private String liberacao;
	
	@Column(name = "valunit")
	private Float valorUnitario;
	
	@Column(name = "valorcado")
	private Float valorOrcado;
	
	@Column(name = "valproprio")
	private Float valorProprio;
	
	@Column(name = "valperprop")
	private Float valorPerProp;
	
	@Column(name = "valfinaciar")
	private Float valorFinanciado;
	
	
	


	public ProjetoCreditoAntigoInvestimento(Integer id, ProjetoCreditoAntigo projetoCreditoAntigo, String idUnidade,
			String idBeneficiario, String idPropriedade, String idCdt, String unidade, Float quantidade,
			String descricao, String unidadeDeMedida, String liberacao, Float valorUnitario, Float valorOrcado,
			Float valorProprio, Float valorPerProp, Float valorFinanciado) {
		super();
		this.id = id;
		this.projetoCreditoAntigo = projetoCreditoAntigo;
		this.idUnidade = idUnidade;
		this.idBeneficiario = idBeneficiario;
		this.idPropriedade = idPropriedade;
		this.idCdt = idCdt;
		this.unidade = unidade;
		this.quantidade = quantidade;
		this.descricao = descricao;
		this.unidadeDeMedida = unidadeDeMedida;
		this.liberacao = liberacao;
		this.valorUnitario = valorUnitario;
		this.valorOrcado = valorOrcado;
		this.valorProprio = valorProprio;
		this.valorPerProp = valorPerProp;
		this.valorFinanciado = valorFinanciado;
	}


	public ProjetoCreditoAntigoInvestimento() {
		super();
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public ProjetoCreditoAntigo getProjetoCreditoAntigo() {
		return projetoCreditoAntigo;
	}


	public void setProjetoCreditoAntigo(ProjetoCreditoAntigo projetoCreditoAntigo) {
		this.projetoCreditoAntigo = projetoCreditoAntigo;
	}


	public String getIdUnidade() {
		return idUnidade;
	}


	public void setIdUnidade(String idUnidade) {
		this.idUnidade = idUnidade;
	}


	public String getIdBeneficiario() {
		return idBeneficiario;
	}


	public void setIdBeneficiario(String idBeneficiario) {
		this.idBeneficiario = idBeneficiario;
	}


	public String getIdPropriedade() {
		return idPropriedade;
	}


	public void setIdPropriedade(String idPropriedade) {
		this.idPropriedade = idPropriedade;
	}


	public String getIdCdt() {
		return idCdt;
	}


	public void setIdCdt(String idCdt) {
		this.idCdt = idCdt;
	}


	public String getUnidade() {
		return unidade;
	}


	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}


	public Float getQuantidade() {
		return quantidade;
	}


	public void setQuantidade(Float quantidade) {
		this.quantidade = quantidade;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public String getUnidadeDeMedida() {
		return unidadeDeMedida;
	}


	public void setUnidadeDeMedida(String unidadeDeMedida) {
		this.unidadeDeMedida = unidadeDeMedida;
	}


	public String getLiberacao() {
		return liberacao;
	}


	public void setLiberacao(String liberacao) {
		this.liberacao = liberacao;
	}


	public Float getValorUnitario() {
		return valorUnitario;
	}


	public void setValorUnitario(Float valorUnitario) {
		this.valorUnitario = valorUnitario;
	}


	public Float getValorOrcado() {
		return valorOrcado;
	}


	public void setValorOrcado(Float valorOrcado) {
		this.valorOrcado = valorOrcado;
	}


	public Float getValorProprio() {
		return valorProprio;
	}


	public void setValorProprio(Float valorProprio) {
		this.valorProprio = valorProprio;
	}


	public Float getValorPerProp() {
		return valorPerProp;
	}


	public void setValorPerProp(Float valorPerProp) {
		this.valorPerProp = valorPerProp;
	}


	public Float getValorFinanciado() {
		return valorFinanciado;
	}


	public void setValorFinanciado(Float valorFinanciado) {
		this.valorFinanciado = valorFinanciado;
	}
	
	
}