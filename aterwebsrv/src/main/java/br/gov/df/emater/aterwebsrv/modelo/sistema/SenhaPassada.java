package br.gov.df.emater.aterwebsrv.modelo.sistema;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerTimestamp;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerTimestamp;

/**
 * Classe persistente das senhas passadas dos usuarios do sistema.
 * 
 */
@Entity
@Table(name = "senha_passada", schema = EntidadeBase.SISTEMA_SCHEMA)
public class SenhaPassada extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "data_troca")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerTimestamp.class)
	@JsonDeserialize(using = JsonDeserializerTimestamp.class)
	private Calendar dataTroca;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String senha;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	public SenhaPassada() {
	}

	public SenhaPassada(Usuario usuario, String senha) {
		setUsuario(usuario);
		setSenha(senha);
	}

	public Calendar getDataTroca() {
		return dataTroca;
	}

	public Integer getId() {
		return id;
	}

	public String getSenha() {
		return senha;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setDataTroca(Calendar dataTroca) {
		this.dataTroca = dataTroca;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}