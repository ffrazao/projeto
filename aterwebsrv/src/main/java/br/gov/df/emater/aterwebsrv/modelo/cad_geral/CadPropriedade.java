package br.gov.df.emater.aterwebsrv.modelo.cad_geral;

import java.math.BigDecimal;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.StatusValidoInvalido;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonFormatarBigDecimal;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "cad_propriedade", schema = EntidadeBase.CAD_GERAL_SCHEMA)
public class CadPropriedade extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "area_propriedade")
	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal areaPropriedade;

	@Column(name = "arrendatario")
	@Enumerated(EnumType.STRING)
	private Confirmacao arrendatario;

	@Column(name = "bairro")
	private String bairro;

	@ManyToOne
	@JoinColumn(name = "origem_id")
	private CadOrigem cadOrigem;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "cad_pessoa_propriedade", schema = EntidadeBase.CAD_GERAL_SCHEMA, joinColumns = { @JoinColumn(name = "cad_propriedade_id") }, inverseJoinColumns = { @JoinColumn(name = "cad_pessoa_id") })
	private List<CadPessoa> cadPessoaList;

	@Column(name = "cidade")
	private String cidade;

	@Column(name = "cnpj")
	private String cnpj;

	@Column(name = "endereco")
	private String endereco;

	@Column(name = "geo_referenciamento_s")
	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal geoReferenciamentoS;

	@Column(name = "geo_referenciamento_w")
	@NumberFormat(style = Style.NUMBER)
	@JsonDeserialize(using = JsonFormatarBigDecimal.class)
	private BigDecimal geoReferenciamentoW;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "inicio_atividade")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inicioAtividade;

	@Column(name = "nome")
	private String nome;

	@Column(name = "numero_empregado_fixo")
	private String numeroEmpregadoFixo;

	@Column(name = "razao_social")
	private String razaoSocial;

	@Column(name = "situacao_fundiaria")
	private String situacaoFundiaria;

	@Column(name = "situacao_fundiaria_outro")
	private String situacaoFundiariaOutro;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private StatusValidoInvalido status;

	@Column(name = "telefone_1")
	private String telefone1;

	@Column(name = "telefone_2")
	private String telefone2;

	@Column(name = "tem_app")
	@Enumerated(EnumType.STRING)
	private Confirmacao temApp;

	@Column(name = "tem_plano_utilizacao")
	@Enumerated(EnumType.STRING)
	private Confirmacao temPlanoUtilizacao;

	@Column(name = "tem_reserva_legal")
	@Enumerated(EnumType.STRING)
	private Confirmacao temReservaLegal;

	@Column(name = "uf")
	private String uf;

	public CadPropriedade() {
		super();
	}

	public CadPropriedade(Integer id, String nome) {
		this();
		setId(id);
		setNome(nome);
	}

	public BigDecimal getAreaPropriedade() {
		return areaPropriedade;
	}

	public Confirmacao getArrendatario() {
		return arrendatario;
	}

	public String getBairro() {
		return bairro;
	}

	public CadOrigem getCadOrigem() {
		return cadOrigem;
	}

	public List<CadPessoa> getCadPessoaList() {
		return cadPessoaList;
	}

	public String getCidade() {
		return cidade;
	}

	public String getCnpj() {
		return cnpj;
	}

	public String getEndereco() {
		return endereco;
	}

	public BigDecimal getGeoReferenciamentoS() {
		return geoReferenciamentoS;
	}

	public BigDecimal getGeoReferenciamentoW() {
		return geoReferenciamentoW;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Calendar getInicioAtividade() {
		return inicioAtividade;
	}

	public String getNome() {
		return nome;
	}

	public String getNumeroEmpregadoFixo() {
		return numeroEmpregadoFixo;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public String getSituacaoFundiaria() {
		return situacaoFundiaria;
	}

	public String getSituacaoFundiariaOutro() {
		return situacaoFundiariaOutro;
	}

	public StatusValidoInvalido getStatus() {
		return status;
	}

	public String getTelefone1() {
		return telefone1;
	}

	public String getTelefone2() {
		return telefone2;
	}

	public Confirmacao getTemApp() {
		return temApp;
	}

	public Confirmacao getTemPlanoUtilizacao() {
		return temPlanoUtilizacao;
	}

	public Confirmacao getTemReservaLegal() {
		return temReservaLegal;
	}

	public String getUf() {
		return uf;
	}

	public void setAreaPropriedade(BigDecimal areaPropriedade) {
		this.areaPropriedade = areaPropriedade;
	}

	public void setArrendatario(Confirmacao arrendatario) {
		this.arrendatario = arrendatario;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public void setCadOrigem(CadOrigem cadOrigem) {
		this.cadOrigem = cadOrigem;
	}

	public void setCadPessoaList(List<CadPessoa> cadPessoaList) {
		this.cadPessoaList = cadPessoaList;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public void setGeoReferenciamentoS(BigDecimal geoReferenciamentoS) {
		this.geoReferenciamentoS = geoReferenciamentoS;
	}

	public void setGeoReferenciamentoW(BigDecimal geoReferenciamentoW) {
		this.geoReferenciamentoW = geoReferenciamentoW;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setInicioAtividade(Calendar inicioAtividade) {
		this.inicioAtividade = inicioAtividade;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setNumeroEmpregadoFixo(String numeroEmpregadoFixo) {
		this.numeroEmpregadoFixo = numeroEmpregadoFixo;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public void setSituacaoFundiaria(String situacaoFundiaria) {
		this.situacaoFundiaria = situacaoFundiaria;
	}

	public void setSituacaoFundiariaOutro(String situacaoFundiariaOutro) {
		this.situacaoFundiariaOutro = situacaoFundiariaOutro;
	}

	public void setStatus(StatusValidoInvalido status) {
		this.status = status;
	}

	public void setTelefone1(String telefone1) {
		this.telefone1 = telefone1;
	}

	public void setTelefone2(String telefone2) {
		this.telefone2 = telefone2;
	}

	public void setTemApp(Confirmacao temApp) {
		this.temApp = temApp;
	}

	public void setTemPlanoUtilizacao(Confirmacao temPlanoUtilizacao) {
		this.temPlanoUtilizacao = temPlanoUtilizacao;
	}

	public void setTemReservaLegal(Confirmacao temReservaLegal) {
		this.temReservaLegal = temReservaLegal;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

}