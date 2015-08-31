package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.MeioContatoTipo;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * The persistent class for the meio_contato database table.
 * 
 */
@Entity
@Table(name = "meio_contato", schema = EntidadeBase.PESSOA_SCHEMA)
@Inheritance(strategy = InheritanceType.JOINED)
// para identificar classes dentro de contextos polim�rficos
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public abstract class MeioContato extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "meio_contato_tipo")
	@Enumerated(EnumType.STRING)
	private MeioContatoTipo meioContatoTipo;

	//@OneToMany(mappedBy = "meioContato", cascade=CascadeType.ALL, orphanRemoval=true)
	@OneToMany(mappedBy = "meioContato", cascade=CascadeType.ALL)
	private List<PessoaMeioContato> pessoaMeioContatoList;

	public MeioContato() {
	}

	@Override
	public Integer getId() {
		return id;
	}

	public MeioContatoTipo getMeioContatoTipo() {
		return meioContatoTipo;
	}

	public List<PessoaMeioContato> getPessoaMeioContatoList() {
		return pessoaMeioContatoList;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setMeioContatoTipo(MeioContatoTipo meioContatoTipo) {
		this.meioContatoTipo = meioContatoTipo;
	}

	public void setPessoaMeioContatoList(List<PessoaMeioContato> pessoaMeioContatoList) {
		this.pessoaMeioContatoList = pessoaMeioContatoList;
	}

}