package br.gov.df.emater.aterwebsrv.modelo.sistema;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
 * Classe persistente dos log acao do sistema.
 * 
 */
@Entity
@Table(name = "log_acao", schema = EntidadeBase.SISTEMA_SCHEMA)
public class LogAcao extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;
	private String administrador;

	@Column(name = "comando_codigo")
	private String comandoCodigo;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerTimestamp.class)
	@JsonDeserialize(using = JsonDeserializerTimestamp.class)
	private Calendar data;

	@Column(name = "empresa_sigla")
	private String empresaSigla;

	@Lob
	private String erro;

	@Column(name = "funcionalidade_codigo")
	private String funcionalidadeCodigo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "modulo_codigo")
	private String moduloCodigo;

	@Column(name = "nome_usuario")
	private String nomeUsuario;

	@Lob
	private String requisicao;

	@Lob
	private String resposta;

	@Column(name = "unidade_organizacional_sigla")
	private String unidadeOrganizacionalSigla;

	@Column(name = "usuario_id")
	private Integer usuarioId;

	public LogAcao() {

	}

	public LogAcao(Serializable id) {
		super(id);
	}

	public String getAdministrador() {
		return administrador;
	}

	public String getComandoCodigo() {
		return comandoCodigo;
	}

	public Calendar getData() {
		return data;
	}

	public String getEmpresaSigla() {
		return empresaSigla;
	}

	public String getErro() {
		return erro;
	}

	public String getFuncionalidadeCodigo() {
		return funcionalidadeCodigo;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getModuloCodigo() {
		return moduloCodigo;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public String getRequisicao() {
		return requisicao;
	}

	public String getResposta() {
		return resposta;
	}

	public String getUnidadeOrganizacionalSigla() {
		return unidadeOrganizacionalSigla;
	}

	public Integer getUsuarioId() {
		return usuarioId;
	}

	public void setAdministrador(String administrador) {
		this.administrador = administrador;
	}

	public void setComandoCodigo(String comandoCodigo) {
		this.comandoCodigo = comandoCodigo;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public void setEmpresaSigla(String empresaSigla) {
		this.empresaSigla = empresaSigla;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	public void setFuncionalidadeCodigo(String funcionalidadeCodigo) {
		this.funcionalidadeCodigo = funcionalidadeCodigo;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setModuloCodigo(String moduloCodigo) {
		this.moduloCodigo = moduloCodigo;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public void setRequisicao(String requisicao) {
		this.requisicao = requisicao;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
	}

	public void setUnidadeOrganizacionalSigla(String unidadeOrganizacionalSigla) {
		this.unidadeOrganizacionalSigla = unidadeOrganizacionalSigla;
	}

	public void setUsuarioId(Integer usuarioId) {
		this.usuarioId = usuarioId;
	}
}