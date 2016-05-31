package br.gov.df.emater.aterwebsrv.modelo.ater;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaCodigo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaTipo;
import br.gov.df.emater.aterwebsrv.modelo.pendencia.Pendencia;

@Entity
@Table(name = "propriedade_rural_pendencia", schema = EntidadeBase.ATER_SCHEMA)
public class PropriedadeRuralPendencia extends EntidadeBase implements _ChavePrimaria<Integer>, Pendencia<PropriedadeRuralPendencia> {

	private static final long serialVersionUID = 1L;

	private String codigo;

	@Lob
	private String descricao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "propriedade_rural_id")
	private PropriedadeRural propriedadeRural;

	@Enumerated(EnumType.STRING)
	private PendenciaTipo tipo;

	public PropriedadeRuralPendencia() {
	}

	public PropriedadeRuralPendencia(Integer id, PendenciaCodigo codigo, String descricao) {
		this.setId(id);
		this.setCodigo(codigo.name());
		this.setTipo(codigo.getTipo());
		this.setDescricao(descricao);
	}

	public PropriedadeRuralPendencia(PendenciaCodigo codigo, String descricao) {
		this(null, codigo, descricao);
	}

	public PropriedadeRuralPendencia(Serializable id) {
		super(id);
	}

	@Override
	public String getCodigo() {
		return codigo;
	}

	@Override
	public String getDescricao() {
		return descricao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	@Transient
	public EntidadeBase getPendenciaDono() {
		return this.getPropriedadeRural();
	}

	public PropriedadeRural getPropriedadeRural() {
		return propriedadeRural;
	}

	@Override
	public PendenciaTipo getTipo() {
		return tipo;
	}

	@Override
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Override
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPropriedadeRural(PropriedadeRural propriedadeRural) {
		this.propriedadeRural = propriedadeRural;
	}

	@Override
	public void setTipo(PendenciaTipo tipo) {
		this.tipo = tipo;
	}

}