package br.gov.df.emater.aterwebsrv.modelo.credito_rural;

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
import br.gov.df.emater.aterwebsrv.modelo.dominio.ParecerTecnicoCodigo;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(name = "parecer_tecnico", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class ParecerTecnico extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private ParecerTecnicoCodigo codigo;

	@Lob
	private String conteudo;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar data;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "projeto_credito_id")
	private ProjetoCreditoRural projetoCredito;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	public ParecerTecnico() {
		super();
	}

	public ParecerTecnico(Integer id) {
		super(id);
	}

	public ParecerTecnicoCodigo getCodigo() {
		return codigo;
	}

	public String getConteudo() {
		return conteudo;
	}

	public Calendar getData() {
		return data;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public ProjetoCreditoRural getProjetoCredito() {
		return projetoCredito;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setCodigo(ParecerTecnicoCodigo codigo) {
		this.codigo = codigo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public void setData(Calendar data) {
		this.data = data;
	}
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	public void setProjetoCredito(ProjetoCreditoRural projetoCredito) {
		this.projetoCredito = projetoCredito;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}