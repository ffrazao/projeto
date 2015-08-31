package br.gov.df.emater.aterwebsrv.modelo.ipa;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.rest.json.JsonFormatarBigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
@Table(name = "producao", schema = EntidadeBase.IPA_SCHEMA)
public class Producao extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1l;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@JoinColumn(name = "previsao_producao_id")
	@ManyToOne
	private PrevisaoProducao previsaoProducao;

	@JoinColumn(name = "propriedade_rural_id")
	@ManyToOne
	private PropriedadeRural propriedadeRural;

	@OneToMany(mappedBy = "producao", cascade = CascadeType.ALL)
	private List<Responsavel> responsavelList;

	@Column(name = "volume")
	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal volume;

	public Producao() {
	}

	public Producao(PrevisaoProducao previsaoProducao) {
		setPrevisaoProducao(previsaoProducao);
	}

	@Override
	public Integer getId() {
		return id;
	}

	public PrevisaoProducao getPrevisaoProducao() {
		return previsaoProducao;
	}

	public PropriedadeRural getPropriedadeRural() {
		return propriedadeRural;
	}

	public List<Responsavel> getResponsavelList() {
		return responsavelList;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPrevisaoProducao(PrevisaoProducao previsaoProducao) {
		this.previsaoProducao = previsaoProducao;
	}

	public void setPropriedadeRural(PropriedadeRural propriedadeRural) {
		this.propriedadeRural = propriedadeRural;
	}

	public void setResponsavelList(List<Responsavel> responsavelList) {
		this.responsavelList = responsavelList;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

}