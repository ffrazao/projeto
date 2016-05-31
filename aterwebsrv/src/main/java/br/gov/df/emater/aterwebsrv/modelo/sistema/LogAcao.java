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

	private String browser;

	@Column(name = "comando_chain")
	private String comandoChain;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerTimestamp.class)
	@JsonDeserialize(using = JsonDeserializerTimestamp.class)
	private Calendar data;

	@Column(name = "data_login")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerTimestamp.class)
	@JsonDeserialize(using = JsonDeserializerTimestamp.class)
	private Calendar dataLogin;

	@Column(name = "endereco_origem")
	private String enderecoOrigem;

	@Column(name = "endereco_referencia")
	private String enderecoReferencia;

	@Lob
	private String erro;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "modulo_id")
	private Integer moduloId;

	@Column(name = "nome_usuario")
	private String nomeUsuario;

	@Column(name = "numero_ip")
	private String numeroIp;

	@Lob
	private String requisicao;

	@Lob
	private String resposta;

	@Column(name = "unidade_organizacional_id")
	private Integer unidadeOrganizacionalId;

	public LogAcao() {
	}

	public LogAcao(Serializable id) {
		super(id);
	}

	public String getBrowser() {
		return browser;
	}

	public String getComandoChain() {
		return comandoChain;
	}

	public Calendar getData() {
		return data;
	}

	public Calendar getDataLogin() {
		return dataLogin;
	}

	public String getEnderecoOrigem() {
		return enderecoOrigem;
	}

	public String getEnderecoReferencia() {
		return enderecoReferencia;
	}

	public String getErro() {
		return erro;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Integer getModuloId() {
		return moduloId;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public String getNumeroIp() {
		return numeroIp;
	}

	public String getRequisicao() {
		return requisicao;
	}

	public String getResposta() {
		return resposta;
	}

	public Integer getUnidadeOrganizacionalId() {
		return unidadeOrganizacionalId;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public void setComandoChain(String comandoChain) {
		this.comandoChain = comandoChain;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public void setDataLogin(Calendar dataLogin) {
		this.dataLogin = dataLogin;
	}

	public void setEnderecoOrigem(String enderecoOrigem) {
		this.enderecoOrigem = enderecoOrigem;
	}

	public void setEnderecoReferencia(String enderecoReferencia) {
		this.enderecoReferencia = enderecoReferencia;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setModuloId(Integer moduloId) {
		this.moduloId = moduloId;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public void setNumeroIp(String numeroIp) {
		this.numeroIp = numeroIp;
	}

	public void setRequisicao(String requisicao) {
		this.requisicao = requisicao;
	}

	public void setResposta(String resposta) {
		this.resposta = resposta;
	}

	public void setUnidadeOrganizacionalId(Integer unidadeOrganizacionalId) {
		this.unidadeOrganizacionalId = unidadeOrganizacionalId;
	}

}