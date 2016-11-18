package br.gov.df.emater.aterwebsrv.dto.agenda;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Assunto;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Metodo;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

public class AgendaCadFiltroDto extends CadFiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private Assunto assunto;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inicio;

	private Metodo metodo;

	private Set<Integer> pessoaIdList;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar termino;

	public Assunto getAssunto() {
		return assunto;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public Metodo getMetodo() {
		return metodo;
	}

	public Set<Integer> getPessoaIdList() {
		return pessoaIdList;
	}

	public Calendar getTermino() {
		return termino;
	}

	public void setAssunto(Assunto assunto) {
		this.assunto = assunto;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public void setMetodo(Metodo metodo) {
		this.metodo = metodo;
	}

	public void setPessoaIdList(Set<Integer> pessoaIdList) {
		this.pessoaIdList = pessoaIdList;
	}

	public void setTermino(Calendar termino) {
		this.termino = termino;
	}

}