package br.gov.df.emater.aterwebsrv.modelo.indice_producao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;

@Entity
@Table(name = "ipa", schema = EntidadeBase.INDICE_PRODUCAO_SCHEMA)
public class Ipa extends EntidadeBase{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private Integer ano;
	
	@ManyToOne
	@JoinColumn(name = "unidade_organizacional_id")
	private UnidadeOrganizacional unidadeOrganizacional;
	
	@ManyToOne
	@JoinColumn(name = "publico_alvo_id")
	private PublicoAlvo publicoAlvo;

	@ManyToOne
	@JoinColumn(name = "propriedade_rural_id")
	private PropriedadeRural propriedadeRural;

	@Transient
	private IpaProducao ipaProducao;
	
	public Ipa() {
		super();
	}

	public Ipa(Ipa ipa) {
		super();
		this.id = ipa.getId();
		this.ano = ipa.getAno();
		this.unidadeOrganizacional = ipa.getUnidadeOrganizacional(); 
		this.publicoAlvo = ipa.getPublicoAlvo();
		this.propriedadeRural = ipa.getPropriedadeRural();
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public UnidadeOrganizacional getUnidadeOrganizacional() {
		return unidadeOrganizacional;
	}

	public void setUnidadeOrganizacional(UnidadeOrganizacional unidadeOrganizacional) {
		this.unidadeOrganizacional = unidadeOrganizacional;
	}

	public PublicoAlvo getPublicoAlvo() {
		return publicoAlvo;
	}

	public void setPublicoAlvo(PublicoAlvo publicoAlvo) {
		this.publicoAlvo = publicoAlvo;
	}

	public PropriedadeRural getPropriedadeRural() {
		return propriedadeRural;
	}

	public void setPropriedadeRural(PropriedadeRural propriedadeRural) {
		this.propriedadeRural = propriedadeRural;
	}

	public IpaProducao getIpaProducao() {
		return ipaProducao;
	}

	public void setIpaProducao(IpaProducao ipaProducao) {
		this.ipaProducao = ipaProducao;
	}
	
}
