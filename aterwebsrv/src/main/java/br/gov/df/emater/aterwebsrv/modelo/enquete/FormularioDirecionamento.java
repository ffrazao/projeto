package br.gov.df.emater.aterwebsrv.modelo.enquete;

import java.util.Calendar;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "formulario_direcionamento", schema = EntidadeBase.ENQUETE_SCHEMA)
public class FormularioDirecionamento extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@Column(name = "complemento")
	private String complemento;

	@ManyToOne
	@JoinColumn(name = "formulario_id")
	private Formulario formulario;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "inicio")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar inicio;

	@Column(name = "ordem")
	private Integer ordem;

	@Column(name = "permite_impressao")
	@Enumerated(EnumType.STRING)
	private Confirmacao permiteImpressao;

	@OneToMany(mappedBy = "formularioDirecionamento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<RespostaVersao> respostaVersaoList;

	@Column(name = "termino")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar termino;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	public FormularioDirecionamento() {
		super();
	}

	public FormularioDirecionamento(Formulario formulario, Usuario usuario, String complemento, Integer ordem) {
		setFormulario(formulario);
		setUsuario(usuario);
		setComplemento(complemento);
		setOrdem(ordem);
	}

	public FormularioDirecionamento(Integer id) {
		setId(id);
	}

	public String getComplemento() {
		return complemento;
	}

	public Formulario getFormulario() {
		return formulario;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Calendar getInicio() {
		return inicio;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public Confirmacao getPermiteImpressao() {
		return permiteImpressao;
	}

	public List<RespostaVersao> getRespostaVersaoList() {
		return respostaVersaoList;
	}

	public Calendar getTermino() {
		return termino;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public void setFormulario(Formulario formulario) {
		this.formulario = formulario;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setInicio(Calendar inicio) {
		this.inicio = inicio;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public void setPermiteImpressao(Confirmacao permiteImpressao) {
		this.permiteImpressao = permiteImpressao;
	}

	public void setRespostaVersaoList(List<RespostaVersao> respostaVersaoList) {
		this.respostaVersaoList = respostaVersaoList;
	}

	public void setTermino(Calendar termino) {
		this.termino = termino;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}