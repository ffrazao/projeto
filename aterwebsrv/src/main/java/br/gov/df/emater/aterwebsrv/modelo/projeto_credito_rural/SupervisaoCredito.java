package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Emprego;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(name = "supervisao_credito", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class SupervisaoCredito extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<SupervisaoCredito> {

	private static final long serialVersionUID = 1L;

	@Column(name = "data_prevista")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar dataPrevista;

	@Column(name = "data_realizacao")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar dataRealizacao;

	@ManyToOne()
	@JoinColumn(name = "emprego_id")
	private Emprego emprego;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Lob
	@Column(name = "liberacao_prevista")
	private String liberacaoPrevista;

	@Lob
	@Column(name = "observacao_situacao")
	private String observacaoSituacao;

	private Integer ordem;

	@ManyToOne()
	@JoinColumn(name = "projeto_credito_rural_id")
	private ProjetoCreditoRural projetoCreditoRural;

	@Lob
	private String recomendacao;

	public SupervisaoCredito() {
	}

	public SupervisaoCredito(Integer id) {
		this.id = id;
	}

	public SupervisaoCredito(Integer id, ProjetoCreditoRural projetoCreditoRural, Integer ordem, Calendar dataPrevista,
			Calendar dataRealizacao, String observacaoSituacao, String recomendacao, String liberacaoPrevista,
			Emprego emprego) {
		super();
		this.id = id;
		this.projetoCreditoRural = projetoCreditoRural;
		this.ordem = ordem;
		this.dataPrevista = dataPrevista;
		this.dataRealizacao = dataRealizacao;
		this.observacaoSituacao = observacaoSituacao;
		this.recomendacao = recomendacao;
		this.liberacaoPrevista = liberacaoPrevista;
		this.emprego = emprego;
	}

	public Calendar getDataPrevista() {
		return dataPrevista;
	}

	public Calendar getDataRealizacao() {
		return dataRealizacao;
	}

	public Emprego getEmprego() {
		return emprego;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getLiberacaoPrevista() {
		return liberacaoPrevista;
	}

	public String getObservacaoSituacao() {
		return observacaoSituacao;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public ProjetoCreditoRural getProjetoCreditoRural() {
		return projetoCreditoRural;
	}

	public String getRecomendacao() {
		return recomendacao;
	}

	@Override
	public SupervisaoCredito infoBasica() {
		return new SupervisaoCredito(this.id, new ProjetoCreditoRural(this.projetoCreditoRural.getId()), this.ordem,
				this.dataPrevista, this.dataRealizacao, this.observacaoSituacao, this.recomendacao,
				this.liberacaoPrevista, (Emprego) infoBasicaReg(this.emprego));
	}

	public void setDataPrevista(Calendar dataPrevista) {
		this.dataPrevista = dataPrevista;
	}

	public void setDataRealizacao(Calendar dataRealizacao) {
		this.dataRealizacao = dataRealizacao;
	}

	public void setEmprego(Emprego emprego) {
		this.emprego = emprego;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setLiberacaoPrevista(String liberacaoPrevista) {
		this.liberacaoPrevista = liberacaoPrevista;
	}

	public void setObservacaoSituacao(String observacaoSituacao) {
		this.observacaoSituacao = observacaoSituacao;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public void setProjetoCreditoRural(ProjetoCreditoRural projetoCreditoRural) {
		this.projetoCreditoRural = projetoCreditoRural;
	}

	public void setRecomendacao(String recomendacao) {
		this.recomendacao = recomendacao;
	}

}