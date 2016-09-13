package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

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
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.UnidadeMedida;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonFormatarBigDecimal;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(name = "custo_producao", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class CustoProducaoInsumoServicoPreco extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<CustoProducaoInsumoServicoPreco> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	@Column(name="inicio_vigencia")
	private Calendar inicioVigencia;

	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal preco;

	@OneToMany(mappedBy = "pessoa")
	private List<CustoProducaoInsumoServicoPreco> precoList;
	
	@ManyToOne
	@JoinColumn(name = "unidade_medida_id")
	private UnidadeMedida unidadeMedida;



	public CustoProducaoInsumoServico() {
		super();
	}

	public CustoProducaoInsumoServico(Integer id) {
		super(id);
	}


	@Override
	public Integer getId() {
		return id;
	}


	@Override
	public CustoProducaoInsumoServico infoBasica() {
		return new CustoProducaoInsumoServico(this.id, this.custoProducaoItens == null ? null : this.custoProducaoItens.infoBasica(), this.custoProducaoInsumoServico == null ? null : this.custoProducaoInsumoServico.infoBasica(), this.quantidade);
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}

}