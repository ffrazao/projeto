package br.gov.df.emater.aterwebsrv.modelo.dto;

import java.util.Calendar;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Situacao;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

public class FormularioCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private String codigo;

	private String nome;

	private Set<Situacao> situacao;

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar vigencia;

	private Set<Confirmacao> vigente;

	public String getCodigo() {
		return codigo;
	}

	public String getNome() {
		return nome;
	}

	public Set<Situacao> getSituacao() {
		return situacao;
	}

	public Calendar getVigencia() {
		return vigencia;
	}

	public Set<Confirmacao> getVigente() {
		return vigente;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setSituacao(Set<Situacao> situacao) {
		this.situacao = situacao;
	}

	public void setVigencia(Calendar vigencia) {
		this.vigencia = vigencia;
	}

	public void setVigente(Set<Confirmacao> vigente) {
		this.vigente = vigente;
	}

}