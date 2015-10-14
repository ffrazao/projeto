package br.gov.df.emater.aterwebsrv.modelo.sistema;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;

/**
 * The persistent class for the perfil database table.
 * 
 */
@Entity
@Table(name = "perfil", schema = EntidadeBase.SISTEMA_SCHEMA)
public class Perfil extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	@Column
	private Confirmacao ativo;

	@Column
	private String codigo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	@OneToMany(mappedBy = "perfil")
	private List<PerfilFuncionalidadeComando> perfilFuncionalidadeComandoList;

	public Perfil() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Perfil other = (Perfil) obj;
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
		return this.codigo;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public List<PerfilFuncionalidadeComando> getPerfilFuncionalidadeComandoList() {
		return perfilFuncionalidadeComandoList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
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

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPerfilFuncionalidadeComandoList(List<PerfilFuncionalidadeComando> perfilFuncionalidadeComandoList) {
		this.perfilFuncionalidadeComandoList = perfilFuncionalidadeComandoList;
	}

}