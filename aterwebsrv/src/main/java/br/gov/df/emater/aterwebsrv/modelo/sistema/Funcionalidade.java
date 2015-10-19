package br.gov.df.emater.aterwebsrv.modelo.sistema;

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
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;

@Entity
@Table(name = "funcionalidade", schema = EntidadeBase.SISTEMA_SCHEMA)
public class Funcionalidade extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;
	
	@Enumerated(EnumType.STRING)
	private Confirmacao ativo;
	
	private String codigo;
	
	@OneToMany(mappedBy="funcionalidade")
	private Set<FuncionalidadeComando> funcionalidadeComandoList;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Funcionalidade other = (Funcionalidade) obj;
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

	public Set<FuncionalidadeComando> getFuncionalidadeComandoList() {
		return funcionalidadeComandoList;
	}

	@Override
	public Integer getId() {
		return this.id;
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
	
	public void setAtivo(Confirmacao ativo) {
		this.ativo = ativo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setFuncionalidadeComandoList(Set<FuncionalidadeComando> funcionalidadeComandoList) {
		this.funcionalidadeComandoList = funcionalidadeComandoList;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}