package br.gov.df.emater.aterwebsrv.modelo.dto;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.atividade.Assunto;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Metodo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGeracao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoCategoria;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

public class AtividadeCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private String codigo;

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

	private Metodo metodo;

	private Assunto assunto;

	private String demandante;

	private String executor;

	private Set<PublicoAlvoCategoria> categoria;

	private Set<PessoaGenero> genero;

	private Set<PessoaGeracao> geracao;

	private Set<Confirmacao> organizacao;

	private List<Integer> unidadeOrganizacional;

	private List<Integer> comunidade;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public Calendar getTermino() {
		return termino;
	}

	public void setTermino(Calendar termino) {
		this.termino = termino;
	}

	public Metodo getMetodo() {
		return metodo;
	}

	public void setMetodo(Metodo metodo) {
		this.metodo = metodo;
	}

	public Assunto getAssunto() {
		return assunto;
	}

	public void setAssunto(Assunto assunto) {
		this.assunto = assunto;
	}

	public String getDemandante() {
		return demandante;
	}

	public void setDemandante(String demandante) {
		this.demandante = demandante;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public Set<PublicoAlvoCategoria> getCategoria() {
		return categoria;
	}

	public void setCategoria(Set<PublicoAlvoCategoria> categoria) {
		this.categoria = categoria;
	}

	public Set<PessoaGenero> getGenero() {
		return genero;
	}

	public void setGenero(Set<PessoaGenero> genero) {
		this.genero = genero;
	}

	public Set<PessoaGeracao> getGeracao() {
		return geracao;
	}

	public void setGeracao(Set<PessoaGeracao> geracao) {
		this.geracao = geracao;
	}

	public Set<Confirmacao> getOrganizacao() {
		return organizacao;
	}

	public void setOrganizacao(Set<Confirmacao> organizacao) {
		this.organizacao = organizacao;
	}

	public List<Integer> getUnidadeOrganizacional() {
		return unidadeOrganizacional;
	}

	public void setUnidadeOrganizacional(List<Integer> unidadeOrganizacional) {
		this.unidadeOrganizacional = unidadeOrganizacional;
	}

	public List<Integer> getComunidade() {
		return comunidade;
	}

	public void setComunidade(List<Integer> comunidade) {
		this.comunidade = comunidade;
	}

}