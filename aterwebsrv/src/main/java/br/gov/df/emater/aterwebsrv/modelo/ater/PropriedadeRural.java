package br.gov.df.emater.aterwebsrv.modelo.ater;

import java.math.BigDecimal;
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
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PropriedadeRuralSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.SituacaoFundiaria;
import br.gov.df.emater.aterwebsrv.modelo.pendencia.Pendenciavel;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonFormatarBigDecimal;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

/**
 * The persistent class for the propriedade_rural database table.
 * 
 */
@Entity
@Table(name = "propriedade_rural", schema = EntidadeBase.ATER_SCHEMA)
// @Indexed
public class PropriedadeRural extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<PropriedadeRural>, Pendenciavel<PropriedadeRuralPendencia> {

	private static final long serialVersionUID = 1L;

	@Column(name = "alteracao_data")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar alteracaoData;

	@Column(name = "area_total")
	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal areaTotal;

	@OneToMany(mappedBy = "propriedadeRural")
	// @IndexedEmbedded
	private List<PropriedadeRuralArquivo> arquivoList;

	// @NotNull
	@ManyToOne
	@JoinColumn(name = "bacia_hidrografica_id")
	private BaciaHidrografica baciaHidrografica;

	// @NumberFormat(style = Style.CURRENCY)
	// @JsonDeserialize(using = JsonFormatarBigDecimal.class)
	// private BigDecimal benfeitoria;
	//
	// @OneToMany(mappedBy = "propriedadeRural")
	// // @IndexedEmbedded
	// private List<Benfeitoria> benfeitoriaList;

	@Column(name = "cartorio_data_registro")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar cartorioDataRegistro;

	@Column(name = "cartorio_informacao")
	private String cartorioInformacao;

	@Column(name = "chave_sisater")
	private String chaveSisater;

	// @NotNull
	@ManyToOne
	@JoinColumn(name = "comunidade_id")
	private Comunidade comunidade;

	@OneToOne
	@JoinColumn(name = "endereco_id")
	@NotNull
	private Endereco endereco;

	// @IndexedEmbedded
	@OneToMany(mappedBy = "propriedadeRural", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PropriedadeRuralFormaUtilizacaoEspacoRural> formaUtilizacaoEspacoRuralList = new ArrayList<PropriedadeRuralFormaUtilizacaoEspacoRural>();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "inclusao_data")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inclusaoData;

	private String nome;

	@Column(name = "numero_registro")
	// @Field(index = Index.YES, store = Store.YES)
	private String numeroRegistro;

	@Lob
	// @Field(index = Index.YES, store = Store.YES)
	private String observacoes;

	// @Field(index = Index.YES, store = Store.YES)
	@Enumerated(EnumType.STRING)
	private Confirmacao outorga;

	@Column(name = "outorga_numero")
	// @Field(index = Index.YES, store = Store.YES)
	private String outorgaNumero;

	@Column(name = "outorga_validade")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar outorgaValidade;

	@OneToMany(mappedBy = "propriedadeRural")
	private List<PropriedadeRuralPendencia> pendenciaList = new ArrayList<PropriedadeRuralPendencia>();

	@Column(name = "principais_atividades_produtivas")
	private String principaisAtividadesProdutivas;

	@OneToMany(mappedBy = "propriedadeRural")
	private List<PublicoAlvoPropriedadeRural> publicoAlvoPropriedadeRuralList;

	@Lob
	@Column(name = "roteiro_acesso")
	// @Field(index = Index.YES, store = Store.YES)
	private String roteiroAcesso;

	@ManyToOne
	@JoinColumn(name = "sistema_producao_id")
	private SistemaProducao sistemaProducao;

	@Enumerated(EnumType.STRING)
	private PropriedadeRuralSituacao situacao;

	@Column(name = "situacao_data")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar situacaoData;

	@Column(name = "situacao_fundiaria")
	@Enumerated(EnumType.STRING)
	private SituacaoFundiaria situacaoFundiaria;

	@ManyToOne
	@JoinColumn(name = "alteracao_usuario_id")
	private Usuario usuarioAlteracao;

	@ManyToOne
	@JoinColumn(name = "inclusao_usuario_id")
	private Usuario usuarioInclusao;

	public PropriedadeRural() {

	}

	public PropriedadeRural(Integer id) {
		this();
		setId(id);
	}

