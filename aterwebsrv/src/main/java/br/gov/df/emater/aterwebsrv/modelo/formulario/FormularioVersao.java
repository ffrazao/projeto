package br.gov.df.emater.aterwebsrv.modelo.formulario;

import java.util.Calendar;
import java.util.List;

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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(schema = EntidadeBase.FORMULARIO_SCHEMA)
public class FormularioVersao extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar data;

	@ManyToOne
	@JoinColumn(name = "formulario_id")
	private Formulario formulario;

	@OneToMany(mappedBy = "formularioVersao")
	private List<FormularioVersaoElemento> formularioVersaoElementoList;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private Integer versao;

	public Calendar getData() {
		return data;
	}

	public Formulario getFormulario() {
		return formulario;
	}

	public List<FormularioVersaoElemento> getFormularioVersaoElementoList() {
		return formularioVersaoElementoList;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}

	public void setFormularioVersaoElementoList(List<FormularioVersaoElemento> formularioVersaoElementoList) {
		this.formularioVersaoElementoList = formularioVersaoElementoList;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
