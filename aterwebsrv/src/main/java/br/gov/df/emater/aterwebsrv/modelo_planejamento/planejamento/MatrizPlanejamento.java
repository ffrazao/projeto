package br.gov.df.emater.aterwebsrv.modelo_planejamento.planejamento;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

@Entity
@Table(name = "matriz_planejamento", schema = EntidadeBase.PLANEJAMENTO_SCHEMA)
public class MatrizPlanejamento extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<MatrizPlanejamento> {

	private static final long serialVersionUID = 1L;

	private String descricao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	public MatrizPlanejamento() {
		super();
	}

	public MatrizPlanejamento(Integer id) {
		super(id);
	}

	public MatrizPlanejamento(Integer id, String descricao) {
		this(id);
		setDescricao(descricao);
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public MatrizPlanejamento infoBasica() {
		return new MatrizPlanejamento(getId(), getDescricao());
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

}