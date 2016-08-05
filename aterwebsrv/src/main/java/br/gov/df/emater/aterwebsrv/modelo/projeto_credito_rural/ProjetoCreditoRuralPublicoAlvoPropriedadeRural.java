package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;

@Entity
@Table(name = "projeto_credito_rural_publico_alvo_propriedade_rural", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class ProjetoCreditoRuralPublicoAlvoPropriedadeRural extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<ProjetoCreditoRuralPublicoAlvoPropriedadeRural> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "projeto_credito_rural_id")
	private ProjetoCreditoRural projetoCreditoRural;

	@ManyToOne
	@JoinColumn(name = "publico_alvo_propriedade_rural_id")
	private PublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural;

	public ProjetoCreditoRuralPublicoAlvoPropriedadeRural() {
		super();
	}

	public ProjetoCreditoRuralPublicoAlvoPropriedadeRural(Integer id) {
		super(id);
	}

	public ProjetoCreditoRuralPublicoAlvoPropriedadeRural(Integer id, PublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural) {
		super();
		this.id = id;
		this.publicoAlvoPropriedadeRural = publicoAlvoPropriedadeRural;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public ProjetoCreditoRural getProjetoCreditoRural() {
		return projetoCreditoRural;
	}

	public PublicoAlvoPropriedadeRural getPublicoAlvoPropriedadeRural() {
		return publicoAlvoPropriedadeRural;
	}

	@Override
	public ProjetoCreditoRuralPublicoAlvoPropriedadeRural infoBasica() {
		return new ProjetoCreditoRuralPublicoAlvoPropriedadeRural(this.id, infoBasicaReg(this.publicoAlvoPropriedadeRural));
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setProjetoCreditoRural(ProjetoCreditoRural projetoCreditoRural) {
		this.projetoCreditoRural = projetoCreditoRural;
	}

	public void setPublicoAlvoPropriedadeRural(PublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural) {
		this.publicoAlvoPropriedadeRural = publicoAlvoPropriedadeRural;
	}

}