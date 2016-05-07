package br.gov.df.emater.aterwebsrv.modelo.ater;

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

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaTipo;

@Entity
@Table(name = "propriedade_rural_pendencia", schema = EntidadeBase.ATER_SCHEMA)
public class PropriedadeRuralPendencia extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

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

	public String getDescricao() {
		return descricao;
	}

	public Integer getId() {
		return id;
	}

	public PropriedadeRural getPropriedadeRural() {
		return propriedadeRural;
	}

	public PendenciaTipo getTipo() {
		return tipo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	public void setId(Integer id) {
		this.id = id;
	}

	public void setPropriedadeRural(PropriedadeRural propriedadeRural) {
		this.propriedadeRural = propriedadeRural;
	}

	public void setTipo(PendenciaTipo tipo) {
		this.tipo = tipo;
	}

}