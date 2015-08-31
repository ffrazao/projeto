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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadePrioridade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeSituacao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerMilisegundos;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerMilisegundos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "atividade_atual_vi", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class AtividadeAtualVi extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "ocorrencia_id")
	private Ocorrencia ocorrencia;

	@Column(name = "percentual_conclusao")
	private Integer percentualConclusao;

	@Enumerated(EnumType.STRING)
	@Column(name = "prioridade")
	private AtividadePrioridade prioridade;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerMilisegundos.class)
	@JsonDeserialize(using = JsonDeserializerMilisegundos.class)
	@Column(name = "registro")
	private Calendar registro;

	@Enumerated(EnumType.STRING)
	@Column(name = "situacao")
	private AtividadeSituacao situacao;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	public AtividadeAtualVi() {
		super();
	}

	public AtividadeAtualVi(Integer id) {
		super(id);
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Ocorrencia getOcorrencia() {
		return ocorrencia;
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

	public AtividadeSituacao getSituacao() {
		return situacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setOcorrencia(Ocorrencia ocorrencia) {
		this.ocorrencia = ocorrencia;
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

	public void setSituacao(AtividadeSituacao situacao) {
		this.situacao = situacao;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}