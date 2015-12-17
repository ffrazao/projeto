package br.gov.df.emater.aterwebsrv.modelo.dto;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGeracao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoCategoria;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

public class AtividadeCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private List<Integer> assunto;

	private List<Integer> beneficiario;

	private String codigo;

	private List<Integer> comunidade;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar fim;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inicio;

	private List<Integer> meta_estrategica;

	private List<Integer> meta_tatica;

	private List<Integer> metodo;

	private Set<PessoaGenero> pessoaGenero;

	private Set<PessoaGeracao> pessoaGeracao;

	private Set<PessoaSituacao> pessoaSituacao;

	private Set<PublicoAlvoCategoria> publicoAlvoCategoria;

	private List<Integer> unidade;

	public List<Integer> getAssunto() {
		return assunto;
	}

	public List<Integer> getBeneficiario() {
		return beneficiario;
	}

	public String getCodigo() {
		return codigo;
	}

	public List<Integer> getComunidade() {
		return comunidade;
	}

	public Calendar getFim() {
		return fim;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public List<Integer> getMeta_estrategica() {
		return meta_estrategica;
	}

	public List<Integer> getMeta_tatica() {
		return meta_tatica;
	}

	public List<Integer> getMetodo() {
		return metodo;
	}

	public Set<PessoaGenero> getPessoaGenero() {
		return pessoaGenero;
	}

	public Set<PessoaGeracao> getPessoaGeracao() {
		return pessoaGeracao;
	}

	public Set<PessoaSituacao> getPessoaSituacao() {
		return pessoaSituacao;
	}

	public Set<PublicoAlvoCategoria> getPublicoAlvoCategoria() {
		return publicoAlvoCategoria;
	}

	public List<Integer> getUnidade() {
		return unidade;
	}

	public void setAssunto(List<Integer> assunto) {
		this.assunto = assunto;
	}

	public void setBeneficiario(List<Integer> beneficiario) {
		this.beneficiario = beneficiario;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setComunidade(List<Integer> comunidade) {
		this.comunidade = comunidade;
	}

	public void setFim(Calendar fim) {
		this.fim = fim;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public void setMeta_estrategica(List<Integer> meta_estrategica) {
		this.meta_estrategica = meta_estrategica;
	}

	public void setMeta_tatica(List<Integer> meta_tatica) {
		this.meta_tatica = meta_tatica;
	}

	public void setMetodo(List<Integer> metodo) {
		this.metodo = metodo;
	}

	public void setPessoaGenero(Set<PessoaGenero> pessoaGenero) {
		this.pessoaGenero = pessoaGenero;
	}

	public void setPessoaGeracao(Set<PessoaGeracao> pessoaGeracao) {
		this.pessoaGeracao = pessoaGeracao;
	}

	public void setPessoaSituacao(Set<PessoaSituacao> pessoaSituacao) {
		this.pessoaSituacao = pessoaSituacao;
	}

	public void setPublicoAlvoCategoria(Set<PublicoAlvoCategoria> publicoAlvoCategoria) {
		this.publicoAlvoCategoria = publicoAlvoCategoria;
	}

	public void setUnidade(List<Integer> unidade) {
		this.unidade = unidade;
	}


}