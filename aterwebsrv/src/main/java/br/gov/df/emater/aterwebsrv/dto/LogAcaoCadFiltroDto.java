package br.gov.df.emater.aterwebsrv.modelo.dto;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

public class LogAcaoCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)

	private Calendar inicio;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar termino;

	private Set<TagDto> usuario;

	public Calendar getInicio() {
		return inicio;
	}

	public Calendar getTermino() {
		return termino;
	}

	public Set<TagDto> getUsuario() {
		return usuario;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public void setTermino(Calendar termino) {
		this.termino = termino;
	}

	public void setUsuario(Set<TagDto> usuario) {
		this.usuario = usuario;
	}

}