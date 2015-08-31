package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGrupoNivelGestao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;

/**
 * Classe persistente da tabela pessoa_grupo
 * 
 * @author frazao
 * 
 */
@Entity
@Table(name = "pessoa_grupo", schema = EntidadeBase.PESSOA_SCHEMA)
@PrimaryKeyJoinColumn(name = "id")
public class PessoaGrupo extends Pessoa {

	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "pai", fetch = FetchType.LAZY)
	private List<PessoaGrupo> filhos;

	@Column(name = "nivel_gestao")
	@Enumerated(EnumType.STRING)
	private PessoaGrupoNivelGestao nivelGestao;

	@ManyToOne
	@JoinColumn(name = "pessoa_grupo_id")
	private PessoaGrupo pai;

	@ManyToOne
	@JoinColumn(name = "pessoa_grupo_tipo_id")
	private PessoaGrupoTipo pessoaGrupoTipo;

	public PessoaGrupo() {
		setPessoaTipo(PessoaTipo.GS);
	}

	public PessoaGrupo(Integer id) {
		super(id);
		setPessoaTipo(PessoaTipo.GS);
	}

	public PessoaGrupo(Integer id, String nome, String apelidoSigla) {
		super(id, nome, apelidoSigla);
		setPessoaTipo(PessoaTipo.GS);
	}

	public List<PessoaGrupo> getFilhos() {
		return filhos;
	}

	public PessoaGrupoNivelGestao getNivelGestao() {
		return nivelGestao;
	}

	public PessoaGrupo getPai() {
		return pai;
	}

	public PessoaGrupoTipo getPessoaGrupoTipo() {
		return pessoaGrupoTipo;
	}

	public void setFilhos(List<PessoaGrupo> filhos) {
		this.filhos = filhos;
	}

	public void setNivelGestao(PessoaGrupoNivelGestao nivelGestao) {
		this.nivelGestao = nivelGestao;
	}

	public void setPai(PessoaGrupo pai) {
		this.pai = pai;
	}

	public void setPessoaGrupoTipo(PessoaGrupoTipo pessoaGrupoTipo) {
		this.pessoaGrupoTipo = pessoaGrupoTipo;
	}
}