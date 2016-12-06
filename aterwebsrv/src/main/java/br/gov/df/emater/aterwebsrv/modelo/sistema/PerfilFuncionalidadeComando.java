package br.gov.df.emater.aterwebsrv.modelo.sistema;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;

@Entity
@Table(name = "perfil_funcionalidade_comando", schema = EntidadeBase.SISTEMA_SCHEMA)
public class PerfilFuncionalidadeComando extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<PerfilFuncionalidadeComando> {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	@Column
	private Confirmacao conceder;

	@ManyToOne
	@JoinColumn(name = "funcionalidade_comando_id")
	private FuncionalidadeComando funcionalidadeComando;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "perfil_id")
	private Perfil perfil;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PerfilFuncionalidadeComando other = (PerfilFuncionalidadeComando) obj;
		if (funcionalidadeComando == null) {
			if (other.funcionalidadeComando != null)
				return false;
		} else if (!funcionalidadeComando.equals(other.funcionalidadeComando))
			return false;
		if (perfil == null) {
			if (other.perfil != null)
				return false;
		} else if (!perfil.equals(other.perfil))
			return false;
		return true;
	}

	public Confirmacao getConceder() {
		return conceder;
	}

	public FuncionalidadeComando getFuncionalidadeComando() {
		return funcionalidadeComando;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((funcionalidadeComando == null) ? 0 : funcionalidadeComando.hashCode());
		result = prime * result + ((perfil == null) ? 0 : perfil.hashCode());
		return result;
	}

	public void setConceder(Confirmacao conceder) {
		this.conceder = conceder;
	}

	public void setFuncionalidadeComando(FuncionalidadeComando funcionalidadeComando) {
		this.funcionalidadeComando = funcionalidadeComando;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	@Override
	public PerfilFuncionalidadeComando infoBasica() {
		return new PerfilFuncionalidadeComando(this.id, this.perfil != null ? new Perfil(this.perfil.getId()) : null, UtilitarioInfoBasica.infoBasicaReg(this.funcionalidadeComando), this.conceder);
	}

	public PerfilFuncionalidadeComando(Integer id, Perfil perfil, FuncionalidadeComando funcionalidadeComando,
			Confirmacao conceder) {
		super();
		this.id = id;
		this.perfil = perfil;
		this.funcionalidadeComando = funcionalidadeComando;
		this.conceder = conceder;
	}

}
