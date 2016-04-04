package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.util.Calendar;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.bytecode.internal.javassist.FieldHandled;
import org.hibernate.bytecode.internal.javassist.FieldHandler;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerDataHora;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerDataHora;

@Entity
@Table(name = "arquivo", schema = EntidadeBase.PESSOA_SCHEMA)
// @Indexed
public class Arquivo extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<Arquivo>, FieldHandled {

	private static final long serialVersionUID = 1L;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] conteudo;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerDataHora.class)
	@JsonDeserialize(using = JsonDeserializerDataHora.class)
	@Column(name = "data_upload")
	private Calendar dataUpload;

	private String extensao;

	@Transient
	@JsonIgnore
	private FieldHandler fieldHandler;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	// @Field(index = Index.YES, store = Store.YES)
	private String md5;

	// @Field(index = Index.YES, store = Store.YES)
	@Column(name = "nome_original")
	private String nomeOriginal;

	private Integer tamanho;

	private String tipo;

	public Arquivo() {
	}

	public Arquivo(Integer id) {
		super(id);
	}

	public Arquivo(String md5) {
		setMd5(md5);
	}
	
	@Column(name = "local_diretorio_web")
	private String localDiretorioWeb;

	public String getLocalDiretorioWeb() {
		return localDiretorioWeb;
	}

	public void setLocalDiretorioWeb(String localDiretorioWeb) {
		this.localDiretorioWeb = localDiretorioWeb;
	}

	public Arquivo(Integer id, String md5, String nomeOriginal, Calendar dataUpload, String extensao, Integer tamanho, String tipo, String localDiretorioWeb) {
		this(id);
		this.dataUpload = dataUpload;
		this.extensao = extensao;
		this.md5 = md5;
		this.nomeOriginal = nomeOriginal;
		this.tamanho = tamanho;
		this.tipo = tipo;
		this.localDiretorioWeb = localDiretorioWeb;
	}

	public byte[] getConteudo() {
		if (getFieldHandler() != null) {
			return (byte[]) getFieldHandler().readObject(this, "conteudo", this.conteudo);
		}
		return this.conteudo;
	}

	public Calendar getDataUpload() {
		return dataUpload;
	}

	public String getExtensao() {
		return extensao;
	}

	@Override
	public FieldHandler getFieldHandler() {
		return this.fieldHandler;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getMd5() {
		return md5;
	}

	public String getNomeOriginal() {
		return nomeOriginal;
	}

	public Integer getTamanho() {
		return tamanho;
	}

	public String getTipo() {
		return tipo;
	}

	public void setConteudo(byte[] conteudo) {
		if (getFieldHandler() != null) {
			getFieldHandler().writeObject(this, "conteudo", this.conteudo, conteudo);
			return;
		}
		this.conteudo = conteudo;
	}

	public void setDataUpload(Calendar dataUpload) {
		this.dataUpload = dataUpload;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}

	@Override
	public void setFieldHandler(FieldHandler fieldHandler) {
		this.fieldHandler = fieldHandler;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setMd5(String md5) {
		if (md5 != null) {
			md5 = md5.toLowerCase();
		}
		this.md5 = md5;
	}

	public void setNomeOriginal(String nomeOriginal) {
		this.nomeOriginal = nomeOriginal;
	}

	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public Arquivo infoBasica() {
		return new Arquivo(this.getId(), this.getMd5(), this.getNomeOriginal(), this.getDataUpload(), this.getExtensao(), this.getTamanho(), this.getTipo(), this.getLocalDiretorioWeb());
	}

}