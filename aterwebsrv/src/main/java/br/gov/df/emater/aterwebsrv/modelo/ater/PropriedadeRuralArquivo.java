package br.gov.df.emater.aterwebsrv.modelo.ater;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;

@Entity
@Table(name = "propriedade_rural_arquivo", schema = EntidadeBase.ATER_SCHEMA)
public class PropriedadeRuralArquivo extends EntidadeBase implements _ChavePrimaria<Integer> {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "arquivo_id")
	private Arquivo arquivo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Enumerated(EnumType.STRING)
	private Confirmacao perfil;

	@ManyToOne
	@JoinColumn(name = "propriedade_rural_id")
	private PropriedadeRural propriedadeRural;

	public PropriedadeRuralArquivo() {

	}

	public PropriedadeRuralArquivo(PropriedadeRural propriedadeRural, Arquivo arquivo) {
		setPropriedadeRural(propriedadeRural);
		setArquivo(arquivo);
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public Confirmacao getPerfil() {
		return perfil;
	}

	public PropriedadeRural getPropriedadeRural() {
		return propriedadeRural;
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setPerfil(Confirmacao perfil) {
		this.perfil = perfil;
	}

	public void setPropriedadeRural(PropriedadeRural propriedadeRural) {
		this.propriedadeRural = propriedadeRural;
	}

}
