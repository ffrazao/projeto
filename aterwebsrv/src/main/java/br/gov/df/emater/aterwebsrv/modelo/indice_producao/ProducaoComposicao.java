package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

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

@Entity
@Table(name = "producao_composicao", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class ProducaoComposicao extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<ProducaoComposicao> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "forma_producao_valor_id")
	private FormaProducaoValor formaProducaoValor;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private Integer ordem;

	@ManyToOne
	@JoinColumn(name = "producao_id")
	private Producao producao;

	public ProducaoComposicao() {

	}

	public ProducaoComposicao(Integer id) {
		super(id);
	}

	public ProducaoComposicao(Integer id, Producao producao, FormaProducaoValor formaProducaoValor, Integer ordem) {
		super();
		this.id = id;
		this.producao = producao;
		this.formaProducaoValor = formaProducaoValor;
		this.ordem = ordem;
	}

	public ProducaoComposicao(ProducaoComposicao producaoComposicao) {
		this.formaProducaoValor = producaoComposicao.getFormaProducaoValor();
		this.ordem = producaoComposicao.getOrdem();
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

	public Producao getProducao() {
		return producao;
	}

	@Override
	public ProducaoComposicao infoBasica() {
		return new ProducaoComposicao(this.id, this.producao == null ? null : new Producao(this.producao.getId()), infoBasicaReg(this.formaProducaoValor), this.ordem);
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

	public void setProducao(Producao producao) {
		this.producao = producao;
	}

}