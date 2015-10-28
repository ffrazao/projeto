package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.MeioContatoTipo;
import br.gov.df.emater.aterwebsrv.rest.json.JsonFormatarBigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * The persistent class for the meio_contato_endereco database table.
 * 
 */
@Entity
@Table(name = "meio_contato_endereco", schema = EntidadeBase.PESSOA_SCHEMA)
@Indexed
public class MeioContatoEndereco extends MeioContato {

	private static final long serialVersionUID = 1L;

	@Max(value = 250, message = "Muito extenso")
	private String bairro;

	@Field(index = Index.YES, store = Store.YES)
	@Max(value = 10, message = "Muito extenso")
	private String cep;

	@Column(name = "codigo_ibge")
	@Max(value = 10, message = "Muito extenso")
	private String codigoIbge;

	@Max(value = 250, message = "Muito extenso")
	private String complemento;

	@Column(name = "latitude")
	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal latitude;

	@NotBlank
	@Field(index = Index.YES, store = Store.YES)
	@Max(value = 250, message = "Muito extenso")
	private String logradouro;

	@Column(name = "longitude")
	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal longitude;

	@Column(name = "nome_prop_ou_estab")
	@Max(value = 250, message = "Muito extenso")
	private String nomePropriedadeRuralOuEstabelecimento;

	@Max(value = 50, message = "Muito extenso")
	private String numero;

	@ManyToOne
	@JoinColumn(name = "cidade_id")
	private Cidade cidade;

	@ManyToOne
	@JoinColumn(name = "municipio_id")
	private Municipio municipio;
	
	@ManyToOne
	@JoinColumn(name = "estado_id")
	private Estado estado;
	
	@ManyToOne
	@JoinColumn(name = "pais_id")
	private Pais pais;

	@OneToOne(mappedBy = "meioContatoEndereco", fetch = FetchType.LAZY)
	@Transient
	private PropriedadeRural propriedadeRural;

	@Column(name = "propriedade_rural_confirmacao")
	@Enumerated(EnumType.STRING)
	private Confirmacao propriedadeRuralConfirmacao;

	@Column(name = "roteiro_aces_ou_end_inter")
	@Max(value = 500, message = "Muito extenso")
	private String roteiroAcessoOuEnderecoInternacional;

	public MeioContatoEndereco() {
		setMeioContatoTipo(MeioContatoTipo.END);
	}

	public String getBairro() {
		return bairro;
	}

	public String getCep() {
		return cep;
	}

	public String getCodigoIbge() {
		return codigoIbge;
	}

	public String getComplemento() {
		return complemento;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public String getNomePropriedadeRuralOuEstabelecimento() {
		return nomePropriedadeRuralOuEstabelecimento;
	}

	public String getNumero() {
		return numero;
	}

	public PropriedadeRural getPropriedadeRural() {
		return propriedadeRural;
	}

	public Confirmacao getPropriedadeRuralConfirmacao() {
		return propriedadeRuralConfirmacao;
	}

	public String getRoteiroAcessoOuEnderecoInternacional() {
		return roteiroAcessoOuEnderecoInternacional;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public void setCodigoIbge(String codigoIbge) {
		this.codigoIbge = codigoIbge;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public void setNomePropriedadeRuralOuEstabelecimento(String nomePropriedadeRuralOuEstabelecimento) {
		this.nomePropriedadeRuralOuEstabelecimento = nomePropriedadeRuralOuEstabelecimento;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setPropriedadeRural(PropriedadeRural propriedadeRural) {
		this.propriedadeRural = propriedadeRural;
	}

	public void setPropriedadeRuralConfirmacao(Confirmacao propriedadeRuralConfirmacao) {
		this.propriedadeRuralConfirmacao = propriedadeRuralConfirmacao;
	}

	public void setRoteiroAcessoOuEnderecoInternacional(String roteiroAcessoOuEnderecoInternacional) {
		this.roteiroAcessoOuEnderecoInternacional = roteiroAcessoOuEnderecoInternacional;
	}

}