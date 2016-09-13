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
public class CustoProducao extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<CustoProducao> {

	private static final long serialVersionUID = 1L;

	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal area;

	@ManyToOne
	@JoinColumn(name = "bem_id")
	private Bem bem;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal produtividade;

	@ManyToOne
	@JoinColumn(name = "unidade_medida_id")
	private UnidadeMedida unidadeMedida;

	public CustoProducao() {
		super();
	}

	public CustoProducao(Integer id) {
		super(id);
	}

	public CustoProducao(Integer id, Bem bem, BigDecimal area, BigDecimal produtividade, UnidadeMedida unidadeMedida) {
		super();
		this.id = id;
		this.bem = bem;
		this.area = area;
		this.produtividade = produtividade;
		this.unidadeMedida = unidadeMedida;
	}

	public BigDecimal getArea() {
		return area;
	}

	public Bem getBem() {
		return bem;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public BigDecimal getProdutividade() {
		return produtividade;
	}

	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public CustoProducao infoBasica() {
		return new CustoProducao(this.id, this.bem == null ? null : this.bem.infoBasica(), this.area, this.produtividade, this.unidadeMedida == null ? null : this.unidadeMedida.infoBasica());
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}

	public void setBem(Bem bem) {
		this.bem = bem;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setProdutividade(BigDecimal produtividade) {
		this.produtividade = produtividade;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

}