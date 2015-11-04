package br.gov.df.emater.aterwebsrv.modelo.ater;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEndereco;
import br.gov.df.emater.aterwebsrv.rest.json.JsonFormatarBigDecimal;

/**
 * The persistent class for the exploracao database table.
 * 
 */
@Entity
@Table(name = "exploracao", schema = EntidadeBase.ATER_SCHEMA)
public class Exploracao extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal area;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "pessoa_endereco_id")
	private PessoaEndereco pessoaEndereco;

	private String regime;

	public Exploracao() {
	}

	public Exploracao(Integer id) {
		super(id);
	}

	public BigDecimal getArea() {
		return area;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public PessoaEndereco getPessoaEndereco() {
		return pessoaEndereco;
	}

	public String getRegime() {
		return regime;
	}

	public void setArea(BigDecimal area) {
		this.area = area;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
		this.pessoaEndereco = pessoaEndereco;
	}

	public void setRegime(String regime) {
		this.regime = regime;
	}
}