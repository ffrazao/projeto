package br.gov.df.emater.aterwebsrv.modelo.ater;

import java.util.ArrayList;
import java.util.Calendar;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ConfirmacaoDap;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoCategoria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoSegmento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

/**
 * The persistent class for the publico_alvo database table.
 * 
 */
@Entity
@Table(name = "publico_alvo", schema = EntidadeBase.ATER_SCHEMA)
// @Indexed
public class PublicoAlvo extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<PublicoAlvo> {

	private static final long serialVersionUID = 1L;

	@Column(name = "carteira_produtor_emissao")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar carteiraProdutorEmissao;

	@Column(name = "carteira_produtor_expiracao")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar carteiraProdutorExpiracao;

	@Column(name = "carteira_produtor_numero")
	// @Field(index = Index.YES, store = Store.YES)
	private String carteiraProdutorNumero;

	@Enumerated(EnumType.STRING)
	private PublicoAlvoCategoria categoria;

	@Column(name = "dap_numero")
	// @Field(index = Index.YES, store = Store.YES)
	private String dapNumero;

	@Column(name = "dap_observacao")
	@Lob
	private String dapObservacao;

	@Column(name = "dap_situacao")
	@Enumerated(EnumType.STRING)
	private ConfirmacaoDap dapSituacao;

	@Column(name = "dap_validade")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar dapValidade;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "organizacao_tipo_id")
	private OrganizacaoTipo organizacaoTipo;

	@OneToOne
	@JoinColumn(name = "pessoa_id")
	private Pessoa pessoa;

	@OneToMany(mappedBy = "publicoAlvo")
	private List<PublicoAlvoPropriedadeRural> publicoAlvoPropriedadeRuralList;

	// @IndexedEmbedded
	@OneToMany(mappedBy = "publicoAlvo", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PublicoAlvoSetor> publicoAlvoSetorList = new ArrayList<PublicoAlvoSetor>();

	@Enumerated(EnumType.STRING)
	private PublicoAlvoSegmento segmento;

	private Integer tradicao;

	public PublicoAlvo() {
		// este campo não pode ser nulo, pois possui replicação do tipo cascade
		this.setPublicoAlvoSetorList(new ArrayList<PublicoAlvoSetor>());
	}

	public PublicoAlvo(Integer id) {
		super(id);
	}

	public PublicoAlvo(Integer id, Pessoa pessoa) {
		this(id);
		setPessoa(pessoa.infoBasica());
	}

	public Calendar getCarteiraProdutorEmissao() {
		return carteiraProdutorEmissao;
	}

	public Calendar getCarteiraProdutorExpiracao() {
		return carteiraProdutorExpiracao;
	}

	public String getCarteiraProdutorNumero() {
		return carteiraProdutorNumero;
	}

	public PublicoAlvoCategoria getCategoria() {
		return categoria;
	}

	public String getDapNumero() {
		return dapNumero;
	}

	public String getDapObservacao() {
		return dapObservacao;
	}

	public ConfirmacaoDap getDapSituacao() {
		return dapSituacao;
	}

	public Calendar getDapValidade() {
		return dapValidade;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public OrganizacaoTipo getOrganizacaoTipo() {
		return organizacaoTipo;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public List<PublicoAlvoPropriedadeRural> getPublicoAlvoPropriedadeRuralList() {
		return publicoAlvoPropriedadeRuralList;
	}

	public List<PublicoAlvoSetor> getPublicoAlvoSetorList() {
		return publicoAlvoSetorList;
	}

	public PublicoAlvoSegmento getSegmento() {
		return segmento;
	}

	public Integer getTradicao() {
		return tradicao;
	}

	@Override
	public PublicoAlvo infoBasica() {
		return new PublicoAlvo(getId(), getPessoa().infoBasica());
	}

	public void setCarteiraProdutorEmissao(Calendar carteiraProdutorEmissao) {
		this.carteiraProdutorEmissao = carteiraProdutorEmissao;
	}

	public void setCarteiraProdutorExpiracao(Calendar carteiraProdutorExpiracao) {
		this.carteiraProdutorExpiracao = carteiraProdutorExpiracao;
	}

	public void setCarteiraProdutorNumero(String carteiraProdutorNumero) {
		this.carteiraProdutorNumero = carteiraProdutorNumero;
	}

	public void setCategoria(PublicoAlvoCategoria categoria) {
		this.categoria = categoria;
	}

	public void setDapNumero(String dapNumero) {
		this.dapNumero = dapNumero;
	}

	public void setDapObservacao(String dapObservacao) {
		this.dapObservacao = dapObservacao;
	}

	public void setDapSituacao(ConfirmacaoDap dapSituacao) {
		this.dapSituacao = dapSituacao;
	}

	public void setDapValidade(Calendar dapValidade) {
		this.dapValidade = dapValidade;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setOrganizacaoTipo(OrganizacaoTipo organizacaoTipo) {
		this.organizacaoTipo = organizacaoTipo;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public void setPublicoAlvoPropriedadeRuralList(List<PublicoAlvoPropriedadeRural> publicoAlvoPropriedadeRuralList) {
		this.publicoAlvoPropriedadeRuralList = publicoAlvoPropriedadeRuralList;
	}

	public void setPublicoAlvoSetorList(List<PublicoAlvoSetor> publicoAlvoSetorList) {
		this.publicoAlvoSetorList = publicoAlvoSetorList;
	}

	public void setSegmento(PublicoAlvoSegmento segmento) {
		this.segmento = segmento;
	}

	public void setTradicao(Integer tradicao) {
		this.tradicao = tradicao;
	}

}