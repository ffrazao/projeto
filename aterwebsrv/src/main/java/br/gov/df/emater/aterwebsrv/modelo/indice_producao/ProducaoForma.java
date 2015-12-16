package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import java.math.BigDecimal;
import java.util.Calendar;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(name = "producao_forma", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class ProducaoForma extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "alteracao_data", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar alteracaoData;

	@ManyToOne
	@JoinColumn(name = "alteracao_usuario_id")
	private Usuario alteracaoUsuario;

	@Column(name = "data_confirmacao")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar dataConfirmacao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "inclusao_data", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inclusaoData;

	@ManyToOne
	@JoinColumn(name = "inclusao_usuario_id")
	private Usuario inclusaoUsuario;

	@Column(name = "item_a_valor")
	private BigDecimal itemAValor;

	@Column(name = "item_b_valor")
	private BigDecimal itemBValor;

	@Column(name = "item_c_valor")
	private BigDecimal itemCValor;

	@ManyToOne
	@JoinColumn(name = "producao_id")
	private Producao producao;

	@OneToMany(mappedBy = "producaoForma")
	private List<ProducaoFormaComposicao> producaoFormaComposicaoList;

	@Column(name = "quantidade_produtores")
	private Integer quantidadeProdutores;

	@Column(name = "valor_total")
	private BigDecimal valorTotal;

	@Column(name = "valor_unitario")
	private BigDecimal valorUnitario;

	private BigDecimal volume;

	public ProducaoForma() {
		super();
	}

	public Calendar getAlteracaoData() {
		return alteracaoData;
	}

	public Usuario getAlteracaoUsuario() {
		return alteracaoUsuario;
	}

	public Calendar getDataConfirmacao() {
		return dataConfirmacao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Calendar getInclusaoData() {
		return inclusaoData;
	}

	public Usuario getInclusaoUsuario() {
		return inclusaoUsuario;
	}

	public BigDecimal getItemAValor() {
		return itemAValor;
	}

	public BigDecimal getItemBValor() {
		return itemBValor;
	}

	public BigDecimal getItemCValor() {
		return itemCValor;
	}

	public Producao getProducao() {
		return producao;
	}

	public List<ProducaoFormaComposicao> getProducaoFormaComposicaoList() {
		return producaoFormaComposicaoList;
	}

	public Integer getQuantidadeProdutores() {
		return quantidadeProdutores;
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

	public void setAlteracaoData(Calendar alteracaoData) {
		this.alteracaoData = alteracaoData;
	}

	public void setAlteracaoUsuario(Usuario alteracaoUsuario) {
		this.alteracaoUsuario = alteracaoUsuario;
	}

	public void setDataConfirmacao(Calendar dataConfirmacao) {
		this.dataConfirmacao = dataConfirmacao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setInclusaoData(Calendar inclusaoData) {
		this.inclusaoData = inclusaoData;
	}

	public void setInclusaoUsuario(Usuario inclusaoUsuario) {
		this.inclusaoUsuario = inclusaoUsuario;
	}

	public void setItemAValor(BigDecimal itemAValor) {
		this.itemAValor = itemAValor;
	}

	public void setItemBValor(BigDecimal itemBValor) {
		this.itemBValor = itemBValor;
	}

	public void setItemCValor(BigDecimal itemCValor) {
		this.itemCValor = itemCValor;
	}

	public void setProducao(Producao producao) {
		this.producao = producao;
	}

	public void setProducaoFormaComposicaoList(List<ProducaoFormaComposicao> producaoFormaComposicaoList) {
		this.producaoFormaComposicaoList = producaoFormaComposicaoList;
	}

	public void setQuantidadeProdutores(Integer quantidadeProdutores) {
		this.quantidadeProdutores = quantidadeProdutores;
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