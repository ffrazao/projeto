package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;

@Entity
@Table(name = "ipa_producao_forma", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class IpaProducaoForma extends EntidadeBase{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JoinColumn(name = "ipa_producao_id")
	private IpaProducao ipaProducao;
	
	@ManyToOne
	@JoinColumn(name = "forma_producao_valor_id")
	private FormaProducaoValor formaProducaoValor;
	
	@ManyToOne
	@JoinColumn(name = "bem_classificacao_id")
	private BemClassificacao BemClassificacao;
	
	private Integer ordem;
	
	

	public IpaProducaoForma() {
		super();
	}



//	public IpaProducaoForma(Integer id, IpaProducao ipaProducao, FormaProducaoItem formaProducaoValor, Integer ordem) {
//		super();
//		this.id = id;
//		this.ipaProducao = ipaProducao;
//		this.formaProducaoValor = formaProducaoValor;
//		this.ordem = ordem;
//	}



//	public IpaProducaoForma(IpaProducaoForma pbc) {
//		this.id = pbc.getId();
//		//this.ipaProducao = ipaProducao;
//		this.formaProducaoValor = pbc.getFormaProducaoValor();
//		this.ordem = pbc.getOrdem();
//	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public IpaProducao getIpaProducao() {
		return ipaProducao;
	}



	public void setIpaProducao(IpaProducao ipaProducao) {
		this.ipaProducao = ipaProducao;
	}




	public Integer getOrdem() {
		return ordem;
	}



	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}



	public FormaProducaoValor getFormaProducaoValor() {
		return formaProducaoValor;
	}



	public void setFormaProducaoValor(FormaProducaoValor formaProducaoValor) {
		this.formaProducaoValor = formaProducaoValor;
	}



	public BemClassificacao getBemClassificacao() {
		return BemClassificacao;
	}



	public void setBemClassificacao(BemClassificacao bemClassificacao) {
		BemClassificacao = bemClassificacao;
	}
	
	
	

}
