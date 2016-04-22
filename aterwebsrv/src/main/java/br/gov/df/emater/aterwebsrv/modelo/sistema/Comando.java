package br.gov.df.emater.aterwebsrv.modelo.sistema;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;

@Entity
@Table(name = "comando", schema = EntidadeBase.SISTEMA_SCHEMA)
public class Comando extends EntidadeBase implements _ChavePrimaria<Integer> {

	public static enum Codigo {
		CONSULTAR("Consultar"), EDITAR("Editar"), EXCLUIR("Excluir"), INCLUIR("Incluir"), VISUALIZAR("Visualizar");

		private String descricao;

		private Codigo(String descricao) {
			this.descricao = descricao;
		}

		public String toString() {
			return this.descricao;
		}
	}

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private Confirmacao ativo;

	private String codigo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	public Confirmacao getAtivo() {
		return ativo;
	}

	public String getCodigo() {
		return codigo;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	public String getNome() {
		return nome;
	}

	public void setAtivo(Confirmacao ativo) {
		this.ativo = ativo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
