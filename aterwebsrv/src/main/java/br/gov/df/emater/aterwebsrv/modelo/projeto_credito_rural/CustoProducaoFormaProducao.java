package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoValor;

@Entity
@Table(name = "custo_producao_forma_producao", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class CustoProducaoFormaProducao extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<CustoProducaoFormaProducao> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "custo_producao_id")
	private CustoProducao custoProducao;

	@ManyToOne
	@JoinColumn(name = "forma_producao_valor_id")
	private FormaProducaoValor formaProducaoValor;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	public CustoProducaoFormaProducao() {
		super();
	}

	public CustoProducaoFormaProducao(Integer id) {
		super(id);
	}

	public CustoProducaoFormaProducao(Integer id, CustoProducao custoProducao, FormaProducaoValor formaProducaoValor) {
		super();
		this.id = id;
		this.custoProducao = custoProducao;
		this.formaProducaoValor = formaProducaoValor;
	}

	public CustoProducao getCustoProducao() {
		return custoProducao;
	}

	public FormaProducaoValor getFormaProducaoValor() {
		return formaProducaoValor;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public CustoProducaoFormaProducao infoBasica() {
		return new CustoProducaoFormaProducao(this.id, this.custoProducao == null ? null : this.custoProducao.infoBasica(), this.formaProducaoValor == null ? null : this.formaProducaoValor.infoBasica());
	}

	public void setCustoProducao(CustoProducao custoProducao) {
		this.custoProducao = custoProducao;
	}

	public void setFormaProducaoValor(FormaProducaoValor formaProducaoValor) {
		this.formaProducaoValor = formaProducaoValor;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

}