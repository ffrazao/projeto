package br.gov.df.emater.aterwebsrv.modelo_planejamento.planejamento;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

@Entity
@Table(name = "meta_tatica_vi", schema = EntidadeBase.PLANEJAMENTO_SCHEMA)
public class MetaTatica extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<MetaTatica> {

	private static final long serialVersionUID = 1L;

	private String ano;

	private String descricao;

	@Column(name="gerencia_tatica")
	private String gerenciaTatica;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	public MetaTatica() {
		super();
	}

	public MetaTatica(Integer id) {
		super(id);
	}

	public MetaTatica(Integer id, String descricao, String ano) {
		this(id);
		setDescricao(descricao);
		setAno(ano);
	}

	public String getAno() {
		return ano;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getGerenciaTatica() {
		return gerenciaTatica;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public MetaTatica infoBasica() {
		return new MetaTatica(getId(), getDescricao(), getAno());
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setGerenciaTatica(String gerenciaTatica) {
		this.gerenciaTatica = gerenciaTatica;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

}