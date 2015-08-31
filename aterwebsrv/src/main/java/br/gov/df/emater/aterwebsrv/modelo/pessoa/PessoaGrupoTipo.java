package br.gov.df.emater.aterwebsrv.modelo.pessoa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Entity
@Table(name = "pessoa_grupo_tipo", schema = EntidadeBase.PESSOA_SCHEMA)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class PessoaGrupoTipo extends EntidadeBase implements _ChavePrimaria<Integer> {

	public enum Codigo {
		ASSENTAMENTO, BACIA_HIDROGRAFICA, CIDADE, COMUNIDADE, ESTADO, FAIXA_ETARIA, GENERO, LOCALIZACAO, MUNICIPIO, ORGANOGRAMA, PAIS, PERSONALIZADO, TERRITORIAL, ZONA_RURAL,
	}

	private static final long serialVersionUID = 1L;

	private String codigo;

	@Column(name = "gerado_pelo_sistema")
	@Enumerated(EnumType.STRING)
	private Confirmacao geradoPeloSistema;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String nome;

	// @OneToMany(mappedBy = "pessoaGrupoTipo")
	// private List<PessoaGrupo> pessoaGrupoList;

	@Column(name = "sql_elementos")
	@Lob
	private String sqlElementos;

	public PessoaGrupoTipo() {

	}

	public PessoaGrupoTipo(Integer id) {
		super(id);
	}

	public PessoaGrupoTipo(Codigo codigo) {
		setCodigo(codigo.name());
	}

	public String getCodigo() {
		return codigo;
	}

	public Confirmacao getGeradoPeloSistema() {
		return geradoPeloSistema;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getSqlElementos() {
		return sqlElementos;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setGeradoPeloSistema(Confirmacao geradoPeloSistema) {
		this.geradoPeloSistema = geradoPeloSistema;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setSqlElementos(String sqlElementos) {
		this.sqlElementos = sqlElementos;
	}
}