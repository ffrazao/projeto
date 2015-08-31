package br.gov.df.emater.aterwebsrv.modelo.enquete;

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
@Table(name = "opcao_valor", schema = EntidadeBase.ENQUETE_SCHEMA)
public class OpcaoValor extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "codigo")
	private String codigo;

	@Column(name = "descricao")
	private String descricao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "opcao_resposta_id")
	private OpcaoResposta opcaoResposta;

	@Column(name = "ordem")
	private Integer ordem;

	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public OpcaoResposta getOpcaoResposta() {
		return opcaoResposta;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setOpcaoResposta(OpcaoResposta opcaoResposta) {
		this.opcaoResposta = opcaoResposta;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

}
