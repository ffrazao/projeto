package br.gov.df.emater.aterwebsrv.dto;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FormularioDestino;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Situacao;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

public class FormularioCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private String codigo;

	private Set<FormularioDestino> destino;

	private String nome;

	private Set<Situacao> situacao;

	private Set<Confirmacao> subformulario;

	private Integer versao;

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar vigencia;

	private Set<Confirmacao> vigente;

	public FormularioCadFiltroDto() {

	}

	public FormularioCadFiltroDto(Confirmacao vigente) {
		this.setVigente(new HashSet<Confirmacao>(Arrays.asList(vigente)));
	}

	public String getCodigo() {
		return codigo;
	}

	public Set<FormularioDestino> getDestino() {
		return destino;
	}

	public String getNome() {
		return nome;
	}

	public Set<Situacao> getSituacao() {
		return situacao;
	}

	public Set<Confirmacao> getSubformulario() {
		return subformulario;
	}

	public Integer getVersao() {
		return versao;
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

	public void setDestino(Set<FormularioDestino> destino) {
		this.destino = destino;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setSituacao(Set<Situacao> situacao) {
		this.situacao = situacao;
	}

	public void setSubformulario(Set<Confirmacao> subformulario) {
		this.subformulario = subformulario;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public void setVigencia(Calendar vigencia) {
		this.vigencia = vigencia;
	}

	public void setVigente(Set<Confirmacao> vigente) {
		this.vigente = vigente;
	}

}