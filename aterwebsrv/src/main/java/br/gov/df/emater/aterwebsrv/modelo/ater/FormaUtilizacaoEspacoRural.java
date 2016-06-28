package br.gov.df.emater.aterwebsrv.modelo.ater;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

/**
 * The persistent class for the forma_utilizacao_espaco_rural database table.
 * 
 */
@Entity
@Table(name = "forma_utilizacao_espaco_rural", schema = EntidadeBase.ATER_SCHEMA)
// @Indexed
public class FormaUtilizacaoEspacoRural extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<FormaUtilizacaoEspacoRural> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	// @Field(index = Index.YES, store = Store.YES)
	private String nome;

	public FormaUtilizacaoEspacoRural() {
	}

	public FormaUtilizacaoEspacoRural(Integer id, String nome) {
		setId(id);
		setNome(nome);
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public FormaUtilizacaoEspacoRural infoBasica() {
		return new FormaUtilizacaoEspacoRural(getId(), getNome());
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}