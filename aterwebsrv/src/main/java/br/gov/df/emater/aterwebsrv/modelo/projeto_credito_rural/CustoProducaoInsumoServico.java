package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import java.util.List;

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
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.CustoProducaoTipo;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.UnidadeMedida;

@Entity
@Table(name = "custo_producao_insumo_servico", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class CustoProducaoInsumoServico extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<CustoProducaoInsumoServico> {

	private static final long serialVersionUID = 1L;

	private String especificacao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToMany(mappedBy = "custoProducaoInsumoServico")
	private List<CustoProducaoInsumoServicoPreco> precoList;

	@Column(name = "tipo")
	@Enumerated(EnumType.STRING)
	private CustoProducaoTipo tipo;

	@ManyToOne
	@JoinColumn(name = "unidade_medida_id")
	private UnidadeMedida unidadeMedida;

	public CustoProducaoInsumoServico() {
		super();
	}

	public CustoProducaoInsumoServico(Integer id) {
		super(id);
	}

	public CustoProducaoInsumoServico(Integer id, String especificacao, CustoProducaoTipo tipo, UnidadeMedida unidadeMedida, List<CustoProducaoInsumoServicoPreco> precoList) {
		super(id);
		this.especificacao = especificacao;
		this.tipo = tipo;
		this.unidadeMedida = unidadeMedida;
		this.precoList = precoList;
	}

	public String getEspecificacao() {
		return especificacao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public List<CustoProducaoInsumoServicoPreco> getPrecoList() {
		return precoList;
	}

	public CustoProducaoTipo getTipo() {
		return tipo;
	}

	public UnidadeMedida getUnidadeMedida() {
		return unidadeMedida;
	}

	@Override
	public CustoProducaoInsumoServico infoBasica() {
		return new CustoProducaoInsumoServico(this.id, this.especificacao, this.tipo, this.unidadeMedida == null ? null : this.unidadeMedida.infoBasica(), this.precoList);
	}

	public void setEspecificacao(String especificacao) {
		this.especificacao = especificacao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPrecoList(List<CustoProducaoInsumoServicoPreco> precoList) {
		this.precoList = precoList;
	}

	public void setTipo(CustoProducaoTipo tipo) {
		this.tipo = tipo;
	}

	public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

}