package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import java.math.BigDecimal;
import java.util.Calendar;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(name = "producao", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class Producao extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "alteracao_data")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar alteracaoData;

	@ManyToOne
	@JoinColumn(name = "alteracao_usuario_id")
	private Usuario alteracaoUsuario;

	private Integer ano;

	@ManyToOne
	@JoinColumn(name = "bem_forma_producao_id")
	private BemFormaProducao bemFormaProducao;

	@ManyToOne
	@JoinColumn(name = "comunidade_id")
	private Comunidade comunidade;

	@Enumerated(EnumType.STRING)
	private Confirmacao confirmado;

	@Column(name = "data_confirmacao")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar dataConfirmacao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "inclusao_data")
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
	@JoinColumn(name = "propriedade_rural_id")
	private PropriedadeRural propriedadeRural;

	@ManyToOne
	@JoinColumn(name = "publico_alvo_id")
	private PublicoAlvo publicoAlvo;

	@Column(name = "valor_total")
	private BigDecimal valorTotal;

	@Column(name = "valor_unitario")
	private BigDecimal valorUnitario;

	private BigDecimal volume;

	public Producao() {
		super();
	}

	public Calendar getAlteracaoData() {
		return alteracaoData;
	}

	public Usuario getAlteracaoUsuario() {
		return alteracaoUsuario;
	}

	public Integer getAno() {
		return ano;
	}

	public BemFormaProducao getBemFormaProducao() {
		return bemFormaProducao;
	}

	public Comunidade getComunidade() {
		return comunidade;
	}

	public Confirmacao getConfirmado() {
		return confirmado;
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

	public void setAlteracaoData(Calendar alteracaoData) {
		this.alteracaoData = alteracaoData;
	}

	public void setAlteracaoUsuario(Usuario alteracaoUsuario) {
		this.alteracaoUsuario = alteracaoUsuario;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public void setBemFormaProducao(BemFormaProducao bemFormaProducao) {
		this.bemFormaProducao = bemFormaProducao;
	}

	public void setComunidade(Comunidade comunidade) {
		this.comunidade = comunidade;
	}

	public void setConfirmado(Confirmacao confirmado) {
		this.confirmado = confirmado;
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