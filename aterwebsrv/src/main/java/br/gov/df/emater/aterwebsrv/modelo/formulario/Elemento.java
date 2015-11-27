package br.gov.df.emater.aterwebsrv.modelo.formulario;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ElementoTipo;

@Entity
@Table(schema = EntidadeBase.FORMULARIO_SCHEMA)
public class Elemento extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	private String codigo;

	@Enumerated(EnumType.STRING)
	@Column(name = "esconde_form")
	private Confirmacao escondeForm;

	@Enumerated(EnumType.STRING)
	@Column(name = "esconde_lista")
	private Confirmacao escondeLista;

	@Lob
	@Column(name = "funcao_editar_antes")
	private String funcaoEditarAntes;

	@Lob
	@Column(name = "funcao_excluir_antes")
	private String funcaoExcluirAntes;

	@Lob
	@Column(name = "funcao_excluir_depois")
	private String funcaoExcluirDepois;

	@Lob
	@Column(name = "funcao_exibir")
	private String funcaoExibir;

	@Lob
	@Column(name = "funcao_incluir_antes")
	private String funcaoIncluirAntes;

	@Lob
	@Column(name = "funcao_requerido")
	private String funcaoRequerido;

	@Lob
	@Column(name = "funcao_salvar_depois")
	private String funcaoSalvarDepois;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	@OneToMany(mappedBy = "elemento")
	private List<Observar> observarList;

	@Lob
	private String opcao;

	@Transient
	private Object opcaoTemp;

	@Enumerated(EnumType.STRING)
	@Column(name = "somente_leitura")
	private Confirmacao somenteLeitura;

	private Integer tamanho;

	@Enumerated(EnumType.STRING)
	private ElementoTipo tipo;

	public String getCodigo() {
		return codigo;
	}

	public Confirmacao getEscondeForm() {
		return escondeForm;
	}

	public Confirmacao getEscondeLista() {
		return escondeLista;
	}

	public String getFuncaoEditarAntes() {
		return funcaoEditarAntes;
	}

	public String getFuncaoExcluirAntes() {
		return funcaoExcluirAntes;
	}

	public String getFuncaoExcluirDepois() {
		return funcaoExcluirDepois;
	}

	public String getFuncaoExibir() {
		return funcaoExibir;
	}

	public String getFuncaoIncluirAntes() {
		return funcaoIncluirAntes;
	}

	public String getFuncaoRequerido() {
		return funcaoRequerido;
	}

	public String getFuncaoSalvarDepois() {
		return funcaoSalvarDepois;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public List<Observar> getObservarList() {
		return observarList;
	}

	public String getOpcao() {
		return opcao;
	}

	public Object getOpcaoTemp() {
		return opcaoTemp;
	}

	public Confirmacao getSomenteLeitura() {
		return somenteLeitura;
	}

	public Integer getTamanho() {
		return tamanho;
	}

	public ElementoTipo getTipo() {
		return tipo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setEscondeForm(Confirmacao escondeForm) {
		this.escondeForm = escondeForm;
	}

	public void setEscondeLista(Confirmacao escondeLista) {
		this.escondeLista = escondeLista;
	}

	public void setFuncaoEditarAntes(String funcaoEditarAntes) {
		this.funcaoEditarAntes = funcaoEditarAntes;
	}

	public void setFuncaoExcluirAntes(String funcaoExcluirAntes) {
		this.funcaoExcluirAntes = funcaoExcluirAntes;
	}

	public void setFuncaoExcluirDepois(String funcaoExcluirDepois) {
		this.funcaoExcluirDepois = funcaoExcluirDepois;
	}

	public void setFuncaoExibir(String funcaoExibir) {
		this.funcaoExibir = funcaoExibir;
	}

	public void setFuncaoIncluirAntes(String funcaoIncluirAntes) {
		this.funcaoIncluirAntes = funcaoIncluirAntes;
	}

	public void setFuncaoRequerido(String funcaoRequerido) {
		this.funcaoRequerido = funcaoRequerido;
	}

	public void setFuncaoSalvarDepois(String funcaoSalvarDepois) {
		this.funcaoSalvarDepois = funcaoSalvarDepois;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setObservarList(List<Observar> observarList) {
		this.observarList = observarList;
	}

	public void setOpcao(String opcao) {
		this.opcao = opcao;
	}

	public void setOpcaoTemp(Object opcaoTemp) {
		this.opcaoTemp = opcaoTemp;
	}

	public void setSomenteLeitura(Confirmacao somenteLeitura) {
		this.somenteLeitura = somenteLeitura;
	}

	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}

	public void setTipo(ElementoTipo tipo) {
		this.tipo = tipo;
	}

}
