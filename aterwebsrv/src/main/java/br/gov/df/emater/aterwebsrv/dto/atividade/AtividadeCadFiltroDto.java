package br.gov.df.emater.aterwebsrv.dto.atividade;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;
import br.gov.df.emater.aterwebsrv.dto.TagDto;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Assunto;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Metodo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGeracao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoCategoria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoSegmento;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

public class AtividadeCadFiltroDto extends CadFiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private Assunto assunto;
	
	private Set<PublicoAlvoCategoria> categoria;

	private Set<TagDto> codigoList;

	private List<Comunidade> comunidadeList;

	private Set<TagDto> demandanteList;

	private List<PessoaJuridica> empresaList;

	private Set<TagDto> executorList;

	private Set<PessoaGenero> genero;
	
	private Set<PessoaGeracao> geracao;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inicio;

	private Integer metaTatica;

	private Metodo metodo;

	private Set<Confirmacao> organizacao;

	private Set<PublicoAlvoSegmento> segmento;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar termino;

	private List<UnidadeOrganizacional> unidadeOrganizacionalList;

	public Assunto getAssunto() {
		return assunto;
	}

	public Set<PublicoAlvoCategoria> getCategoria() {
		return categoria;
	}

	public Set<TagDto> getCodigoList() {
		return codigoList;
	}

	public List<Comunidade> getComunidadeList() {
		return comunidadeList;
	}

	public Set<TagDto> getDemandanteList() {
		return demandanteList;
	}

	public List<PessoaJuridica> getEmpresaList() {
		return empresaList;
	}

	public Set<TagDto> getExecutorList() {
		return executorList;
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

	public Integer getMetaTatica() {
		return metaTatica;
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

	public List<UnidadeOrganizacional> getUnidadeOrganizacionalList() {
		return unidadeOrganizacionalList;
	}

	public void setAssunto(Assunto assunto) {
		this.assunto = assunto;
	}

	public void setCategoria(Set<PublicoAlvoCategoria> categoria) {
		this.categoria = categoria;
	}

	public void setCodigoList(Set<TagDto> codigoList) {
		this.codigoList = codigoList;
	}

	public void setComunidadeList(List<Comunidade> comunidadeList) {
		this.comunidadeList = comunidadeList;
	}

	public void setDemandanteList(Set<TagDto> demandanteList) {
		this.demandanteList = demandanteList;
	}

	public void setEmpresaList(List<PessoaJuridica> empresaList) {
		this.empresaList = empresaList;
	}

	public void setExecutorList(Set<TagDto> executorList) {
		this.executorList = executorList;
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

	public void setMetaTatica(Integer metaTatica) {
		this.metaTatica = metaTatica;
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

	public void setUnidadeOrganizacionalList(List<UnidadeOrganizacional> unidadeOrganizacionalList) {
		this.unidadeOrganizacionalList = unidadeOrganizacionalList;
	}

}