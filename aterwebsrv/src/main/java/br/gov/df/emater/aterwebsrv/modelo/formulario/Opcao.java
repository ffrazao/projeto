package br.gov.df.emater.aterwebsrv.modelo.formulario;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

	@OneToMany
	@JoinColumn(name = "formulario_id")
	private List<Formulario> formularioList;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Lob
	private String valor;

	public Elemento getElemento() {
		return elemento;
	}

	public List<Formulario> getFormularioList() {
		return formularioList;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getValor() {
		return valor;
	}

	public void setElemento(Elemento elemento) {
		this.elemento = elemento;
	}

	public void setFormularioList(List<Formulario> formularioList) {
		this.formularioList = formularioList;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
