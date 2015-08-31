package br.gov.df.emater.aterwebsrv.modelo.enquete;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Table(name = "resposta", schema = EntidadeBase.ENQUETE_SCHEMA)
public class Resposta extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "pergunta_id")
	private Pergunta pergunta;

	@ManyToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name = "resposta_versao_id")
	private RespostaVersao respostaVersao;

	@Column(name = "valor")
	@Lob
	private String valor;

	public Resposta() {
	}
	
	public Resposta(Integer id) {
		setId(id);
	}
	
	public Resposta(Integer id, String valor, RespostaVersao respostaVersao) {
		setId(id);
		setValor(valor);
		setRespostaVersao(respostaVersao);
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Pergunta getPergunta() {
		return pergunta;
	}

	public RespostaVersao getRespostaVersao() {
		return respostaVersao;
	}

	public String getValor() {
		return valor;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPergunta(Pergunta pergunta) {
		this.pergunta = pergunta;
	}

	public void setRespostaVersao(RespostaVersao respostaVersao) {
		this.respostaVersao = respostaVersao;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
