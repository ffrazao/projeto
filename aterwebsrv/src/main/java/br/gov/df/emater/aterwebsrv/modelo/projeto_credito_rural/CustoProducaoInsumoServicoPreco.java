package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonFormatarBigDecimal;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(name = "custo_producao_insumo_servico_preco", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class CustoProducaoInsumoServicoPreco extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<CustoProducaoInsumoServicoPreco> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "custo_producao_insumo_servico_id")
	private CustoProducaoInsumoServico custoProducaoInsumoServico;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	@Column(name = "inicio_vigencia")
	private Calendar inicioVigencia;

	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal preco;

	public CustoProducaoInsumoServicoPreco() {
		super();
	}

	public CustoProducaoInsumoServicoPreco(Integer id) {
		super(id);
	}

	public CustoProducaoInsumoServicoPreco(Integer id, CustoProducaoInsumoServico custoProducaoInsumoServico, Calendar inicioVigencia, BigDecimal preco) {
		super();
		this.id = id;
		this.custoProducaoInsumoServico = custoProducaoInsumoServico;
		this.inicioVigencia = inicioVigencia;
		this.preco = preco;
	}

	public CustoProducaoInsumoServico getCustoProducaoInsumoServico() {
		return custoProducaoInsumoServico;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Calendar getInicioVigencia() {
		return inicioVigencia;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	@Override
	public CustoProducaoInsumoServicoPreco infoBasica() {
		return new CustoProducaoInsumoServicoPreco(this.id, this.custoProducaoInsumoServico == null ? null : new CustoProducaoInsumoServico(this.custoProducaoInsumoServico.getId()), this.inicioVigencia, this.preco);
	}

	public void setCustoProducaoInsumoServico(CustoProducaoInsumoServico custoProducaoInsumoServico) {
		this.custoProducaoInsumoServico = custoProducaoInsumoServico;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setInicioVigencia(Calendar inicioVigencia) {
		this.inicioVigencia = inicioVigencia;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

}