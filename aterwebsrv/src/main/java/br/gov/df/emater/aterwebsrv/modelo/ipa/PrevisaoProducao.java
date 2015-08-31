package br.gov.df.emater.aterwebsrv.modelo.ipa;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaGrupo;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonFormatarBigDecimal;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "previsao_producao", schema = EntidadeBase.IPA_SCHEMA)
@Inheritance(strategy = InheritanceType.JOINED)
// para identificar classes dentro de contextos polim�rficos
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class PrevisaoProducao extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1l;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "inicio")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inicio;

	@JoinColumn(name = "pessoa_grupo_id")
	@ManyToOne
	private PessoaGrupo pessoaGrupo;

	@OneToMany(mappedBy = "previsaoProducao", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Producao> producaoList;

	@ManyToOne
	@JoinColumn(name = "produto_servico_id")
	private ProdutoServico produtoServico;

	@Column(name = "termino")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar termino;

	@Column(name = "total_propriedades")
	private Integer totalPropriedades;

	@Column(name = "volume")
	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal volume;

	public PrevisaoProducao() {
	}

	public PrevisaoProducao(Integer id) {
		setId(id);
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public PessoaGrupo getPessoaGrupo() {
		return pessoaGrupo;
	}

	public List<Producao> getProducaoList() {
		return producaoList;
	}

	public ProdutoServico getProdutoServico() {
		return produtoServico;
	}

	public Calendar getTermino() {
		return termino;
	}

	public Integer getTotalPropriedades() {
		return totalPropriedades;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public void setPessoaGrupo(PessoaGrupo pessoaGrupo) {
		this.pessoaGrupo = pessoaGrupo;
	}

	public void setProducaoList(List<Producao> producaoList) {
		this.producaoList = producaoList;
	}

	public void setProdutoServico(ProdutoServico produtoServico) {
		this.produtoServico = produtoServico;
	}

	public void setTermino(Calendar termino) {
		this.termino = termino;
	}

	public void setTotalPropriedades(Integer totalPropriedades) {
		this.totalPropriedades = totalPropriedades;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

}