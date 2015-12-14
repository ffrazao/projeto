package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;

@Entity
@Table(name = "bem_forma_producao_media", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class BemFormaProducaoMedia extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "bem_id")
	private Bem bem;

	@ManyToOne
	@JoinColumn(name = "comunidade_id")
	private Comunidade comunidade;

	@ManyToOne
	@JoinColumn(name = "forma_producao_valor_id")
	private FormaProducaoValor formaProducaoValor;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "item_a_media")
	private BigDecimal itemAMedia;

	@Column(name = "item_b_media")
	private BigDecimal itemBMedia;

	@Column(name = "item_c_media")
	private BigDecimal itemCMedia;

	@ManyToOne
	@JoinColumn(name = "propriedade_rural_id")
	private PropriedadeRural propriedadeRural;

	@ManyToOne
	@JoinColumn(name = "publico_alvo_id")
	private PublicoAlvo publicoAlvo;

	@Column(name = "valor_unitario")
	private BigDecimal valorUnitario;

	public BemFormaProducaoMedia() {
		super();
	}

	public Bem getBem() {
		return bem;
	}

	public Comunidade getComunidade() {
		return comunidade;
	}

	public FormaProducaoValor getFormaProducaoValor() {
		return formaProducaoValor;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public BigDecimal getItemAMedia() {
		return itemAMedia;
	}

	public BigDecimal getItemBMedia() {
		return itemBMedia;
	}

	public BigDecimal getItemCMedia() {
		return itemCMedia;
	}

	public PropriedadeRural getPropriedadeRural() {
		return propriedadeRural;
	}

	public PublicoAlvo getPublicoAlvo() {
		return publicoAlvo;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setBem(Bem bem) {
		this.bem = bem;
	}

	public void setComunidade(Comunidade comunidade) {
		this.comunidade = comunidade;
	}

	public void setFormaProducaoValor(FormaProducaoValor formaProducaoValor) {
		this.formaProducaoValor = formaProducaoValor;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setItemAMedia(BigDecimal itemAMedia) {
		this.itemAMedia = itemAMedia;
	}

	public void setItemBMedia(BigDecimal itemBMedia) {
		this.itemBMedia = itemBMedia;
	}

	public void setItemCMedia(BigDecimal itemCMedia) {
		this.itemCMedia = itemCMedia;
	}

	public void setPropriedadeRural(PropriedadeRural propriedadeRural) {
		this.propriedadeRural = propriedadeRural;
	}

	public void setPublicoAlvo(PublicoAlvo publicoAlvo) {
		this.publicoAlvo = publicoAlvo;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

}