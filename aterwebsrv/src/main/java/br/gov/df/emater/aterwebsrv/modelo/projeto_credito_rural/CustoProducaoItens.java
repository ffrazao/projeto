package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Bem;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.UnidadeMedida;
import br.gov.df.emater.aterwebsrv.rest.json.JsonFormatarBigDecimal;

@Entity
@Table(name = "custo_producao", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class CustoProducaoItens extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<CustoProducaoItens> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "custo_producao_insumo_servico_id")
	private CustoProducaoInsumoServico custoProducaoInsumoServico;

	@ManyToOne
	@JoinColumn(name = "custo_producao_itens_id")
	private CustoProducaoItens custoProducaoItens;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal quantidade;


	public CustoProducaoItens() {
		super();
	}

	public CustoProducaoItens(Integer id) {
		super(id);
	}

	public CustoProducaoItens(Integer id, CustoProducaoItens custoProducaoItens, CustoProducaoInsumoServico custoProducaoInsumoServico, BigDecimal quantidade) {
		super();
		this.id = id;
		this.custoProducaoItens = custoProducaoItens;
		this.custoProducaoInsumoServico = custoProducaoInsumoServico;
		this.quantidade = quantidade;
	}

	public CustoProducaoInsumoServico getCustoProducaoInsumoServico() {
		return custoProducaoInsumoServico;
	}

	public CustoProducaoItens getCustoProducaoItens() {
		return custoProducaoItens;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	@Override
	public CustoProducaoItens infoBasica() {
		return new CustoProducaoItens(this.id, this.custoProducaoItens == null ? null : this.custoProducaoItens.infoBasica(), this.custoProducaoInsumoServico == null ? null : this.custoProducaoInsumoServico.infoBasica(), this.quantidade);
	}

	public void setCustoProducaoInsumoServico(CustoProducaoInsumoServico custoProducaoInsumoServico) {
		this.custoProducaoInsumoServico = custoProducaoInsumoServico;
	}

	public void setCustoProducaoItens(CustoProducaoItens custoProducaoItens) {
		this.custoProducaoItens = custoProducaoItens;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}


}