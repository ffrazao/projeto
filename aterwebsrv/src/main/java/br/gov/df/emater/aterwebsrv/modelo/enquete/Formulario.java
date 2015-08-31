package br.gov.df.emater.aterwebsrv.modelo.enquete;

import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "formulario", schema = EntidadeBase.ENQUETE_SCHEMA)
public class Formulario extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "codigo")
	private String codigo;

	@OneToMany(mappedBy = "formulario", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<FormularioDirecionamento> formularioDirecionamentoList;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "inicio")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inicio;

	@Column(name = "nome")
	private String nome;

	@OneToMany(mappedBy = "formulario", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Pergunta> perguntaList;

	@Column(name = "permitir_excluir_resposta")
	@Enumerated(EnumType.STRING)
	private Confirmacao permitirExcluirResposta;

	@Column(name = "permitir_reenviar_resposta")
	@Enumerated(EnumType.STRING)
	private Confirmacao permitirReenviarResposta;

	@Column(name = "permitir_resposta_anonima")
	@Enumerated(EnumType.STRING)
	private Confirmacao permitirRespostaAnonima;

	@Column(name = "resposta_direcionada")
	@Enumerated(EnumType.STRING)
	private Confirmacao respostaDirecionada;

	@Column(name = "termino")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar termino;

	public String getCodigo() {
		return codigo;
	}

	public List<FormularioDirecionamento> getFormularioDirecionamentoList() {
		return formularioDirecionamentoList;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public String getNome() {
		return nome;
	}

	public List<Pergunta> getPerguntaList() {
		return perguntaList;
	}

	public Confirmacao getPermitirExcluirResposta() {
		return permitirExcluirResposta;
	}

	public Confirmacao getPermitirReenviarResposta() {
		return permitirReenviarResposta;
	}

	public Confirmacao getPermitirRespostaAnonima() {
		return permitirRespostaAnonima;
	}

	public Confirmacao getRespostaDirecionada() {
		return respostaDirecionada;
	}

	public Calendar getTermino() {
		return termino;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setFormularioDirecionamentoList(List<FormularioDirecionamento> formularioDirecionamentoList) {
		this.formularioDirecionamentoList = formularioDirecionamentoList;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPerguntaList(List<Pergunta> perguntaList) {
		this.perguntaList = perguntaList;
	}

	public void setPermitirExcluirResposta(Confirmacao permitirExcluirResposta) {
		this.permitirExcluirResposta = permitirExcluirResposta;
	}

	public void setPermitirReenviarResposta(Confirmacao permitirReenviarResposta) {
		this.permitirReenviarResposta = permitirReenviarResposta;
	}

	public void setPermitirRespostaAnonima(Confirmacao permitirRespostaAnonima) {
		this.permitirRespostaAnonima = permitirRespostaAnonima;
	}

	public void setRespostaDirecionada(Confirmacao respostaDirecionada) {
		this.respostaDirecionada = respostaDirecionada;
	}

	public void setTermino(Calendar termino) {
		this.termino = termino;
	}

}