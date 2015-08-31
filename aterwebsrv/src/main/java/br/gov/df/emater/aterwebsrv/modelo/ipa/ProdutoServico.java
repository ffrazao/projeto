package br.gov.df.emater.aterwebsrv.modelo.ipa;

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

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PerspectivaProducao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ProdutoServicoTipo;

/**
 * The persistent class for the produto_servico database table.
 * 
 */
@Entity
@Table(name = "produto_servico", schema = EntidadeBase.IPA_SCHEMA)
public class ProdutoServico extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "pai", cascade = CascadeType.ALL)
	private List<ProdutoServico> filhos;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "nome")
	private String nome;

	@ManyToOne
	@JoinColumn(name = "produto_servico_id")
	private ProdutoServico pai;

	@Column(name = "perspectiva")
	@Enumerated(EnumType.STRING)
	private PerspectivaProducao perspectiva;

	@Column(name = "tipo")
	@Enumerated(EnumType.STRING)
	private ProdutoServicoTipo tipo;

	@ManyToOne
	@JoinColumn(name = "unidade_medida_id")
	private UnidadeMedida unidadeMedida;

	public ProdutoServico() {
	}

	public ProdutoServico(Integer id) {
		setId(id);
	}

	public List<ProdutoServico> getFilhos() {
		return filhos;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public ProdutoServico getPai() {
		return pai;
	}

	public PerspectivaProducao getPerspectiva() {
		return perspectiva;
	}

	public ProdutoServicoTipo getTipo() {
		return tipo;
	}

	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setFilhos(List<ProdutoServico> filhos) {
		this.filhos = filhos;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPai(ProdutoServico pai) {
		this.pai = pai;
	}

	public void setPerspectiva(PerspectivaProducao perspectiva) {
		this.perspectiva = perspectiva;
	}

	public void setTipo(ProdutoServicoTipo tipo) {
		this.tipo = tipo;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

}