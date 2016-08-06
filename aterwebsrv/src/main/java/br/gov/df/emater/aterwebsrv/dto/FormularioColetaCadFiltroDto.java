package br.gov.df.emater.aterwebsrv.dto;

import java.util.Calendar;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

public class FormularioColetaCadFiltroDto extends FormularioCadFiltroDto {

	private static final long serialVersionUID = 1L;

	private Set<Confirmacao> completada;

	private Integer formularioId;

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inicio;

	private Pessoa pessoa;

	private PropriedadeRural propriedadeRural;

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar termino;

	public FormularioColetaCadFiltroDto() {

	}

	public Set<Confirmacao> getCompletada() {
		return completada;
	}

	public Integer getFormularioId() {
		return formularioId;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public PropriedadeRural getPropriedadeRural() {
		return propriedadeRural;
	}

	public Calendar getTermino() {
		return termino;
	}

	public void setCompletada(Set<Confirmacao> completada) {
		this.completada = completada;
	}

	public void setFormularioId(Integer formularioId) {
		this.formularioId = formularioId;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public void setPropriedadeRural(PropriedadeRural propriedadeRural) {
		this.propriedadeRural = propriedadeRural;
	}

	public void setTermino(Calendar termino) {
		this.termino = termino;
	}

}