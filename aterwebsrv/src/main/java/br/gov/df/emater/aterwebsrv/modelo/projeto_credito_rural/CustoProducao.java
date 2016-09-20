package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

	@OneToMany(mappedBy = "custoProducao", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<CustoProducaoFormaProducao> formaProducaoList;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToMany(mappedBy = "custoProducao")
	private List<CustoProducaoItem> itens;

	@Column(name = "nome_forma_producao")
	private String nomeFormaProducao;

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

	public CustoProducao(Integer id, String nomeFormaProducao, Bem bem, BigDecimal area, BigDecimal produtividade, UnidadeMedida unidadeMedida, List<CustoProducaoItem> itens) {
		super();
		this.id = id;
		this.nomeFormaProducao = nomeFormaProducao;
		this.bem = bem;
		this.area = area;
		this.produtividade = produtividade;
		this.unidadeMedida = unidadeMedida;
		this.itens = itens;
	}

	public BigDecimal getArea() {
		return area;
	}

	public Bem getBem() {
		return bem;
	}

	public List<CustoProducaoFormaProducao> getFormaProducaoList() {
		return formaProducaoList;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public List<CustoProducaoItem> getItens() {
		return itens;
	}

	public String getNomeFormaProducao() {
		return nomeFormaProducao;
	}

	public BigDecimal getProdutividade() {
		return produtividade;
	}

	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public CustoProducao infoBasica() {
		if (this.itens != null) {
			List<CustoProducaoItem> itensTemp = new ArrayList<>();
			for (CustoProducaoItem item: this.itens) {
				itensTemp.add(item.infoBasica());
			}
			this.itens = itensTemp;
		}
		return new CustoProducao(this.id, this.nomeFormaProducao, this.bem == null ? null : this.bem.infoBasica(), this.area, this.produtividade, this.unidadeMedida == null ? null : this.unidadeMedida.infoBasica(), this.itens);
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}

	public void setBem(Bem bem) {
		this.bem = bem;
	}

	public void setFormaProducaoList(List<CustoProducaoFormaProducao> formaProducaoList) {
		this.formaProducaoList = formaProducaoList;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setItens(List<CustoProducaoItem> itens) {
		this.itens = itens;
	}

	public void setNomeFormaProducao(String nomeFormaProducao) {
		this.nomeFormaProducao = nomeFormaProducao;
	}

	public void setProdutividade(BigDecimal produtividade) {
		this.produtividade = produtividade;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

}