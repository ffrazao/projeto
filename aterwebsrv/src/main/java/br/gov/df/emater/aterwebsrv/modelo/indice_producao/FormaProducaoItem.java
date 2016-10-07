package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaList;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

@Entity
@Table(name = "forma_producao_item", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class FormaProducaoItem extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<FormaProducaoItem> {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "formaProducaoItem")
	private List<FormaProducaoValor> formaProducaoValorList;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	public FormaProducaoItem() {
		super();
	}

	public FormaProducaoItem(Integer id, String nome) {
		super();
		this.id = id;
		this.nome = nome;
	}

	public FormaProducaoItem(Integer id, String nome, List<FormaProducaoValor> formaProducaoValorList) {
		this(id, nome);
		this.formaProducaoValorList = formaProducaoValorList;
	}

	public FormaProducaoItem(Serializable id) {
		super(id);
	}

	public List<FormaProducaoValor> getFormaProducaoValorList() {
		return formaProducaoValorList;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public FormaProducaoItem infoBasica() {
		return new FormaProducaoItem(this.id, this.nome, infoBasicaList(this.formaProducaoValorList));
	}

	public void setFormaProducaoValorList(List<FormaProducaoValor> formaProducaoValorList) {
		this.formaProducaoValorList = formaProducaoValorList;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}