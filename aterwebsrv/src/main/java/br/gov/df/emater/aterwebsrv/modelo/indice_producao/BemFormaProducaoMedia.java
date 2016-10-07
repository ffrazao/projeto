package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	@JoinColumn(name = "bem_classificado_id")
	private BemClassificado bemClassificado;

	@OneToMany(mappedBy = "bemFormaProducaoMedia")
	private List<BemFormaProducaoMediaComposicao> bemFormaProducaoMediaComposicaoList;

	@ManyToOne
	private Comunidade comunidade;

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

	@Column(name = "valor_total")
	private BigDecimal valorTotal;

	@Column(name = "valor_unitario")
	private BigDecimal valorUnitario;

	@JoinColumn(name = "comunidade_id")

	private BigDecimal volume;

	public BemFormaProducaoMedia() {
		super();
	}

	public BemClassificado getBemClassificado() {
		return bemClassificado;
	}

	public List<BemFormaProducaoMediaComposicao> getBemFormaProducaoMediaComposicaoList() {
		return bemFormaProducaoMediaComposicaoList;
	}

	public Comunidade getComunidade() {
		return comunidade;
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

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setBemClassificado(BemClassificado bemClassificado) {
		this.bemClassificado = bemClassificado;
	}

	public void setBemFormaProducaoMediaComposicaoList(List<BemFormaProducaoMediaComposicao> bemFormaProducaoMediaComposicaoList) {
		this.bemFormaProducaoMediaComposicaoList = bemFormaProducaoMediaComposicaoList;
	}

	public void setComunidade(Comunidade comunidade) {
		this.comunidade = comunidade;
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

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

}