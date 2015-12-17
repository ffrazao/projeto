package br.gov.df.emater.aterwebsrv.modelo.atividade;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(name = "atividade", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class Atividade extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy="atividade")
	private List<AtividadeAssunto> atividadeAssuntoList;

	@OneToMany(mappedBy="atividade")
	private List<AtividadePessoa> atividadePessoaList;

	private String codigo;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar data;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="metodo_id")
	private Metodo metodo;

	public List<AtividadeAssunto> getAtividadeAssuntoList() {
		return atividadeAssuntoList;
	}

	public List<AtividadePessoa> getAtividadePessoaList() {
		return atividadePessoaList;
	}

	public String getCodigo() {
		return codigo;
	}

	public Calendar getData() {
		return data;
	}

	public Integer getId() {
		return id;
	}

	public Metodo getMetodo() {
		return metodo;
	}

	public void setAtividadeAssuntoList(List<AtividadeAssunto> atividadeAssuntoList) {
		this.atividadeAssuntoList = atividadeAssuntoList;
	}

	public void setAtividadePessoaList(List<AtividadePessoa> atividadePessoaList) {
		this.atividadePessoaList = atividadePessoaList;
	}		

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public void setData(Calendar data) {
		this.data = data;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public void setMetodo(Metodo metodo) {
		this.metodo = metodo;
	}
	
	
}