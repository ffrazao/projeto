package br.gov.df.emater.aterwebsrv.modelo.enquete;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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
import br.gov.df.emater.aterwebsrv.modelo.dominio.OpcaoRespostaTipo;

@Entity
@Table(name = "opcao_resposta", schema = EntidadeBase.ENQUETE_SCHEMA)
public class OpcaoResposta extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "nome")
	private String nome;

	@ManyToOne
	@JoinColumn(name = "opcao_resposta_id")
	private OpcaoResposta opcaoResposta;

	@Column(name = "opcao_resposta_tipo")
	@Enumerated(EnumType.STRING)
	private OpcaoRespostaTipo opcaoRespostaTipo;

	@OneToMany(mappedBy = "opcaoResposta", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OpcaoValor> opcaoValorList;

	@Column(name = "sql")
	@Lob
	private String sql;

	@Override
	public Integer getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public OpcaoResposta getOpcaoResposta() {
		return opcaoResposta;
	}

	public OpcaoRespostaTipo getOpcaoRespostaTipo() {
		return opcaoRespostaTipo;
	}

	public List<OpcaoValor> getOpcaoValorList() {
		return opcaoValorList;
	}

	public String getSql() {
		return sql;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setOpcaoResposta(OpcaoResposta opcaoResposta) {
		this.opcaoResposta = opcaoResposta;
	}

	public void setOpcaoRespostaTipo(OpcaoRespostaTipo opcaoRespostaTipo) {
		this.opcaoRespostaTipo = opcaoRespostaTipo;
	}

	public void setOpcaoValorList(List<OpcaoValor> opcaoValorList) {
		this.opcaoValorList = opcaoValorList;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

}