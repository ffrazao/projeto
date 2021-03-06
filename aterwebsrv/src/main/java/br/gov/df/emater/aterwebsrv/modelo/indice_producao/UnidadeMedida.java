package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

@Entity
@Table(name = "unidade_medida", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class UnidadeMedida extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<UnidadeMedida> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	public UnidadeMedida() {
		super();
	}

	public UnidadeMedida(Integer id) {
		super();
		this.id = id;
	}

	public UnidadeMedida(Integer id, String nome) {
		super(id);
		this.nome = nome;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public UnidadeMedida infoBasica() {
		return new UnidadeMedida(this.id, this.nome);
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}