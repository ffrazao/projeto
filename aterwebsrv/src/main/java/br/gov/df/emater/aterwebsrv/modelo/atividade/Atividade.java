package br.gov.df.emater.aterwebsrv.modelo.atividade;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeFinalidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeFormato;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeNatureza;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadePrioridade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeSituacao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(name = "atividade", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class Atividade extends EntidadeBase implements _ChavePrimaria<Integer> {

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

	@OneToMany(mappedBy = "atividade")
	private List<AtividadeAssunto> atividadeAssuntoList;

	@OneToMany(mappedBy = "atividade")
	private List<AtividadePessoa> atividadePessoaList;

	private String codigo;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar conclusao;

	@Lob
	private String detalhamento;

	@Column(name = "duracao_estimada")
	private Integer duracaoEstimada;

	@Column(name = "duracao_real")
	private Integer duracaoReal;

	@Enumerated(EnumType.STRING)
	private AtividadeFinalidade finalidade;

	@Enumerated(EnumType.STRING)
	private AtividadeFormato formato;

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
	@JoinColumn(name = "inclusao_usuario_id", updatable = false)
	private Usuario inclusaoUsuario;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inicio;

	@ManyToOne
	@JoinColumn(name = "metodo_id")
	private Metodo metodo;

	@Enumerated(EnumType.STRING)
	private AtividadeNatureza natureza;

	@Column(name = "percentual_conclusao")
	private Integer percentualConclusao;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar previsao_conclusao;

	@Enumerated(EnumType.STRING)
	private AtividadePrioridade prioridade;

	@Column(name = "publico_estimado")
	private Integer publicoEstimado;

	@Column(name = "publico_real")
	private Integer publicoReal;

	@Enumerated(EnumType.STRING)
	private AtividadeSituacao situacao;

	public Atividade() {
		super();
	}

	public Atividade(Integer id) {
		super(id);
	}

	public Calendar getAlteracaoData() {
		return alteracaoData;
	}

	public Usuario getAlteracaoUsuario() {
		return alteracaoUsuario;
	}

	public List<AtividadeAssunto> getAtividadeAssuntoList() {
		return atividadeAssuntoList;
	}

	public List<AtividadePessoa> getAtividadePessoaList() {
		return atividadePessoaList;
	}

	public String getCodigo() {
		return codigo;
	}

	public Calendar getConclusao() {
		return conclusao;
	}

	public String getDetalhamento() {
		return detalhamento;
	}

	public Integer getDuracaoEstimada() {
		return duracaoEstimada;
	}

	public Integer getDuracaoReal() {
		return duracaoReal;
	}

	public AtividadeFinalidade getFinalidade() {
		return finalidade;
	}

	public AtividadeFormato getFormato() {
		return formato;
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

	public Calendar getInicio() {
		return inicio;
	}

	public Metodo getMetodo() {
		return metodo;
	}

	public AtividadeNatureza getNatureza() {
		return natureza;
	}

	public Integer getPercentualConclusao() {
		return percentualConclusao;
	}

	public Calendar getPrevisao_conclusao() {
		return previsao_conclusao;
	}

	public AtividadePrioridade getPrioridade() {
		return prioridade;
	}

	public Integer getPublicoEstimado() {
		return publicoEstimado;
	}

	public Integer getPublicoReal() {
		return publicoReal;
	}

	public AtividadeSituacao getSituacao() {
		return situacao;
	}

	public void setAlteracaoData(Calendar alteracaoData) {
		this.alteracaoData = alteracaoData;
	}

	public void setAlteracaoUsuario(Usuario alteracaoUsuario) {
		this.alteracaoUsuario = alteracaoUsuario;
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

	public void setConclusao(Calendar conclusao) {
		this.conclusao = conclusao;
	}

	public void setDetalhamento(String detalhamento) {
		this.detalhamento = detalhamento;
	}

	public void setDuracaoEstimada(Integer duracaoEstimada) {
		this.duracaoEstimada = duracaoEstimada;
	}

	public void setDuracaoReal(Integer duracaoReal) {
		this.duracaoReal = duracaoReal;
	}

	public void setFinalidade(AtividadeFinalidade finalidade) {
		this.finalidade = finalidade;
	}

	public void setFormato(AtividadeFormato formato) {
		this.formato = formato;
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

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public void setMetodo(Metodo metodo) {
		this.metodo = metodo;
	}

	public void setNatureza(AtividadeNatureza natureza) {
		this.natureza = natureza;
	}

	public void setPercentualConclusao(Integer percentualConclusao) {
		this.percentualConclusao = percentualConclusao;
	}

	public void setPrevisao_conclusao(Calendar previsao_conclusao) {
		this.previsao_conclusao = previsao_conclusao;
	}

	public void setPrioridade(AtividadePrioridade prioridade) {
		this.prioridade = prioridade;
	}

	public void setPublicoEstimado(Integer publicoEstimado) {
		this.publicoEstimado = publicoEstimado;
	}

	public void setPublicoReal(Integer publicoReal) {
		this.publicoReal = publicoReal;
	}

	public void setSituacao(AtividadeSituacao situacao) {
		this.situacao = situacao;
	}

}