package br.gov.df.emater.aterwebsrv.modelo.funcional;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

import java.io.Serializable;
import java.util.Calendar;

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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

/*
 * ATENÇÃO! Qualquer alteração na estrutura desta classe também deve ser feita na interface UnidadeOrganizacionalHierarquiaBase 
 */
@Entity
@Table(name = "unidade_organizacional_hierarquia", schema = EntidadeBase.FUNCIONAL_SCHEMA)
public class UnidadeOrganizacionalHierarquia extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<UnidadeOrganizacionalHierarquia>, UnidadeOrganizacionalHierarquiaBase {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "unidade_organizacional_ascendente_id")
	private UnidadeOrganizacional ascendente;

	@ManyToOne
	@JoinColumn(name = "unidade_organizacional_descendente_id")
	private UnidadeOrganizacional descendente;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inicio;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar termino;

	public UnidadeOrganizacionalHierarquia() {
	}

	public UnidadeOrganizacionalHierarquia(Serializable id) {
		super(id);
	}

	public UnidadeOrganizacionalHierarquia(Serializable id, UnidadeOrganizacional ascendente, UnidadeOrganizacional descendente, Calendar inicio, Calendar termino) {
		this(id);
	}

	@Override
	public UnidadeOrganizacional getAscendente() {
		return ascendente;
	}

	@Override
	public UnidadeOrganizacional getDescendente() {
		return descendente;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public Calendar getInicio() {
		return inicio;
	}

	@Override
	public Calendar getTermino() {
		return termino;
	}

	@Override
	public UnidadeOrganizacionalHierarquia infoBasica() {
		return new UnidadeOrganizacionalHierarquia(this.id, (UnidadeOrganizacional) infoBasicaReg(this.ascendente), (UnidadeOrganizacional) infoBasicaReg(this.descendente), this.inicio, this.termino);
	}

	@Override
	public void setAscendente(UnidadeOrganizacional ascendente) {
		this.ascendente = ascendente;
	}

	@Override
	public void setDescendente(UnidadeOrganizacional descendente) {
		this.descendente = descendente;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	@Override
	public void setTermino(Calendar termino) {
		this.termino = termino;
	}

}
