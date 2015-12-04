package br.gov.df.emater.aterwebsrv.modelo.formulario;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(schema = EntidadeBase.FORMULARIO_SCHEMA)
public class Coleta extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "data_coleta")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar dataColeta;

	@Enumerated(EnumType.STRING)
	private Confirmacao finalizada;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "formulario_versao_id")
	private FormularioVersao formularioVersao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pessoa_id")
	private Pessoa pessoa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "propriedade_rural_id")
	private PropriedadeRural propriedadeRural;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@Transient
	private Object valor;

	@Lob
	@Column(name = "valor")
	private String valorString;

	public Calendar getDataColeta() {
		return dataColeta;
	}

	public Confirmacao getFinalizada() {
		return finalizada;
	}

	public FormularioVersao getFormularioVersao() {
		return formularioVersao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public PropriedadeRural getPropriedadeRural() {
		return propriedadeRural;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public Object getValor() {
		return valor;
	}

	public String getValorString() {
		return valorString;
	}

	public void setDataColeta(Calendar dataColeta) {
		this.dataColeta = dataColeta;
	}

	public void setFinalizada(Confirmacao finalizada) {
		this.finalizada = finalizada;
	}

	public void setFormularioVersao(FormularioVersao formularioVersao) {
		this.formularioVersao = formularioVersao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public void setPropriedadeRural(PropriedadeRural propriedadeRural) {
		this.propriedadeRural = propriedadeRural;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setValor(Object valor) {
		this.valor = valor;
	}

	public void setValorString(String valorString) {
		this.valorString = valorString;
	}

}