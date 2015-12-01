package br.gov.df.emater.aterwebsrv.modelo.formulario;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FormularioDestino;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Situacao;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(schema = EntidadeBase.FORMULARIO_SCHEMA)
public class Formulario extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	private String codigo;

	@Enumerated(EnumType.STRING)
	private FormularioDestino destino;

	@OneToMany(mappedBy = "formulario")
	private List<FormularioVersao> formularioVersaoList;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inicio;

	private String nome;

	@Enumerated(EnumType.STRING)
	private Situacao situacao;

	@Enumerated(EnumType.STRING)
	private Confirmacao subformulario;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar termino;

	public Formulario() {
		super();
	}

	public Formulario(Integer id, String nome, String codigo, Situacao situacao, Calendar inicio, Calendar termino) {
		this.setId(id);
		this.setNome(nome);
		this.setCodigo(codigo);
		this.setSituacao(situacao);
		this.setInicio(inicio);
		this.setTermino(termino);
	}

	public String getCodigo() {
		return codigo;
	}

	public FormularioDestino getDestino() {
		return destino;
	}

	public List<FormularioVersao> getFormularioVersaoList() {
		return formularioVersaoList;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public String getNome() {
		return nome;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public Confirmacao getSubformulario() {
		return subformulario;
	}

	public Calendar getTermino() {
		return termino;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setDestino(FormularioDestino destino) {
		this.destino = destino;
	}

	public void setFormularioVersaoList(List<FormularioVersao> formularioVersaoList) {
		this.formularioVersaoList = formularioVersaoList;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public void setSubformulario(Confirmacao subformulario) {
		this.subformulario = subformulario;
	}

	public void setTermino(Calendar termino) {
		this.termino = termino;
	}

}
