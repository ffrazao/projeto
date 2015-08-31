package br.gov.df.emater.aterwebsrv.modelo.pessoa;

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
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ConfirmacaoOpcional;

/**
 * The persistent class for the relacionamento_tipo database table.
 * 
 */
@Entity
@Table(name = "relacionamento_tipo", schema = EntidadeBase.PESSOA_SCHEMA)
public class RelacionamentoTipo extends EntidadeBase implements _ChavePrimaria<Integer> {

	public enum Codigo {
		ACADEMICO, FAMILIAR, GESTAO_GRUPO_SOCIAL, ORGANOGRAMA, PERSONALIZADO, PROFISSIONAL
	}

	private static final long serialVersionUID = 1L;

	private String codigo;

	@Column(name = "gerado_pelo_sistema")
	@Enumerated(EnumType.STRING)
	private Confirmacao geradoPeloSistema = Confirmacao.N;

	@Column(name = "grupo_social")
	@Enumerated(EnumType.STRING)
	private Confirmacao grupoSocial;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	@ManyToOne
	@JoinColumn(name = "relacionamento_tipo_id")
	private RelacionamentoTipo relacionamentoTipo;

	@Enumerated(EnumType.STRING)
	private ConfirmacaoOpcional temporario;

	public RelacionamentoTipo() {
	}

	public RelacionamentoTipo(Codigo codigo) {
		setCodigo(codigo.name());
	}

	public RelacionamentoTipo(Integer id) {
		super(id);
	}

	public String getCodigo() {
		return codigo;
	}

	public Confirmacao getGeradoPeloSistema() {
		return geradoPeloSistema;
	}

	public Confirmacao getGrupoSocial() {
		return grupoSocial;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public RelacionamentoTipo getRelacionamentoTipo() {
		return relacionamentoTipo;
	}

	public ConfirmacaoOpcional getTemporario() {
		return temporario;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setGeradoPeloSistema(Confirmacao geradoPeloSistema) {
		this.geradoPeloSistema = geradoPeloSistema;
	}

	public void setGrupoSocial(Confirmacao grupoSocial) {
		this.grupoSocial = grupoSocial;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setRelacionamentoTipo(RelacionamentoTipo relacionamentoTipo) {
		this.relacionamentoTipo = relacionamentoTipo;
	}

	public void setTemporario(ConfirmacaoOpcional temporario) {
		this.temporario = temporario;
	}

}