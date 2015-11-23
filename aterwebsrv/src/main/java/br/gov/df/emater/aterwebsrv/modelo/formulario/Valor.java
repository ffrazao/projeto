package br.gov.df.emater.aterwebsrv.modelo.formulario;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;

@Entity
@Table(schema = EntidadeBase.FORMULARIO_SCHEMA)
public class Valor extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "coleta_id")
	private Coleta coleta;

	@ManyToOne
	@JoinColumn(name = "formulario_versao_elemento_id")
	private FormularioVersaoElemento formularioVersaoElemento;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Lob
	private String valor;

	public Coleta getColeta() {
		return coleta;
	}

	public FormularioVersaoElemento getFormularioVersaoElemento() {
		return formularioVersaoElemento;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getValor() {
		return valor;
	}

	public void setColeta(Coleta coleta) {
		this.coleta = coleta;
	}

	public void setFormularioVersaoElemento(FormularioVersaoElemento formularioVersaoElemento) {
		this.formularioVersaoElemento = formularioVersaoElemento;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
