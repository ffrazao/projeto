package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import br.gov.df.emater.aterwebsrv.modelo._LogInclusaoAlteracao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(name = "producao", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class Producao extends EntidadeBase implements _ChavePrimaria<Integer>, _LogInclusaoAlteracao {

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
	@JoinColumn(name = "inclusao_usuario_id", updatable = false)
	private Usuario inclusaoUsuario;

	@Column(name = "item_a_valor")
	private BigDecimal itemAValor;

	@Column(name = "item_b_valor")
	private BigDecimal itemBValor;

	@Column(name = "item_c_valor")
	private BigDecimal itemCValor;

	@ManyToOne
	@JoinColumn(name = "producao_proprietario_id")
	private ProducaoProprietario producaoProprietario;

	@OneToMany(mappedBy = "producao")
	private List<ProducaoComposicao> producaoComposicaoList;

	@Column(name = "quantidade_produtores")
	private Integer quantidadeProdutores;

	@Column(name = "valor_total")
	private BigDecimal valorTotal;

	@Column(name = "valor_unitario")
	private BigDecimal valorUnitario;

	private BigDecimal volume;

	public Producao() {
		super();
	}

	public Producao(Producao producao) {
		if (producao.getProducaoComposicaoList() != null) {
			producao.getProducaoComposicaoList().forEach((pfc) -> {
				if (this.producaoComposicaoList == null) {
					this.producaoComposicaoList = new ArrayList<>();
				}
				this.producaoComposicaoList.add(new ProducaoComposicao(pfc));
			});
		}
	}

	@Override
	public Calendar getAlteracaoData() {
		return alteracaoData;
	}

	@Override
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

	@Override
	public Calendar getInclusaoData() {
		return inclusaoData;
	}

	@Override
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

	public ProducaoProprietario getProducaoProprietario() {
		return producaoProprietario;
	}

	public List<ProducaoComposicao> getProducaoComposicaoList() {
		return producaoComposicaoList;
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

	@Override
	public void setAlteracaoData(Calendar alteracaoData) {
		this.alteracaoData = alteracaoData;
	}

	@Override
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

	@Override
	public void setInclusaoData(Calendar inclusaoData) {
		this.inclusaoData = inclusaoData;
	}

	@Override
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

	public void setProducaoProprietario(ProducaoProprietario producaoProprietario) {
		this.producaoProprietario = producaoProprietario;
	}

	public void setProducaoComposicaoList(List<ProducaoComposicao> producaoComposicaoList) {
		this.producaoComposicaoList = producaoComposicaoList;
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