package br.gov.df.emater.aterwebsrv.modelo.ater;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

import java.io.Serializable;
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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PropriedadeRuralVinculoTipo;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(name = "publico_alvo_propriedade_rural", schema = EntidadeBase.ATER_SCHEMA)
public class PublicoAlvoPropriedadeRural extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<PublicoAlvoPropriedadeRural> {

	private static final long serialVersionUID = 1L;

	@Column(name = "area")
//	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal area;

	@Column(name = "chave_sisater")
	private String chaveSisater;

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

	@Enumerated(EnumType.STRING)
	protected Confirmacao principal;

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

	public PublicoAlvoPropriedadeRural() {
		super();
	}

	public PublicoAlvoPropriedadeRural(Integer id, BigDecimal area, Comunidade comunidade, Calendar inicio, PropriedadeRural propriedadeRural, PublicoAlvo publicoAlvo, Calendar termino, PropriedadeRuralVinculoTipo vinculo) {
		super();
		this.area = area;
		this.comunidade = comunidade;
		this.id = id;
		this.inicio = inicio;
		this.propriedadeRural = propriedadeRural;
		this.publicoAlvo = publicoAlvo;
		this.termino = termino;
		this.vinculo = vinculo;
	}

	public PublicoAlvoPropriedadeRural(Serializable id) {
		super(id);
	}

	public BigDecimal getArea() {
		return area;
	}

	public String getChaveSisater() {
		return chaveSisater;
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

	public Confirmacao getPrincipal() {
		return principal;
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

	@Override
	public PublicoAlvoPropriedadeRural infoBasica() {
		return new PublicoAlvoPropriedadeRural(this.getId(), this.getArea(), infoBasicaReg(this.getComunidade()), this.getInicio(), infoBasicaReg(this.getPropriedadeRural()), infoBasicaReg(this.getPublicoAlvo()), this.getTermino(), this.getVinculo());
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}

	public void setChaveSisater(String chaveSisater) {
		this.chaveSisater = chaveSisater;
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

	public void setPrincipal(Confirmacao principal) {
		this.principal = principal;
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
