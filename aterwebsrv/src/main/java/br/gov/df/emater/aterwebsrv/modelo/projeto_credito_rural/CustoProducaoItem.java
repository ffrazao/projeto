package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

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
import br.gov.df.emater.aterwebsrv.rest.json.JsonFormatarBigDecimal;

@Entity
@Table(name = "custo_producao_item", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class CustoProducaoItem extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<CustoProducaoItem> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "custo_producao_id")
	private CustoProducao custoProducao;

	@ManyToOne
	@JoinColumn(name = "custo_producao_insumo_servico_id")
	private CustoProducaoInsumoServico custoProducaoInsumoServico;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal quantidade;

	public CustoProducaoItem() {
		super();
	}

	public CustoProducaoItem(Integer id) {
		super(id);
	}

	public CustoProducaoItem(Integer id, CustoProducao custoProducao, CustoProducaoInsumoServico custoProducaoInsumoServico, BigDecimal quantidade) {
		super();
		this.id = id;
		this.custoProducao = custoProducao;
		this.custoProducaoInsumoServico = custoProducaoInsumoServico;
		this.quantidade = quantidade;
	}

	public CustoProducao getCustoProducao() {
		return custoProducao;
	}

	public CustoProducaoInsumoServico getCustoProducaoInsumoServico() {
		return custoProducaoInsumoServico;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	@Override
	public CustoProducaoItem infoBasica() {
		return new CustoProducaoItem(this.id, this.custoProducao == null ? null : new CustoProducao(this.custoProducao.getId()), infoBasicaReg(this.custoProducaoInsumoServico), this.quantidade);
	}

	public void setCustoProducao(CustoProducao custoProducao) {
		this.custoProducao = custoProducao;
	}

	public void setCustoProducaoInsumoServico(CustoProducaoInsumoServico custoProducaoInsumoServico) {
		this.custoProducaoInsumoServico = custoProducaoInsumoServico;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

}