	public PropriedadeRural(Integer id, String nome, Comunidade comunidade, BigDecimal areaTotal, Endereco endereco) {
		setId(id);
		setNome(nome);
		setComunidade(comunidade);
		setAreaTotal(areaTotal);
		setEndereco(endereco);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PropriedadeRural other = (PropriedadeRural) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Calendar getAlteracaoData() {
		return alteracaoData;
	}

	public BigDecimal getAreaTotal() {
		return areaTotal;
	}

	public List<PropriedadeRuralArquivo> getArquivoList() {
		return arquivoList;
	}

	public BaciaHidrografica getBaciaHidrografica() {
		return baciaHidrografica;
	}

	public Calendar getCartorioDataRegistro() {
		return cartorioDataRegistro;
	}

	public String getCartorioInformacao() {
		return cartorioInformacao;
	}

	public String getChaveSisater() {
		return chaveSisater;
	}

	public Comunidade getComunidade() {
		return comunidade;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public List<PropriedadeRuralFormaUtilizacaoEspacoRural> getFormaUtilizacaoEspacoRuralList() {
		return formaUtilizacaoEspacoRuralList;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Calendar getInclusaoData() {
		return inclusaoData;
	}

	public String getNome() {
		return nome;
	}

	public String getNumeroRegistro() {
		return numeroRegistro;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public Confirmacao getOutorga() {
		return outorga;
	}

	public String getOutorgaNumero() {
		return outorgaNumero;
	}

	public Calendar getOutorgaValidade() {
		return outorgaValidade;
	}

	public List<PropriedadeRuralPendencia> getPendenciaList() {
		return pendenciaList;
	}

	public String getPrincipaisAtividadesProdutivas() {
		return principaisAtividadesProdutivas;
	}

	public List<PublicoAlvoPropriedadeRural> getPublicoAlvoPropriedadeRuralList() {
		return publicoAlvoPropriedadeRuralList;
	}

	public String getRoteiroAcesso() {
		return roteiroAcesso;
	}

	public SistemaProducao getSistemaProducao() {
		return sistemaProducao;
	}

	public PropriedadeRuralSituacao getSituacao() {
		return situacao;
	}

	public Calendar getSituacaoData() {
		return situacaoData;
	}

	public SituacaoFundiaria getSituacaoFundiaria() {
		return situacaoFundiaria;
	}

	public Usuario getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public Usuario getUsuarioInclusao() {
		return usuarioInclusao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public PropriedadeRural infoBasica() {
		return new PropriedadeRural(getId(), getNome(), getComunidade() != null ? getComunidade().infoBasica() : null, getAreaTotal(), getEndereco().infoBasica());
	}

	public void setAlteracaoData(Calendar alteracaoData) {
		this.alteracaoData = alteracaoData;
	}

	public void setAreaTotal(BigDecimal areaTotal) {
		this.areaTotal = areaTotal;
	}

	public void setArquivoList(List<PropriedadeRuralArquivo> arquivoList) {
		this.arquivoList = arquivoList;
	}

	public void setBaciaHidrografica(BaciaHidrografica baciaHidrografica) {
		this.baciaHidrografica = baciaHidrografica;
	}

	public void setCartorioDataRegistro(Calendar cartorioDataRegistro) {
		this.cartorioDataRegistro = cartorioDataRegistro;
	}

	public void setCartorioInformacao(String cartorioInformacao) {
		this.cartorioInformacao = cartorioInformacao;
	}

	public void setChaveSisater(String chaveSisater) {
		this.chaveSisater = chaveSisater;
	}

	public void setComunidade(Comunidade comunidade) {
		this.comunidade = comunidade;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public void setFormaUtilizacaoEspacoRuralList(List<PropriedadeRuralFormaUtilizacaoEspacoRural> formaUtilizacaoEspacoRuralList) {
		this.formaUtilizacaoEspacoRuralList = formaUtilizacaoEspacoRuralList;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setInclusaoData(Calendar inclusaoData) {
		this.inclusaoData = inclusaoData;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setNumeroRegistro(String numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public void setOutorga(Confirmacao outorga) {
		this.outorga = outorga;
	}

	public void setOutorgaNumero(String outorgaNumero) {
		this.outorgaNumero = outorgaNumero;
	}

	public void setOutorgaValidade(Calendar outorgaValidade) {
		this.outorgaValidade = outorgaValidade;
	}

	public void setPendenciaList(List<PropriedadeRuralPendencia> pendenciaList) {
		this.pendenciaList = pendenciaList;
	}

	public void setPrincipaisAtividadesProdutivas(String principaisAtividadesProdutivas) {
		this.principaisAtividadesProdutivas = principaisAtividadesProdutivas;
	}

	public void setPublicoAlvoPropriedadeRuralList(List<PublicoAlvoPropriedadeRural> publicoAlvoPropriedadeRuralList) {
		this.publicoAlvoPropriedadeRuralList = publicoAlvoPropriedadeRuralList;
	}

	public void setRoteiroAcesso(String roteiroAcesso) {
		this.roteiroAcesso = roteiroAcesso;
	}

	public void setSistemaProducao(SistemaProducao sistemaProducao) {
		this.sistemaProducao = sistemaProducao;
	}

	public void setSituacao(PropriedadeRuralSituacao situacao) {
		this.situacao = situacao;
	}

	public void setSituacaoData(Calendar situacaoData) {
		this.situacaoData = situacaoData;
	}

	public void setSituacaoFundiaria(SituacaoFundiaria situacaoFundiaria) {
		this.situacaoFundiaria = situacaoFundiaria;
	}

	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	public void setUsuarioInclusao(Usuario usuarioInclusao) {
		this.usuarioInclusao = usuarioInclusao;
	}

}