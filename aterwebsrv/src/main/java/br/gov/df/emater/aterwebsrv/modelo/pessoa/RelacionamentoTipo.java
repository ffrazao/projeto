package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

/**
 * The persistent class for the relacionamento_tipo database table.
 * 
 */
@Entity
@Table(name = "relacionamento_tipo", schema = EntidadeBase.PESSOA_SCHEMA)
public class RelacionamentoTipo extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<RelacionamentoTipo> {

	public static enum Codigo {
		ACADEMICO, FAMILIAR, PROFISSIONAL
	}

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private Codigo codigo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	public RelacionamentoTipo() {
	}

	public RelacionamentoTipo(Integer id, String nome) {
		setId(id);
		setNome(nome);
	}

	public Codigo getCodigo() {
		return codigo;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public RelacionamentoTipo infoBasica() {
		return new RelacionamentoTipo(getId(), getNome());
	}

	public void setCodigo(Codigo codigo) {
		this.codigo = codigo;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}