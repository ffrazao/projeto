package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ParecerTecnicoCodigo;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.rest.json.JsonDeserializerData;
import br.gov.df.emater.aterwebsrv.rest.json.JsonSerializerData;

@Entity
@Table(name = "projeto_credito_rural_parecer_tecnico", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class ProjetoCreditoRuralParecerTecnico extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<ProjetoCreditoRuralParecerTecnico> {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private ParecerTecnicoCodigo codigo;

	@Lob
	private String conteudo;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@JsonSerialize(using = JsonSerializerData.class)
	@JsonDeserialize(using = JsonDeserializerData.class)
	private Calendar data;

	private String descricao;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private Integer ordem;

	@ManyToOne
	@JoinColumn(name = "projeto_credito_rural_id")
	private ProjetoCreditoRural projetoCreditoRural;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	public ProjetoCreditoRuralParecerTecnico() {
		super();
	}

	public ProjetoCreditoRuralParecerTecnico(Integer id) {
		super(id);
	}

	public ProjetoCreditoRuralParecerTecnico(ParecerTecnicoCodigo codigo, String conteudo, Calendar data, String descricao, Integer id, Integer ordem, Usuario usuario) {
		super();
		this.codigo = codigo;
		this.conteudo = conteudo;
		this.data = data;
		this.descricao = descricao;
		this.id = id;
		this.ordem = ordem;
		this.usuario = usuario;
	}

	public ParecerTecnicoCodigo getCodigo() {
		return codigo;
	}

	public String getConteudo() {
		return conteudo;
	}

	public Calendar getData() {
		return data;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public ProjetoCreditoRural getProjetoCreditoRural() {
		return projetoCreditoRural;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	@Override
	public ProjetoCreditoRuralParecerTecnico infoBasica() {
		return new ProjetoCreditoRuralParecerTecnico(this.codigo, this.conteudo, this.data, this.descricao, this.id, this.ordem, infoBasicaReg(this.usuario));
	}

	public void setCodigo(ParecerTecnicoCodigo codigo) {
		this.codigo = codigo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public void setProjetoCreditoRural(ProjetoCreditoRural projetoCreditoRural) {
		this.projetoCreditoRural = projetoCreditoRural;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}