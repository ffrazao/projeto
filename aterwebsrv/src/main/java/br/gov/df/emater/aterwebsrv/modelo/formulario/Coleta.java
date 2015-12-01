package br.gov.df.emater.aterwebsrv.modelo.formulario;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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

	@Enumerated(EnumType.STRING)
	private Confirmacao completada;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar data;

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

	@OneToMany(mappedBy = "coleta")
	private List<Valor> valorList;

	public Confirmacao getCompletada() {
		return completada;
	}

	public Calendar getData() {
		return data;
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

	public List<Valor> getValorList() {
		return valorList;
	}

	public void setCompletada(Confirmacao completada) {
		this.completada = completada;
	}

	public void setData(Calendar data) {
		this.data = data;
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

	public void setValorList(List<Valor> valorList) {
		this.valorList = valorList;
	}

}