package br.gov.df.emater.aterwebsrv.modelo.sistema;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;

/**
 * The persistent class for the modulo database table.
 * 
 */
@Entity
@Table(name = "modulo", schema = EntidadeBase.SISTEMA_SCHEMA)
public class Modulo extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<Modulo> {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private Confirmacao ativo;

	private String codigo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToMany(mappedBy = "modulo")
	private Set<ModuloFuncionalidade> moduloFuncionalidadeList;

	private String nome;

	public Modulo() {
	}

	public Modulo(Integer id, String nome, String codigo, Confirmacao ativo) {
		this.id = id;
		this.nome = nome;
		this.codigo = codigo;
		this.ativo = ativo;
	}

	public Modulo(Serializable id) {
		super(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Modulo other = (Modulo) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	public Confirmacao getAtivo() {
		return ativo;
	}

	public String getCodigo() {
		return codigo;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Set<ModuloFuncionalidade> getModuloFuncionalidadeList() {
		return moduloFuncionalidadeList;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public Modulo infoBasica() {
		return new Modulo(getId(), getNome(), getCodigo(), getAtivo());
	}

	public void setAtivo(Confirmacao ativo) {
		this.ativo = ativo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setModuloFuncionalidadeList(Set<ModuloFuncionalidade> moduloFuncionalidadeList) {
		this.moduloFuncionalidadeList = moduloFuncionalidadeList;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}