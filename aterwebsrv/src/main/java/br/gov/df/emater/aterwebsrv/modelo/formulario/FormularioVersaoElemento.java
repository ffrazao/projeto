package br.gov.df.emater.aterwebsrv.modelo.formulario;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

@Entity
@Table(schema = EntidadeBase.FORMULARIO_SCHEMA, name = "formulario_versao_elemento")
public class FormularioVersaoElemento extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "elemento_id")
	private Elemento elemento;

	@ManyToOne
	@JoinColumn(name = "formulario_versao_id")
	private FormularioVersao formularioVersao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private Integer ordem;

	public Elemento getElemento() {
		return elemento;
	}

	public FormularioVersao getFormularioVersao() {
		return formularioVersao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setElemento(Elemento elemento) {
		this.elemento = elemento;
	}

	public void setFormularioVersao(FormularioVersao formularioVersao) {
		this.formularioVersao = formularioVersao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

}