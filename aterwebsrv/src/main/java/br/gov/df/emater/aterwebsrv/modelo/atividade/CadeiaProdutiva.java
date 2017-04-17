package br.gov.df.emater.aterwebsrv.modelo.atividade;

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
@Table(name = "cadeia_produtiva", schema = EntidadeBase.ATIVIDADE_SCHEMA)
public class CadeiaProdutiva extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<CadeiaProdutiva> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "nome")
	private String nome;

	public CadeiaProdutiva() {
		super();
	}

	public CadeiaProdutiva(Integer id) {
		super(id);
	}

	
	public CadeiaProdutiva(Integer id, String nome) {
		this.setId(id);
		this.setNome(nome);
	}


	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public CadeiaProdutiva infoBasica() {
		return new CadeiaProdutiva(getId(), getNome() );
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}



}