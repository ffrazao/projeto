package br.gov.df.emater.aterwebsrv.modelo.atividade;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaList;
import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

import java.util.ArrayList;
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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo._LogInclusaoAlteracao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeFinalidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeFormato;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeNatureza;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadePrioridade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeSituacao;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(name = "atividade", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class Atividade extends EntidadeBase
		implements _ChavePrimaria<Integer>, _LogInclusaoAlteracao, InfoBasica<Atividade> {

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
	private List<AtividadeAssunto> assuntoList;

	@OneToMany(mappedBy = "atividade")
	private List<AtividadeCadeiaProdutiva> cadeiaProdutivaList;

	@OneToMany(mappedBy = "atividade", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	private List<AtividadeChaveSisater> chaveSisaterList = new ArrayList<AtividadeChaveSisater>();

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

	@Column(name = "duracao_suspensao")
	private Integer duracaoSuspensao;

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

	@OneToMany(mappedBy = "atividade")
	private List<AtividadeMetaTatica> metaTaticaList;

	@ManyToOne
	@JoinColumn(name = "metodo_id")
	private Metodo metodo;

	@Enumerated(EnumType.STRING)
	private AtividadeNatureza natureza;

	@OneToMany(mappedBy = "atividade")
	private List<Ocorrencia> ocorrenciaList;

	@Column(name = "percentual_conclusao")
	private Integer percentualConclusao;

	@OneToMany(mappedBy = "atividade")
	@Where(clause = "participacao = 'D'")
	private List<AtividadePessoa> pessoaDemandanteList;

	@OneToMany(mappedBy = "atividade")
	@Where(clause = "participacao = 'E'")
	private List<AtividadePessoa> pessoaExecutorList;

	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	@Column(name = "previsao_conclusao")
	private Calendar previsaoConclusao;

	@Enumerated(EnumType.STRING)
	private AtividadePrioridade prioridade;

	@Transient
	private ProjetoCreditoRural projetoCreditoRural;

	@Column(name = "publico_estimado")
	private Integer publicoEstimado;

	@Column(name = "publico_real")
	private Integer publicoReal;

	@Enumerated(EnumType.STRING)
	private AtividadeSituacao situacao;

	@Column(name = "situacao_data", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar situacaoData;

	@Lob
	@Column(name = "situacao_motivo")
	private String situacaoMotivo;

	
	public Atividade() {
		super();
	}

	public Atividade(Integer id) {
		super(id);
	}

	public Atividade(Integer id, Calendar alteracaoData, Usuario alteracaoUsuario, List<AtividadeAssunto> assuntoList,
			List<AtividadeCadeiaProdutiva> cadeiaProdutivaList, List<AtividadeChaveSisater> chaveSisaterList,
			String codigo, Calendar conclusao, String detalhamento, Integer duracaoEstimada, Integer duracaoReal,
			Integer duracaoSuspensao, AtividadeFinalidade finalidade, AtividadeFormato formato, Calendar inclusaoData,
			Usuario inclusaoUsuario, Calendar inicio, List<AtividadeMetaTatica> metaTaticaList, Metodo metodo,
			AtividadeNatureza natureza, List<Ocorrencia> ocorrenciaList, Integer percentualConclusao,
			List<AtividadePessoa> pessoaDemandanteList, List<AtividadePessoa> pessoaExecutorList,
			Calendar previsaoConclusao, AtividadePrioridade prioridade, ProjetoCreditoRural projetoCreditoRural,
			Integer publicoEstimado, Integer publicoReal, AtividadeSituacao situacao, Calendar situacaoData,
			String situacaoMotivo) {
		super();
		this.id = id;
		this.alteracaoData = alteracaoData;
		this.alteracaoUsuario = alteracaoUsuario;
		this.assuntoList = assuntoList;
		this.cadeiaProdutivaList = cadeiaProdutivaList;
		this.chaveSisaterList = chaveSisaterList;
		this.codigo = codigo;
		this.conclusao = conclusao;
		this.detalhamento = detalhamento;
		this.duracaoEstimada = duracaoEstimada;
		this.duracaoReal = duracaoReal;
		this.duracaoSuspensao = duracaoSuspensao;
		this.finalidade = finalidade;
		this.formato = formato;
		this.inclusaoData = inclusaoData;
		this.inclusaoUsuario = inclusaoUsuario;
		this.inicio = inicio;
		this.metaTaticaList = metaTaticaList;
		this.metodo = metodo;
		this.natureza = natureza;
		this.ocorrenciaList = ocorrenciaList;
		this.percentualConclusao = percentualConclusao;
		this.pessoaDemandanteList = pessoaDemandanteList;
		this.pessoaExecutorList = pessoaExecutorList;
		this.previsaoConclusao = previsaoConclusao;
		this.prioridade = prioridade;
		this.projetoCreditoRural = projetoCreditoRural;
		this.publicoEstimado = publicoEstimado;
		this.publicoReal = publicoReal;
		this.situacao = situacao;
		this.situacaoData = situacaoData;
		this.situacaoMotivo = situacaoMotivo;
	}

	@Override
	public Calendar getAlteracaoData() {
		return alteracaoData;
	}

	@Override
	public Usuario getAlteracaoUsuario() {
		return alteracaoUsuario;
	}

	public List<AtividadeAssunto> getAssuntoList() {
		return assuntoList;
	}

	public List<AtividadeCadeiaProdutiva> getCadeiaProdutivaList() {
		return cadeiaProdutivaList;
	}

	public List<AtividadeChaveSisater> getChaveSisaterList() {
		return chaveSisaterList;
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

	public Integer getDuracaoSuspensao() {
		return duracaoSuspensao;
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

	@Override
	public Calendar getInclusaoData() {
		return inclusaoData;
	}

	@Override
	public Usuario getInclusaoUsuario() {
		return inclusaoUsuario;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public List<AtividadeMetaTatica> getMetaTaticaList() {
		return metaTaticaList;
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

	public List<AtividadePessoa> getPessoaDemandanteList() {
		return pessoaDemandanteList;
	}

	public List<AtividadePessoa> getPessoaExecutorList() {
		return pessoaExecutorList;
	}

	public Calendar getPrevisaoConclusao() {
		return previsaoConclusao;
	}

	public AtividadePrioridade getPrioridade() {
		return prioridade;
	}

	public ProjetoCreditoRural getProjetoCreditoRural() {
		return projetoCreditoRural;
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

	public Calendar getSituacaoData() {
		return situacaoData;
	}

	public String getSituacaoMotivo() {
		return situacaoMotivo;
	}

	@Override
	public Atividade infoBasica() {
		return new Atividade(this.id, this.alteracaoData, infoBasicaReg(this.alteracaoUsuario),
				infoBasicaList(this.assuntoList), infoBasicaList(this.cadeiaProdutivaList),
				infoBasicaList(this.chaveSisaterList), this.codigo, this.conclusao, this.detalhamento,
				this.duracaoEstimada, this.duracaoReal, this.duracaoSuspensao, this.finalidade, this.formato,
				this.inclusaoData, infoBasicaReg(this.inclusaoUsuario), this.inicio,
				infoBasicaList(this.metaTaticaList), infoBasicaReg(this.metodo), this.natureza,
				infoBasicaList(this.ocorrenciaList), this.percentualConclusao,
				infoBasicaList(this.pessoaDemandanteList), infoBasicaList(this.pessoaExecutorList),
				this.previsaoConclusao, this.prioridade, infoBasicaReg(this.projetoCreditoRural), this.publicoEstimado,
				this.publicoReal, this.situacao, this.situacaoData, this.situacaoMotivo);
	}

	@Override
	public void setAlteracaoData(Calendar alteracaoData) {
		this.alteracaoData = alteracaoData;
	}

	@Override
	public void setAlteracaoUsuario(Usuario alteracaoUsuario) {
		this.alteracaoUsuario = alteracaoUsuario;
	}

	public void setAssuntoList(List<AtividadeAssunto> assuntoList) {
		this.assuntoList = assuntoList;
	}

	public void setCadeiaProdutivaList(List<AtividadeCadeiaProdutiva> cadeiaProdutivaList) {
		this.cadeiaProdutivaList = cadeiaProdutivaList;
	}

	public void setChaveSisaterList(List<AtividadeChaveSisater> chaveSisaterList) {
		this.chaveSisaterList = chaveSisaterList;
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

	public void setDuracaoSuspensao(Integer duracaoSuspensao) {
		this.duracaoSuspensao = duracaoSuspensao;
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

	@Override
	public void setInclusaoData(Calendar inclusaoData) {
		this.inclusaoData = inclusaoData;
	}

	@Override
	public void setInclusaoUsuario(Usuario inclusaoUsuario) {
		this.inclusaoUsuario = inclusaoUsuario;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public void setMetaTaticaList(List<AtividadeMetaTatica> metaTaticaList) {
		this.metaTaticaList = metaTaticaList;
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

	public void setPessoaDemandanteList(List<AtividadePessoa> pessoaDemandanteList) {
		this.pessoaDemandanteList = pessoaDemandanteList;
	}

	public void setPessoaExecutorList(List<AtividadePessoa> pessoaExecutorList) {
		this.pessoaExecutorList = pessoaExecutorList;
	}

	public void setPrevisaoConclusao(Calendar previsaoConclusao) {
		this.previsaoConclusao = previsaoConclusao;
	}

	public void setPrioridade(AtividadePrioridade prioridade) {
		this.prioridade = prioridade;
	}

	public void setProjetoCreditoRural(ProjetoCreditoRural projetoCreditoRural) {
		this.projetoCreditoRural = projetoCreditoRural;
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

	public void setSituacaoData(Calendar situacaoData) {
		this.situacaoData = situacaoData;
	}

	public void setSituacaoMotivo(String situacaoMotivo) {
		this.situacaoMotivo = situacaoMotivo;
	}

}