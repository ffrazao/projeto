package br.gov.df.emater.aterwebsrv.modelo.atividade;

import java.util.Calendar;

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

	@Enumerated(EnumType.STRING)
	private Confirmacao automatico;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Enumerated(EnumType.STRING)
	private Confirmacao incidente;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerMilisegundos.class)
	@JsonDeserialize(using = JsonDeserializerMilisegundos.class)
	private Calendar registro;

	@Lob
	private String relato;

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

	public Confirmacao getAutomatico() {
		return automatico;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Confirmacao getIncidente() {
		return incidente;
	}

	public Calendar getRegistro() {
		return registro;
	}

	public String getRelato() {
		return relato;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public void setAutomatico(Confirmacao automatico) {
		this.automatico = automatico;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setIncidente(Confirmacao incidente) {
		this.incidente = incidente;
	}

	public void setRegistro(Calendar registro) {
		this.registro = registro;
	}

	public void setRelato(String relato) {
		this.relato = relato;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}