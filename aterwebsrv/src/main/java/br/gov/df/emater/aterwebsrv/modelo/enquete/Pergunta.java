package br.gov.df.emater.aterwebsrv.modelo.enquete;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;

@Entity
@Table(name = "pergunta", schema = EntidadeBase.ENQUETE_SCHEMA)
public class Pergunta extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "explicacao")
	@Lob
	private String explicacao;

	@ManyToOne
	@JoinColumn(name = "formulario_id")
	private Formulario formulario;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "obrigatorio")
	@Enumerated(EnumType.STRING)
	private Confirmacao obrigatorio;

	@ManyToOne
	@JoinColumn(name = "opcao_resposta_id")
	private OpcaoResposta opcaoResposta;

	@Column(name = "ordem")
	private Integer ordem;

	@Column(name = "pergunta")
	@Lob
	private String pergunta;

	@OneToOne(mappedBy = "pergunta")
	@Transient
	private Resposta resposta;

	public String getExplicacao() {
		return explicacao;
	}

	public Formulario getFormulario() {
		return formulario;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Confirmacao getObrigatorio() {
		return obrigatorio;
	}

	public OpcaoResposta getOpcaoResposta() {
		return opcaoResposta;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public String getPergunta() {
		return pergunta;
	}

	public Resposta getResposta() {
		return resposta;
	}

	public void setExplicacao(String explicacao) {
		this.explicacao = explicacao;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setObrigatorio(Confirmacao obrigatorio) {
		this.obrigatorio = obrigatorio;
	}

	public void setOpcaoResposta(OpcaoResposta opcaoResposta) {
		this.opcaoResposta = opcaoResposta;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public void setPergunta(String pergunta) {
		this.pergunta = pergunta;
	}

	public void setResposta(Resposta resposta) {
		this.resposta = resposta;
	}

}
