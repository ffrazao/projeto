package br.gov.df.emater.aterwebsrv.modelo.formulario;

import java.io.Serializable;
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
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo._LogInclusaoAlteracao;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(schema = EntidadeBase.FORMULARIO_SCHEMA)
public class Coleta extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<Coleta>, _LogInclusaoAlteracao {

	private static final long serialVersionUID = 1L;

	@Column(name = "alteracao_data", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar alteracaoData;

	@ManyToOne
	@JoinColumn(name = "alteracao_usuario_id")
	private Usuario alteracaoUsuario;

	@Column(name = "chave_sisater")
	private String chaveSisater;

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

	@Column(name = "inclusao_data", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inclusaoData;

	@ManyToOne
	@JoinColumn(name = "inclusao_usuario_id")
	private Usuario inclusaoUsuario;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pessoa_id")
	private Pessoa pessoa;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "propriedade_rural_id")
	private PropriedadeRural propriedadeRural;

	@Transient
	private Object valor;

	@Lob
	@Column(name = "valor")
	private String valorString;

	public Coleta() {
	}

	public Coleta(Integer id, FormularioVersao formularioVersao, Usuario usuarioInclusao, Calendar inclusaoData, Usuario alteracaoUsuario, Calendar alteracaoData, Calendar dataColeta, Confirmacao finalizada, Pessoa pessoa, PropriedadeRural propriedadeRural, Object valor,
			String valorString) {
		this(id);
		this.setFormularioVersao(formularioVersao);
		this.setInclusaoUsuario(inclusaoUsuario);
		this.setInclusaoData(inclusaoData);
		this.setAlteracaoUsuario(alteracaoUsuario);
		this.setAlteracaoData(alteracaoData);
		this.setDataColeta(dataColeta);
		this.setFinalizada(finalizada);
		this.setPessoa(pessoa);
		this.setPropriedadeRural(propriedadeRural);
		this.setValor(valor);
		this.setValorString(valorString);
	}

	public Coleta(Serializable id) {
		super(id);
	}

	public Calendar getAlteracaoData() {
		return alteracaoData;
	}

	public Usuario getAlteracaoUsuario() {
		return alteracaoUsuario;
	}

	public String getChaveSisater() {
		return chaveSisater;
	}

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

	public Calendar getInclusaoData() {
		return inclusaoData;
	}

	public Usuario getInclusaoUsuario() {
		return inclusaoUsuario;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public PropriedadeRural getPropriedadeRural() {
		return propriedadeRural;
	}

	public Object getValor() {
		return valor;
	}

	public String getValorString() {
		return valorString;
	}

	@Override
	public Coleta infoBasica() {
		return new Coleta(this.getId(), this.getFormularioVersao() == null ? null : new FormularioVersao(this.getFormularioVersao().getId()), this.getInclusaoUsuario() == null ? null : this.getInclusaoUsuario().infoBasica(), this.getInclusaoData(),
				this.getAlteracaoUsuario() == null ? null : this.getInclusaoUsuario().infoBasica(), this.getAlteracaoData(), this.getDataColeta(), this.getFinalizada(), this.getPessoa() == null ? null : this.getPessoa().infoBasica(),
				this.getPropriedadeRural() == null ? null : this.getPropriedadeRural().infoBasica(), this.getValor(), this.getValorString());
	}

	public void setAlteracaoData(Calendar alteracaoData) {
		this.alteracaoData = alteracaoData;
	}

	public void setAlteracaoUsuario(Usuario alteracaoUsuario) {
		this.alteracaoUsuario = alteracaoUsuario;
	}

	public void setChaveSisater(String chaveSisater) {
		this.chaveSisater = chaveSisater;
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

	public void setInclusaoData(Calendar inclusaoData) {
		this.inclusaoData = inclusaoData;
	}

	public void setInclusaoUsuario(Usuario inclusaoUsuario) {
		this.inclusaoUsuario = inclusaoUsuario;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public void setPropriedadeRural(PropriedadeRural propriedadeRural) {
		this.propriedadeRural = propriedadeRural;
	}

	public void setValor(Object valor) {
		this.valor = valor;
	}

	public void setValorString(String valorString) {
		this.valorString = valorString;
	}

}