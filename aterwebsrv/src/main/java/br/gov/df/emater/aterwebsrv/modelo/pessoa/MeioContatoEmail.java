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
 * The persistent class for the meio_contato_email database table.
 * 
 */
@Entity
@Table(name = "meio_contato_email", schema = EntidadeBase.PESSOA_SCHEMA)
@PrimaryKeyJoinColumn(name = "id")
@Indexed
public class MeioContatoEmail extends MeioContato {

	private static final long serialVersionUID = 1L;

	@Field(index = Index.YES, store = Store.YES)
	private String email;

	public MeioContatoEmail() {
		setMeioContatoTipo(MeioContatoTipo.EMA);
	}

	public MeioContatoEmail(String email) {
		setEmail(email);
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}