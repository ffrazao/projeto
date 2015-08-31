package br.gov.df.emater.aterwebsrv.modelo.ipa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PerspectivaProducaoServicoProduto;

/**
 * The persistent class for the perspectiva_agroindustria database table.
 * 
 */
@Entity
@Table(name = "perspectiva_agroindustria", schema = EntidadeBase.IPA_SCHEMA)
@PrimaryKeyJoinColumn(name = "id")
public class PerspectivaProducaoServicoAgroIndustria extends PerspectivaProducaoServico {

	private static final long serialVersionUID = 1L;

	@Column(name = "produto")
	@Enumerated(EnumType.STRING)
	private PerspectivaProducaoServicoProduto produto;

	public PerspectivaProducaoServicoProduto getProduto() {
		return produto;
	}

	public void setProduto(PerspectivaProducaoServicoProduto produto) {
		this.produto = produto;
	}

}