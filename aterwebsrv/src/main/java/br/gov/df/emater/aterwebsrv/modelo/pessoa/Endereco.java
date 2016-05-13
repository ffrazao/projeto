package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Point;

import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerGeometry;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerGeometry;

/**
 * The persistent class for the endereco database table.
 * 
 */
@Entity
@Table(name = "endereco", schema = EntidadeBase.PESSOA_SCHEMA)
// @Indexed
public class Endereco extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

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

	@OneToMany(mappedBy = "endereco", cascade = CascadeType.ALL)
	private List<Area> areaList = new ArrayList<Area>();

	// @Max(value = 250, message = "Muito extenso")
	private String bairro;

	// @Field(index = Index.YES, store = Store.YES)
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

	@Column(name = "endereco_atualizado")
	@Enumerated(EnumType.STRING)
	private Confirmacao enderecoAtualizado;

	@Column(name = "endereco_sisater")
	// @Max(value = 500, message = "Muito extenso")
	private String enderecoSisater;

	@Column(name = "entrada_principal")
	@Type(type = "org.hibernate.spatial.GeometryType")
	@JsonDeserialize(using = JsonDeserializerGeometry.class)
	@JsonSerialize(using = JsonSerializerGeometry.class)
	private Point entradaPrincipal;

	@ManyToOne
	@JoinColumn(name = "estado_id")
	private Estado estado;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	// @NotBlank
	// @Field(index = Index.YES, store = Store.YES)
	// @Max(value = 250, message = "Muito extenso")
	private String logradouro;

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

	public Endereco(Serializable id) {
		super(id);
	}

	public Endereco(Serializable id, String logradouro, String cep) {
		super(id);
		this.setLogradouro(logradouro);
		this.setCep(cep);
		this.setEnderecoSisater(String.format("%s%s", logradouro, cep == null ? null : String.format("\n%s", cep)));
	}

	public Endereco(String bairro, String cep, Cidade cidade, String codigoIbge, String complemento, String enderecoSisater, Estado estado, Integer id, Point entradaPrincipal, String logradouro, Municipio municipio, String nomePropriedadeRuralOuEstabelecimento, String numero,
			Pais pais, Confirmacao propriedadeRuralConfirmacao, String roteiroAcessoOuEnderecoInternacional, List<Area> areaList) {
		super();
		this.setBairro(bairro);
		this.setCep(cep);
		this.setCidade(cidade);
		this.setCodigoIbge(codigoIbge);
		this.setComplemento(complemento);
		this.setEnderecoSisater(enderecoSisater);
		this.setEstado(estado);
		this.setId(id);
		this.setEntradaPrincipal(entradaPrincipal);
		this.setLogradouro(logradouro);
		this.setMunicipio(municipio);
		this.setNomePropriedadeRuralOuEstabelecimento(nomePropriedadeRuralOuEstabelecimento);
		this.setNumero(numero);
		this.setPais(pais);
		this.setPropriedadeRuralConfirmacao(propriedadeRuralConfirmacao);
		this.setRoteiroAcessoOuEnderecoInternacional(roteiroAcessoOuEnderecoInternacional);
		this.setAreaList(areaList);
		if (this.getAreaList() != null) {
			for (Area area : this.getAreaList()) {
				area.setEndereco(null);
			}
		}
	}

	public List<Area> getAreaList() {
		return areaList;
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

	public Confirmacao getEnderecoAtualizado() {
		return enderecoAtualizado;
	}

	public String getEnderecoSisater() {
		return enderecoSisater;
	}

	public Point getEntradaPrincipal() {
		return entradaPrincipal;
	}

	public Estado getEstado() {
		return estado;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getLogradouro() {
		return logradouro;
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
		return new Endereco(this.bairro, this.cep, this.cidade == null ? null : this.cidade.infoBasica(), this.codigoIbge, this.complemento, this.enderecoSisater, this.estado == null ? null : this.estado.infoBasica(), this.id, this.entradaPrincipal, this.logradouro,
				this.municipio == null ? null : this.municipio.infoBasica(), this.nomePropriedadeRuralOuEstabelecimento, this.numero, this.pais == null ? null : this.pais.infoBasica(), this.propriedadeRuralConfirmacao, this.roteiroAcessoOuEnderecoInternacional, this.areaList);
	}

	public void setAreaList(List<Area> areaList) {
		this.areaList = areaList;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public void setCep(String cep) {
		this.cep = UtilitarioString.formataCep(cep);
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

	public void setEnderecoAtualizado(Confirmacao enderecoAtualizado) {
		this.enderecoAtualizado = enderecoAtualizado;
	}

	public void setEnderecoSisater(String enderecoSisater) {
		this.enderecoSisater = enderecoSisater;
	}

	public void setEntradaPrincipal(Point entradaPrincipal) {
		this.entradaPrincipal = entradaPrincipal;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
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

}