package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ItemNomeResultado;

@Entity
@Table(name = "item_nome", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class ItemNome extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	@Enumerated(EnumType.STRING)
	private ItemNomeResultado resultado;

	public ItemNome() {
		super();
	}

	public ItemNome(Integer id) {
		super(id);
	}

	public ItemNome(String nome) {
		setNome(nome);
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public ItemNomeResultado getResultado() {
		return resultado;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setResultado(ItemNomeResultado resultado) {
		this.resultado = resultado;
	}

}