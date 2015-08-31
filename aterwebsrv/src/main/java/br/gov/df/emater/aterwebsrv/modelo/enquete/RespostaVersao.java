package br.gov.df.emater.aterwebsrv.modelo.enquete;

import java.util.Calendar;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerTimestamp;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerTimestamp;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "resposta_versao", schema = EntidadeBase.ENQUETE_SCHEMA)
public class RespostaVersao extends EntidadeBase implements
		_ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinColumn(name = "formulario_direcionamento_id")
	private FormularioDirecionamento formularioDirecionamento;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "inicio")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerTimestamp.class)
	@JsonDeserialize(using = JsonDeserializerTimestamp.class)
	private Calendar inicio;

	@OneToMany(mappedBy = "respostaVersao", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Resposta> respostaList;

	@Column(name = "termino")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerTimestamp.class)
	@JsonDeserialize(using = JsonDeserializerTimestamp.class)
	private Calendar termino;

	@Column(name = "uuid")
	private String uuid;

	public FormularioDirecionamento getFormularioDirecionamento() {
		return formularioDirecionamento;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public List<Resposta> getRespostaList() {
		return respostaList;
	}

	public Calendar getTermino() {
		return termino;
	}

	public String getUuid() {
		return uuid;
	}

	public void setFormularioDirecionamento(
			FormularioDirecionamento formularioDirecionamento) {
		this.formularioDirecionamento = formularioDirecionamento;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public void setRespostaList(List<Resposta> respostaList) {
		this.respostaList = respostaList;
	}

	public void setTermino(Calendar termino) {
		this.termino = termino;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}