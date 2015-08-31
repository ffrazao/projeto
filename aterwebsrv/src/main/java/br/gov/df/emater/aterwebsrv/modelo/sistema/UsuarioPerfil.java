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

/**
 * The persistent class for the usuario_perfil database table.
 * 
 */
@Entity
@Table(name = "usuario_perfil", schema = EntidadeBase.SISTEMA_SCHEMA)
public class UsuarioPerfil extends EntidadeBase implements _ChavePrimaria<Integer> {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "perfil_id")
	private Perfil perfil;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	public UsuarioPerfil() {
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsuarioPerfil other = (UsuarioPerfil) obj;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		if (perfil == null) {
			if (other.perfil != null)
				return false;
		} else if (!perfil.equals(other.perfil))
			return false;
		return true;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result + ((perfil == null) ? 0 : perfil.hashCode());
		return result;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}