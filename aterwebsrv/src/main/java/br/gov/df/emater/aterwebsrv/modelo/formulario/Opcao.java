package br.gov.df.emater.aterwebsrv.modelo.formulario;

import javax.persistence.Column;
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
@Table(schema = EntidadeBase.FORMULARIO_SCHEMA)
public class Opcao extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "elemento_id")
	private Elemento elemento;

	@Column(name="formulario_codigo")
	private String formularioCodigo;

	@Column(name="formulario_versao")
	private Integer formularioVersao;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	public Elemento getElemento() {
		return elemento;
	}

	public String getFormularioCodigo() {
		return formularioCodigo;
	}

	public Integer getFormularioVersao() {
		return formularioVersao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setElemento(Elemento elemento) {
		this.elemento = elemento;
	}

	public void setFormularioCodigo(String formularioCodigo) {
		this.formularioCodigo = formularioCodigo;
	}

	public void setFormularioVersao(Integer formularioVersao) {
		this.formularioVersao = formularioVersao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

}
