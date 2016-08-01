package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FluxoCaixaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FluxoCaixaTipo;

@Entity
@Table(name = "projeto_credito_fluxo_caixa", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class ProjetoCreditoRuralFluxoCaixa extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	private Integer ano;

	@Enumerated(EnumType.STRING)
	private FluxoCaixaCodigo codigo;

	private String descricao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private Integer ordem;

	@ManyToOne
	@JoinColumn(name = "projeto_credito_id")
	private ProjetoCreditoRural projetoCreditoRural;

	@Enumerated(EnumType.STRING)
	private FluxoCaixaTipo tipo;

	public ProjetoCreditoRuralFluxoCaixa() {
		super();
	}

	public ProjetoCreditoRuralFluxoCaixa(Integer id) {
		super(id);
	}

	public ProjetoCreditoRuralFluxoCaixa(Integer id, FluxoCaixaTipo tipo) {
		this(id);
		setTipo(tipo);
	}

	public Integer getAno() {
		return ano;
	}

	public FluxoCaixaCodigo getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public ProjetoCreditoRural getProjetoCreditoRural() {
		return projetoCreditoRural;
	}

	public FluxoCaixaTipo getTipo() {
		return tipo;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public void setCodigo(FluxoCaixaCodigo codigo) {
		this.codigo = codigo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public void setProjetoCreditoRural(ProjetoCreditoRural projetoCreditoRural) {
		this.projetoCreditoRural = projetoCreditoRural;
	}

	public void setTipo(FluxoCaixaTipo tipo) {
		this.tipo = tipo;
	}

}