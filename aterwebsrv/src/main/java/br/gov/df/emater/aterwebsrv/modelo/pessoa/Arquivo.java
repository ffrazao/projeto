package br.gov.df.emater.aterwebsrv.modelo.pessoa;

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
import javax.persistence.Transient;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.springframework.format.annotation.DateTimeFormat;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerMilisegundos;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerMilisegundos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@Entity
@Table(name = "arquivo", schema = EntidadeBase.PESSOA_SCHEMA)
@Indexed
public class Arquivo extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "conteudo", length = 16777216)
	@Lob
	@Transient
	private byte[] conteudo;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerMilisegundos.class)
	@JsonDeserialize(using = JsonDeserializerMilisegundos.class)
	@Column(name = "data_upload")
	private Calendar dataUpload;

	@Field(index = Index.YES, store = Store.YES)
	private String descricao;

	private String extensao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Field(index = Index.YES, store = Store.YES)
	private String md5;

	@Field(index = Index.YES, store = Store.YES)
	private String nome;

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

	public byte[] getConteudo() {
		return conteudo;
	}

	public Calendar getDataUpload() {
		return dataUpload;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getExtensao() {
		return extensao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getMd5() {
		return md5;
	}

	public String getNome() {
		return nome;
	}

	public Integer getTamanho() {
		return tamanho;
	}

	public String getTipo() {
		return tipo;
	}

	public void setConteudo(byte[] conteudo) {
		this.conteudo = conteudo;
	}

	public void setDataUpload(Calendar dataUpload) {
		this.dataUpload = dataUpload;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setExtensao(String extensao) {
		this.extensao = extensao;
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

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}