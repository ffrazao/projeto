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

import org.hibernate.annotations.Immutable;
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
@Table(name = "unidade_organizacional_hierarquia_ativa_vi", schema = EntidadeBase.FUNCIONAL_SCHEMA)
@Immutable
public class UnidadeOrganizacionalHierarquiaAtivaVi extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<UnidadeOrganizacionalHierarquiaAtivaVi> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "unidade_organizacional_ascendente_id")
	private UnidadeOrganizacionalAtivaVi ascendente;

	@ManyToOne
	@JoinColumn(name = "unidade_organizacional_descendente_id")
	private UnidadeOrganizacionalAtivaVi descendente;

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

	public UnidadeOrganizacionalHierarquiaAtivaVi() {
	}

	public UnidadeOrganizacionalHierarquiaAtivaVi(Serializable id) {
		super(id);
	}

	public UnidadeOrganizacionalHierarquiaAtivaVi(Serializable id, UnidadeOrganizacionalAtivaVi ascendente, UnidadeOrganizacionalAtivaVi descendente, Calendar inicio, Calendar termino) {
		this(id);
	}

	public UnidadeOrganizacionalAtivaVi getAscendente() {
		return ascendente;
	}

	public UnidadeOrganizacionalAtivaVi getDescendente() {
		return descendente;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public Calendar getTermino() {
		return termino;
	}

	public UnidadeOrganizacionalHierarquiaAtivaVi infoBasica() {
		return new UnidadeOrganizacionalHierarquiaAtivaVi(this.id, infoBasicaReg(this.ascendente), infoBasicaReg(this.descendente), this.inicio, this.termino);
	}

	public void setAscendente(UnidadeOrganizacionalAtivaVi ascendente) {
		this.ascendente = ascendente;
	}

	public void setDescendente(UnidadeOrganizacionalAtivaVi descendente) {
		this.descendente = descendente;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public void setTermino(Calendar termino) {
		this.termino = termino;
	}

}