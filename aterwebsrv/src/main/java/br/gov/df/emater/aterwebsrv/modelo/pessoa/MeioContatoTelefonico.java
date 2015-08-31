package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.dominio.MeioContatoTipo;

/**
 * The persistent class for the meio_contato_telefonico database table.
 * 
 */
@Entity
@Table(name = "meio_contato_telefonico", schema = EntidadeBase.PESSOA_SCHEMA)
@PrimaryKeyJoinColumn(name = "id")
@Indexed
public class MeioContatoTelefonico extends MeioContato {

	private static final long serialVersionUID = 1L;

	@Field(index = Index.YES, store = Store.YES)
	private String numero;

	public MeioContatoTelefonico() {
		setMeioContatoTipo(MeioContatoTipo.TEL);
	}

	public MeioContatoTelefonico(String numero) {
		setNumero(numero);
	}

	public String getNumero() {
		return this.numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}
}