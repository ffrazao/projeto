package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.GrupoSocialEscopo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;

/**
 * The persistent class for the grupo_social database table.
 * 
 */
@Entity
@Table(name = "grupo_social", schema = EntidadeBase.PESSOA_SCHEMA)
@PrimaryKeyJoinColumn(name = "id")
public class GrupoSocial extends Pessoa {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private GrupoSocialEscopo escopo;

	@ManyToOne
	@JoinColumn(name = "grupo_social_tipo_id")
	private GrupoSocialTipo grupoSocialTipo;

	public GrupoSocial() {
		this(null, null, null, null, null, null, null, null);
	}

	public GrupoSocial(Integer id) {
		this(id, null, null, null, null, null, null, null);
	}

	public GrupoSocial(Integer id, String nome, String apelidoSigla, Arquivo perfilArquivo, PessoaSituacao situacao, Confirmacao publicoAlvoConfirmacao, GrupoSocialTipo grupoSocialTipo, GrupoSocialEscopo escopo) {
		super(id, PessoaTipo.GS, nome, apelidoSigla, perfilArquivo, situacao, publicoAlvoConfirmacao == null ? Confirmacao.N : publicoAlvoConfirmacao);
		setGrupoSocialTipo(grupoSocialTipo);
		setEscopo(escopo);
	}

	public GrupoSocialEscopo getEscopo() {
		return escopo;
	}

	public GrupoSocialTipo getGrupoSocialTipo() {
		return grupoSocialTipo;
	}

	@Override
	public GrupoSocial infoBasica() {
		return new GrupoSocial(this.getId(), this.getNome(), this.getApelidoSigla(), this.getPerfilArquivo(), this.getSituacao(), this.getPublicoAlvoConfirmacao(), this.getGrupoSocialTipo(), this.getEscopo());
	}

	public void setEscopo(GrupoSocialEscopo escopo) {
		this.escopo = escopo;
	}

	public void setGrupoSocialTipo(GrupoSocialTipo grupoSocialTipo) {
		this.grupoSocialTipo = grupoSocialTipo;
	}

}