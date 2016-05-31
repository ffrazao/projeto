package br.gov.df.emater.aterwebsrv.modelo.atividade;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadePessoaParticipacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(name = "atividade_pessoa", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class AtividadePessoa extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "atividade_id")
	private Atividade atividade;

	@Enumerated(EnumType.STRING)
	private Confirmacao ativo;

	private Integer duracao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inicio;

	@Enumerated(EnumType.STRING)
	private AtividadePessoaParticipacao participacao;

	@ManyToOne
	@JoinColumn(name = "pessoa_id")
	private Pessoa pessoa;

	@Enumerated(EnumType.STRING)
	private Confirmacao responsavel;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar termino;

	@ManyToOne
	@JoinColumn(name = "unidade_organizacional_id")
	private UnidadeOrganizacional unidadeOrganizacional;

	public AtividadePessoa() {
		super();
	}

	public AtividadePessoa(Integer id) {
		super(id);
	}

	public AtividadePessoa(UnidadeOrganizacional unidadeOrganizacional, Pessoa pessoa, Calendar inicio, AtividadePessoaParticipacao participacao, Confirmacao responsavel) {
		super();
		this.unidadeOrganizacional = unidadeOrganizacional;
		this.pessoa = pessoa;
		this.inicio = inicio;
		this.participacao = participacao;
		this.responsavel = responsavel;
		this.ativo = Confirmacao.S;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public Confirmacao getAtivo() {
		return ativo;
	}

	public Integer getDuracao() {
		return duracao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public AtividadePessoaParticipacao getParticipacao() {
		return participacao;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public Confirmacao getResponsavel() {
		return responsavel;
	}

	public Calendar getTermino() {
		return termino;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public void setAtivo(Confirmacao ativo) {
		this.ativo = ativo;
	}

	public void setDuracao(Integer duracao) {
		this.duracao = duracao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public void setParticipacao(AtividadePessoaParticipacao participacao) {
		this.participacao = participacao;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public void setResponsavel(Confirmacao responsavel) {
		this.responsavel = responsavel;
	}

	public void setTermino(Calendar termino) {
		this.termino = termino;
	}

}