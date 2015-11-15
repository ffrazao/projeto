package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.math.BigDecimal;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.rest.json.JsonFormatarBigDecimal;

/**
 * The persistent class for the endereco database table.
 * 
 */
@Entity
@Table(name = "endereco", schema = EntidadeBase.PESSOA_SCHEMA)
@Indexed
public class Endereco extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	// @Max(value = 250, message = "Muito extenso")
	private String bairro;

	@Field(index = Index.YES, store = Store.YES)
	// @Max(value = 10, message = "Muito extenso")
	private String cep;

	@ManyToOne
	@JoinColumn(name = "cidade_id")
	private Cidade cidade;

	@Column(name = "codigo_ibge")
	// @Max(value = 10, message = "Muito extenso")
	private String codigoIbge;

	// @Max(value = 250, message = "Muito extenso")
	private String complemento;

	@Column(name = "endereco_sisater")
	// @Max(value = 500, message = "Muito extenso")
	private String enderecoSisater;

	@ManyToOne
	@JoinColumn(name = "estado_id")
	private Estado estado;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "latitude")
	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal latitude;

	@NotBlank
	@Field(index = Index.YES, store = Store.YES)
	// @Max(value = 250, message = "Muito extenso")
	private String logradouro;

	@Column(name = "longitude")
	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal longitude;

	@ManyToOne
	@JoinColumn(name = "municipio_id")
	private Municipio municipio;

	@Column(name = "nome_prop_ou_estab")
	// @Max(value = 250, message = "Muito extenso")
	private String nomePropriedadeRuralOuEstabelecimento;

	// @Max(value = 50, message = "Muito extenso")
	private String numero;

	@ManyToOne
	@JoinColumn(name = "pais_id")
	private Pais pais;

	@Column(name = "propriedade_rural_confirmacao")
	@Enumerated(EnumType.STRING)
	private Confirmacao propriedadeRuralConfirmacao;

	@Column(name = "roteiro_acesso")
	// @Max(value = 500, message = "Muito extenso")
	private String roteiroAcessoOuEnderecoInternacional;

	public Endereco() {
	}

	public Endereco(String bairro, String cep, Cidade cidade, String codigoIbge, String complemento, String enderecoSisater, Estado estado, Integer id, BigDecimal latitude, String logradouro, BigDecimal longitude, Municipio municipio, String nomePropriedadeRuralOuEstabelecimento,
			String numero, Pais pais, Confirmacao propriedadeRuralConfirmacao, String roteiroAcessoOuEnderecoInternacional) {
		super();
		this.bairro = bairro;
		this.cep = cep;
		this.cidade = cidade;
		this.codigoIbge = codigoIbge;
		this.complemento = complemento;
		this.enderecoSisater = enderecoSisater;
		this.estado = estado;
		this.id = id;
		this.latitude = latitude;
		this.logradouro = logradouro;
		this.longitude = longitude;
		this.municipio = municipio;
		this.nomePropriedadeRuralOuEstabelecimento = nomePropriedadeRuralOuEstabelecimento;
		this.numero = numero;
		this.pais = pais;
		this.propriedadeRuralConfirmacao = propriedadeRuralConfirmacao;
		this.roteiroAcessoOuEnderecoInternacional = roteiroAcessoOuEnderecoInternacional;
	}

	public String getBairro() {
		return bairro;
	}

	public String getCep() {
		return cep;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public String getCodigoIbge() {
		return codigoIbge;
	}

	public String getComplemento() {
		return complemento;
	}

	public String getEnderecoSisater() {
		return enderecoSisater;
	}

	public Estado getEstado() {
		return estado;
	}

	@Override
	public Integer getId() {
		return id;
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

	public Municipio getMunicipio() {
		return municipio;
	}

	public String getNomePropriedadeRuralOuEstabelecimento() {
		return nomePropriedadeRuralOuEstabelecimento;
	}

	public String getNumero() {
		return numero;
	}

	public Pais getPais() {
		return pais;
	}

	public Confirmacao getPropriedadeRuralConfirmacao() {
		return propriedadeRuralConfirmacao;
	}

	public String getRoteiroAcessoOuEnderecoInternacional() {
		return roteiroAcessoOuEnderecoInternacional;
	}

	public Endereco infoBasica() {
		return new Endereco(this.bairro, this.cep, this.cidade == null ? null : this.cidade.infoBasica(), this.codigoIbge, this.complemento, this.enderecoSisater, this.estado == null ? null : this.estado.infoBasica(), this.id, this.latitude, this.logradouro, this.longitude,
				this.municipio == null ? null : this.municipio.infoBasica(), this.nomePropriedadeRuralOuEstabelecimento, this.numero, this.pais == null ? null : this.pais.infoBasica(), this.propriedadeRuralConfirmacao, this.roteiroAcessoOuEnderecoInternacional);
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public void setCodigoIbge(String codigoIbge) {
		this.codigoIbge = codigoIbge;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public void setEnderecoSisater(String enderecoSisater) {
		this.enderecoSisater = enderecoSisater;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
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

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public void setNomePropriedadeRuralOuEstabelecimento(String nomePropriedadeRuralOuEstabelecimento) {
		this.nomePropriedadeRuralOuEstabelecimento = nomePropriedadeRuralOuEstabelecimento;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public void setPropriedadeRuralConfirmacao(Confirmacao propriedadeRuralConfirmacao) {
		this.propriedadeRuralConfirmacao = propriedadeRuralConfirmacao;
	}

	public void setRoteiroAcessoOuEnderecoInternacional(String roteiroAcessoOuEnderecoInternacional) {
		this.roteiroAcessoOuEnderecoInternacional = roteiroAcessoOuEnderecoInternacional;
	}

	public static String FORMATA(Endereco endereco) {
		if (endereco == null) {
			return null;
		}
		String[] linha = null;
		linha = new String[3];
		linha[0] = (UtilitarioString.collectionToString(Arrays.asList(endereco.getLogradouro(), endereco.getComplemento(), endereco.getNumero())));
		linha[1] = (endereco.getBairro());
		String cidade = null;
		String municipio = null;
		String estado = null;
		if (endereco.getCidade() != null) {
			cidade = endereco.getCidade().getNome();
			if (endereco.getCidade().getMunicipio() != null) {
				municipio = endereco.getCidade().getMunicipio().getNome();
				if (endereco.getCidade().getMunicipio().getEstado() != null) {
					estado = endereco.getCidade().getMunicipio().getEstado().getSigla();
				}
			}
		}
		linha[2] = (UtilitarioString.collectionToString(Arrays.asList(UtilitarioString.formataCep(endereco.getCep()), cidade, municipio, estado)));
		return (UtilitarioString.collectionToString(Arrays.asList(linha), "\n"));
	}

}