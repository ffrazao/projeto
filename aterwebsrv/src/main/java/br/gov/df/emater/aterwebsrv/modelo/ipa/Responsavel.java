package br.gov.df.emater.aterwebsrv.modelo.ipa;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.ater.Exploracao;
import br.gov.df.emater.aterwebsrv.rest.json.JsonFormatarBigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
@Table(name = "responsavel", schema = EntidadeBase.IPA_SCHEMA)
public class Responsavel extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1l;

	@JoinColumn(name = "exploracao_id")
	@ManyToOne
	private Exploracao exploracao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@JoinColumn(name = "producao_id")
	@ManyToOne
	private Producao producao;

	@Column(name = "volume")
	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal volume;

	public Responsavel() {
	}

	public Exploracao getExploracao() {
		return exploracao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Producao getProducao() {
		return producao;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setExploracao(Exploracao exploracao) {
		this.exploracao = exploracao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setProducao(Producao producao) {
		this.producao = producao;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

}