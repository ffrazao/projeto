package br.gov.df.emater.aterwebsrv.modelo.sistema;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

@Entity
@Table(name = "funcionalidade_comando", schema = EntidadeBase.SISTEMA_SCHEMA)
public class FuncionalidadeComando extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "comando_id")
	private Comando comando;

	@ManyToOne
	@JoinColumn(name = "funcionalidade_id")
	private Funcionalidade funcionalidade;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FuncionalidadeComando other = (FuncionalidadeComando) obj;
		if (comando == null) {
			if (other.comando != null)
				return false;
		} else if (!comando.equals(other.comando))
			return false;
		if (funcionalidade == null) {
			if (other.funcionalidade != null)
				return false;
		} else if (!funcionalidade.equals(other.funcionalidade))
			return false;
		return true;
	}

	public Comando getComando() {
		return comando;
	}

	public Funcionalidade getFuncionalidade() {
		return funcionalidade;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((comando == null) ? 0 : comando.hashCode());
		result = prime * result + ((funcionalidade == null) ? 0 : funcionalidade.hashCode());
		return result;
	}

	public void setComando(Comando comando) {
		this.comando = comando;
	}

	public void setFuncionalidade(Funcionalidade funcionalidade) {
		this.funcionalidade = funcionalidade;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
}