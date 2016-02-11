package br.gov.df.emater.aterwebsrv.modelo.atividade;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadePrioridade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerMilisegundos;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerMilisegundos;

@Entity
@Table(name = "ocorrencia", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class Ocorrencia extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "atividade_id")
	private Atividade atividade;

	@Column(name = "duracao_suspencao")
	private Integer duracaoSuspencao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Enumerated(EnumType.STRING)
	private Confirmacao incidente;

	@Column(name = "inicio_suspensao")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerMilisegundos.class)
	@JsonDeserialize(using = JsonDeserializerMilisegundos.class)
	private Calendar inicioSuspensao;

	@Column(name = "motivo_suspensao")
	@Lob
	private String motivoSuspensao;

	@Column(name = "percentual_conclusao")
	private Integer percentualConclusao;

	@Enumerated(EnumType.STRING)
	@Column(name = "prioridade")
	private AtividadePrioridade prioridade;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerMilisegundos.class)
	@JsonDeserialize(using = JsonDeserializerMilisegundos.class)
	private Calendar registro;

	@Lob
	private String relato;

	@Enumerated(EnumType.STRING)
	@Column(name = "situacao")
	private AtividadeSituacao situacao;

	@Column(name = "termino_suspensao")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerMilisegundos.class)
	@JsonDeserialize(using = JsonDeserializerMilisegundos.class)
	private Calendar terminoSuspensao;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	public Ocorrencia() {
		super();
	}

	public Ocorrencia(Integer id) {
		super(id);
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public Integer getDuracaoSuspencao() {
		return duracaoSuspencao;
	}

	public Integer getId() {
		return id;
	}

	public Confirmacao getIncidente() {
		return incidente;
	}

	public Calendar getInicioSuspensao() {
		return inicioSuspensao;
	}

	public String getMotivoSuspensao() {
		return motivoSuspensao;
	}

	public Integer getPercentualConclusao() {
		return percentualConclusao;
	}

	public AtividadePrioridade getPrioridade() {
		return prioridade;
	}

	public Calendar getRegistro() {
		return registro;
	}

	public String getRelato() {
		return relato;
	}

	public AtividadeSituacao getSituacao() {
		return situacao;
	}

	public Calendar getTerminoSuspensao() {
		return terminoSuspensao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public void setDuracaoSuspencao(Integer duracaoSuspencao) {
		this.duracaoSuspencao = duracaoSuspencao;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setIncidente(Confirmacao incidente) {
		this.incidente = incidente;
	}

	public void setInicioSuspensao(Calendar inicioSuspensao) {
		this.inicioSuspensao = inicioSuspensao;
	}

	public void setMotivoSuspensao(String motivoSuspensao) {
		this.motivoSuspensao = motivoSuspensao;
	}

	public void setPercentualConclusao(Integer percentualConclusao) {
		this.percentualConclusao = percentualConclusao;
	}

	public void setPrioridade(AtividadePrioridade prioridade) {
		this.prioridade = prioridade;
	}

	public void setRegistro(Calendar registro) {
		this.registro = registro;
	}

	public void setRelato(String relato) {
		this.relato = relato;
	}

	public void setSituacao(AtividadeSituacao situacao) {
		this.situacao = situacao;
	}

	public void setTerminoSuspensao(Calendar terminoSuspensao) {
		this.terminoSuspensao = terminoSuspensao;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}