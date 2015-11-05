package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

/**
 * The persistent class for the relacionamento_funcao database table.
 * 
 */
@Entity
@Table(name = "relacionamento_funcao", schema = EntidadeBase.PESSOA_SCHEMA)
public class RelacionamentoFuncao extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "nome_se_feminino")
	private String nomeSeFeminino;

	@Column(name = "nome_se_masculino")
	private String nomeSeMasculino;

	public RelacionamentoFuncao() {
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNomeSeFeminino() {
		return nomeSeFeminino;
	}

	public String getNomeSeMasculino() {
		return nomeSeMasculino;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNomeSeFeminino(String nomeSeFeminino) {
		this.nomeSeFeminino = nomeSeFeminino;
	}

	public void setNomeSeMasculino(String nomeSeMasculino) {
		this.nomeSeMasculino = nomeSeMasculino;
	}

}