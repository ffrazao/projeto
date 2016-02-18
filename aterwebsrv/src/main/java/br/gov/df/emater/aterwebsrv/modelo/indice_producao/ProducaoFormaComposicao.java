package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

@Entity
@Table(name = "producao_forma_composicao", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class ProducaoFormaComposicao extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "forma_producao_valor_id")
	private FormaProducaoValor formaProducaoValor;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private Integer ordem;

	@ManyToOne
	@JoinColumn(name = "producao_forma_id")
	private ProducaoForma producaoForma;

	public ProducaoFormaComposicao() {

	}

	public ProducaoFormaComposicao(Integer id) {
		super(id);
	}

	public ProducaoFormaComposicao(ProducaoFormaComposicao producaoFormaComposicao) {
		this.formaProducaoValor = producaoFormaComposicao.getFormaProducaoValor();
		this.ordem = producaoFormaComposicao.getOrdem();
	}

	public FormaProducaoValor getFormaProducaoValor() {
		return formaProducaoValor;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public ProducaoForma getProducaoForma() {
		return producaoForma;
	}

	public void setFormaProducaoValor(FormaProducaoValor formaProducaoValor) {
		this.formaProducaoValor = formaProducaoValor;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public void setProducaoForma(ProducaoForma producaoForma) {
		this.producaoForma = producaoForma;
	}

}