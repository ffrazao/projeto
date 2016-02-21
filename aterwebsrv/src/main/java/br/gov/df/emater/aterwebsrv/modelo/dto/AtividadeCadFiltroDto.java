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
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoSegmento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

public class AtividadeCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private Assunto assunto;

	private Set<PublicoAlvoCategoria> categoria;

	private String codigo;

	private List<Integer> comunidade;

	private String demandante;

	private List<PessoaJuridica> empresaList;

	private String executor;

	private Set<PessoaGenero> genero;

	private Set<PessoaGeracao> geracao;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inicio;

	private Metodo metodo;

	private Set<Confirmacao> organizacao;

	private Set<PublicoAlvoSegmento> segmento;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar termino;

	private List<Integer> unidadeOrganizacional;

	public Assunto getAssunto() {
		return assunto;
	}

	public Set<PublicoAlvoCategoria> getCategoria() {
		return categoria;
	}

	public String getCodigo() {
		return codigo;
	}

	public List<Integer> getComunidade() {
		return comunidade;
	}

	public String getDemandante() {
		return demandante;
	}

	public List<PessoaJuridica> getEmpresaList() {
		return empresaList;
	}

	public String getExecutor() {
		return executor;
	}

	public Set<PessoaGenero> getGenero() {
		return genero;
	}

	public Set<PessoaGeracao> getGeracao() {
		return geracao;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public Metodo getMetodo() {
		return metodo;
	}

	public Set<Confirmacao> getOrganizacao() {
		return organizacao;
	}

	public Set<PublicoAlvoSegmento> getSegmento() {
		return segmento;
	}

	public Calendar getTermino() {
		return termino;
	}

	public List<Integer> getUnidadeOrganizacional() {
		return unidadeOrganizacional;
	}

	public void setAssunto(Assunto assunto) {
		this.assunto = assunto;
	}

	public void setCategoria(Set<PublicoAlvoCategoria> categoria) {
		this.categoria = categoria;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setComunidade(List<Integer> comunidade) {
		this.comunidade = comunidade;
	}

	public void setDemandante(String demandante) {
		this.demandante = demandante;
	}

	public void setEmpresaList(List<PessoaJuridica> empresaList) {
		this.empresaList = empresaList;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public void setGenero(Set<PessoaGenero> genero) {
		this.genero = genero;
	}

	public void setGeracao(Set<PessoaGeracao> geracao) {
		this.geracao = geracao;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public void setMetodo(Metodo metodo) {
		this.metodo = metodo;
	}

	public void setOrganizacao(Set<Confirmacao> organizacao) {
		this.organizacao = organizacao;
	}

	public void setSegmento(Set<PublicoAlvoSegmento> segmento) {
		this.segmento = segmento;
	}

	public void setTermino(Calendar termino) {
		this.termino = termino;
	}

	public void setUnidadeOrganizacional(List<Integer> unidadeOrganizacional) {
		this.unidadeOrganizacional = unidadeOrganizacional;
	}

}