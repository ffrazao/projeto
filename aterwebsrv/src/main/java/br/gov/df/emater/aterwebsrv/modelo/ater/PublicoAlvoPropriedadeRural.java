package br.gov.df.emater.aterwebsrv.modelo.ater;

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
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PropriedadeRuralVinculoTipo;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonFormatarBigDecimal;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(name = "publico_alvo_propriedade_rural", schema = EntidadeBase.ATER_SCHEMA)
public class PublicoAlvoPropriedadeRural extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "area")
	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal area;

	@ManyToOne
	@JoinColumn(name = "comunidade_id")
	private Comunidade comunidade;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "inicio")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inicio;

	@ManyToOne
	@JoinColumn(name = "propriedade_rural_id")
	private PropriedadeRural propriedadeRural;

	@ManyToOne
	@JoinColumn(name = "publico_alvo_id")
	private PublicoAlvo publicoAlvo;

	@Column(name = "termino")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar termino;

	@Column(name = "tipo_vinculo")
	@Enumerated(EnumType.STRING)
	private PropriedadeRuralVinculoTipo vinculo;

	public BigDecimal getArea() {
		return area;
	}

	public Comunidade getComunidade() {
		return comunidade;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public PropriedadeRural getPropriedadeRural() {
		return propriedadeRural;
	}

	public PublicoAlvo getPublicoAlvo() {
		return publicoAlvo;
	}

	public Calendar getTermino() {
		return termino;
	}

	public PropriedadeRuralVinculoTipo getVinculo() {
		return vinculo;
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}

	public void setComunidade(Comunidade comunidade) {
		this.comunidade = comunidade;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public void setPropriedadeRural(PropriedadeRural propriedadeRural) {
		this.propriedadeRural = propriedadeRural;
	}

	public void setPublicoAlvo(PublicoAlvo publicoAlvo) {
		this.publicoAlvo = publicoAlvo;
	}

	public void setTermino(Calendar termino) {
		this.termino = termino;
	}

	public void setVinculo(PropriedadeRuralVinculoTipo vinculo) {
		this.vinculo = vinculo;
	}

}
