package br.gov.df.emater.aterwebsrv.modelo.atividade;

import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AgendamentoTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AgendamentoTipoRepeticao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeFinalidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeFormato;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeNatureza;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadePrioridade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.DiaMes;
import br.gov.df.emater.aterwebsrv.modelo.dominio.DiaSemana;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Mes;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerMilisegundos;
import br.gov.df.emater.aterwebsrv.rest.json.JsonFormatarBigDecimal;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerMilisegundos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "atividade", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class Atividade extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "agendamento_contagem")
	private Integer agendamentoContagem;

	@Enumerated(EnumType.STRING)
	@Column(name = "agendamento_dia_mes")
	private DiaMes agendamentoDiaMes;

	@Enumerated(EnumType.STRING)
	@Column(name = "agendamento_dia_semana")
	private DiaSemana agendamentoDiaSemana;

	@Enumerated(EnumType.STRING)
	@Column(name = "agendamento_mes")
	private Mes agendamentoMes;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerMilisegundos.class)
	@JsonDeserialize(using = JsonDeserializerMilisegundos.class)
	@Column(name = "agendamento_termino")
	private Calendar agendamentoTermino;

	@Enumerated(EnumType.STRING)
	@Column(name = "agendamento_tipo")
	@NotNull
	private AgendamentoTipo agendamentoTipo;

	@Enumerated(EnumType.STRING)
	@Column(name = "agendamento_tipo_repeticao")
	private AgendamentoTipoRepeticao agendamentoTipoRepeticao;

	@OneToMany(mappedBy = "atividade")
	private List<AtividadeArquivo> atividadeArquivoList;

	@OneToMany(mappedBy = "atividade")
	@NotNull
	@Size(min = 1)
	private List<AtividadeAssunto> atividadeAssuntoList;

	@OneToMany(mappedBy = "atividade")
	@NotNull
	@Size(min = 2)
	private List<AtividadePessoa> atividadePessoaList;

	@OneToMany(mappedBy = "atividade")
	private List<AtividadeRestricao> atividadeRestricaoList;

	@Column(name = "codigo")
	@NotBlank
	private String codigo;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerMilisegundos.class)
	@JsonDeserialize(using = JsonDeserializerMilisegundos.class)
	@Column(name = "conclusao")
	private Calendar conclusao;

	@Enumerated(EnumType.STRING)
	@Column(name = "dia_todo")
	@NotNull
	private Confirmacao diaTodo;

	@Column(name = "duracao_prevista")
	private Integer duracaoPrevista;

	@Column(name = "duracao_real")
	private Integer duracaoReal;

	@Enumerated(EnumType.STRING)
	@Column(name = "finalidade")
	@NotNull
	private AtividadeFinalidade finalidade;

	@Enumerated(EnumType.STRING)
	@Column(name = "formato")
	@NotNull
	private AtividadeFormato formato;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerMilisegundos.class)
	@JsonDeserialize(using = JsonDeserializerMilisegundos.class)
	@Column(name = "inicio")
	private Calendar inicio;

	@Lob
	@Column(name = "justificativa")
	private String justificativa;

	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	@Column(name = "latitude")
	private BigDecimal latitude;

	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	@Column(name = "longitude")
	private BigDecimal longitude;

	@ManyToOne
	@JoinColumn(name = "metodo_id")
	@NotNull
	private Metodo metodo;

	@Enumerated(EnumType.STRING)
	@Column(name = "natureza")
	@NotNull
	private AtividadeNatureza natureza;

	@OneToMany(mappedBy = "atividade")
	private List<Ocorrencia> ocorrenciaList;

	@Column(name = "percentual_conclusao")
	private Integer percentualConclusao;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerMilisegundos.class)
	@JsonDeserialize(using = JsonDeserializerMilisegundos.class)
	@Column(name = "previsao_conclusao")
	private Calendar previsaoConclusao;

	@OneToMany(mappedBy = "atividade")
	private List<AtividadeRelacionamento> principalAtividadeList;

	@Enumerated(EnumType.STRING)
	@Column(name = "prioridade")
	@NotNull
	private AtividadePrioridade prioridade;

	@Column(name = "publico_estimado")
	private Integer publicoEstimado;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerMilisegundos.class)
	@JsonDeserialize(using = JsonDeserializerMilisegundos.class)
	@Column(name = "registro")
	@NotNull
	private Calendar registro;

	@Enumerated(EnumType.STRING)
	@Column(name = "situacao")
	@NotNull
	private AtividadeSituacao situacao;

	@OneToMany(mappedBy = "principalAtividade")
	private List<AtividadeRelacionamento> subatividadeList;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	@NotNull
	private Usuario usuario;

	@NumberFormat(style = Style.CURRENCY)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	@Column(name = "valor_estimado")
	private BigDecimal valorEstimado;

	@NumberFormat(style = Style.CURRENCY)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	@Column(name = "valor_real")
	private BigDecimal valorReal;

	public Atividade() {
		super();
	}

	public Atividade(Integer id) {
		super(id);
	}

	public Integer getAgendamentoContagem() {
		return agendamentoContagem;
	}

	public DiaMes getAgendamentoDiaMes() {
		return agendamentoDiaMes;
	}

	public DiaSemana getAgendamentoDiaSemana() {
		return agendamentoDiaSemana;
	}

	public Mes getAgendamentoMes() {
		return agendamentoMes;
	}

	public Calendar getAgendamentoTermino() {
		return agendamentoTermino;
	}

	public AgendamentoTipo getAgendamentoTipo() {
		return agendamentoTipo;
	}

	public AgendamentoTipoRepeticao getAgendamentoTipoRepeticao() {
		return agendamentoTipoRepeticao;
	}

	public List<AtividadeArquivo> getAtividadeArquivoList() {
		return atividadeArquivoList;
	}

	public List<AtividadeAssunto> getAtividadeAssuntoList() {
		return atividadeAssuntoList;
	}

	public List<AtividadePessoa> getAtividadePessoaList() {
		return atividadePessoaList;
	}

	public List<AtividadeRestricao> getAtividadeRestricaoList() {
		return atividadeRestricaoList;
	}

	public String getCodigo() {
		return codigo;
	}

	public Calendar getConclusao() {
		return conclusao;
	}

	public Confirmacao getDiaTodo() {
		return diaTodo;
	}

	public Integer getDuracaoPrevista() {
		return duracaoPrevista;
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

	public Calendar getInicio() {
		return inicio;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public Metodo getMetodo() {
		return metodo;
	}

	public AtividadeNatureza getNatureza() {
		return natureza;
	}

	public List<Ocorrencia> getOcorrenciaList() {
		return ocorrenciaList;
	}

	public Integer getPercentualConclusao() {
		return percentualConclusao;
	}

	public Calendar getPrevisaoConclusao() {
		return previsaoConclusao;
	}

	public List<AtividadeRelacionamento> getPrincipalAtividadeList() {
		return principalAtividadeList;
	}

	public AtividadePrioridade getPrioridade() {
		return prioridade;
	}

	public Integer getPublicoEstimado() {
		return publicoEstimado;
	}

	public Calendar getRegistro() {
		return registro;
	}

	public AtividadeSituacao getSituacao() {
		return situacao;
	}

	public List<AtividadeRelacionamento> getSubatividadeList() {
		return subatividadeList;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public BigDecimal getValorEstimado() {
		return valorEstimado;
	}

	public BigDecimal getValorReal() {
		return valorReal;
	}

	public void setAgendamentoContagem(Integer agendamentoContagem) {
		this.agendamentoContagem = agendamentoContagem;
	}

	public void setAgendamentoDiaMes(DiaMes agendamentoDiaMes) {
		this.agendamentoDiaMes = agendamentoDiaMes;
	}

	public void setAgendamentoDiaSemana(DiaSemana agendamentoDiaSemana) {
		this.agendamentoDiaSemana = agendamentoDiaSemana;
	}

	public void setAgendamentoMes(Mes agendamentoMes) {
		this.agendamentoMes = agendamentoMes;
	}

	public void setAgendamentoTermino(Calendar agendamentoTermino) {
		this.agendamentoTermino = agendamentoTermino;
	}

	public void setAgendamentoTipo(AgendamentoTipo agendamentoTipo) {
		this.agendamentoTipo = agendamentoTipo;
	}

	public void setAgendamentoTipoRepeticao(AgendamentoTipoRepeticao agendamentoTipoRepeticao) {
		this.agendamentoTipoRepeticao = agendamentoTipoRepeticao;
	}

	public void setAtividadeArquivoList(List<AtividadeArquivo> atividadeArquivoList) {
		this.atividadeArquivoList = atividadeArquivoList;
	}

	public void setAtividadeAssuntoList(List<AtividadeAssunto> atividadeAssuntoList) {
		this.atividadeAssuntoList = atividadeAssuntoList;
	}

	public void setAtividadePessoaList(List<AtividadePessoa> atividadePessoaList) {
		this.atividadePessoaList = atividadePessoaList;
	}

	public void setAtividadeRestricaoList(List<AtividadeRestricao> atividadeRestricaoList) {
		this.atividadeRestricaoList = atividadeRestricaoList;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setConclusao(Calendar conclusao) {
		this.conclusao = conclusao;
	}

	public void setDiaTodo(Confirmacao diaTodo) {
		this.diaTodo = diaTodo;
	}

	public void setDuracaoPrevista(Integer duracaoPrevista) {
		this.duracaoPrevista = duracaoPrevista;
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

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public void setMetodo(Metodo metodo) {
		this.metodo = metodo;
	}

	public void setNatureza(AtividadeNatureza natureza) {
		this.natureza = natureza;
	}

	public void setOcorrenciaList(List<Ocorrencia> ocorrenciaList) {
		this.ocorrenciaList = ocorrenciaList;
	}

	public void setPercentualConclusao(Integer percentualConclusao) {
		this.percentualConclusao = percentualConclusao;
	}

	public void setPrevisaoConclusao(Calendar previsaoConclusao) {
		this.previsaoConclusao = previsaoConclusao;
	}

	public void setPrincipalAtividadeList(List<AtividadeRelacionamento> principalAtividadeList) {
		this.principalAtividadeList = principalAtividadeList;
	}

	public void setPrioridade(AtividadePrioridade prioridade) {
		this.prioridade = prioridade;
	}

	public void setPublicoEstimado(Integer publicoEstimado) {
		this.publicoEstimado = publicoEstimado;
	}

	public void setRegistro(Calendar registro) {
		this.registro = registro;
	}

	public void setSituacao(AtividadeSituacao situacao) {
		this.situacao = situacao;
	}

	public void setSubatividadeList(List<AtividadeRelacionamento> subatividadeList) {
		this.subatividadeList = subatividadeList;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setValorEstimado(BigDecimal valorEstimado) {
		this.valorEstimado = valorEstimado;
	}

	public void setValorReal(BigDecimal valorReal) {
		this.valorReal = valorReal;
	}

}