package br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural;

import static br.gov.df.emater.aterwebsrv.modelo.UtilitarioInfoBasica.infoBasicaReg;

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
import br.gov.df.emater.aterwebsrv.modelo.InfoBasica;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;

@Entity
@Table(name = "projeto_credito_rural_arquivo", schema = EntidadeBase.CREDITO_RURAL_SCHEMA)
public class ProjetoCreditoRuralArquivo extends EntidadeBase implements _ChavePrimaria<Integer>, InfoBasica<ProjetoCreditoRuralArquivo> {

	public enum Codigo {
		EVOLUCAO_REBANHO_BOVINO(44, 63), EVOLUCAO_REBANHO_OVINO(34, 55), EVOLUCAO_REBANHO_SUINO(34, 55);

		private int linhaDespesa;

		private int linhaReceita;

		private Codigo(int linhaDespesa, int linhaReceita) {
			this.linhaDespesa = linhaDespesa; // estes valores indicam a linha no arquivo excel onde serao encontrados os respectivos valores
			this.linhaReceita = linhaReceita;
		}

		public int getLinhaDespesa() {
			return this.linhaDespesa;
		}

		public int getLinhaReceita() {
			return this.linhaReceita;
		}
	}

	private static final long serialVersionUID = 1L;

	@ManyToOne()
	@JoinColumn(name = "arquivo_id")
	private Arquivo arquivo;

	@Enumerated(EnumType.STRING)
	private Codigo codigo;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne()
	@JoinColumn(name = "projeto_credito_rural_id")
	private ProjetoCreditoRural projetoCreditoRural;

	public ProjetoCreditoRuralArquivo() {
	}

	public ProjetoCreditoRuralArquivo(Integer id) {
		this.id = id;
	}

	public ProjetoCreditoRuralArquivo(Integer id, Arquivo arquivo, Codigo codigo) {
		this.id = id;
		this.arquivo = arquivo;
		this.codigo = codigo;
	}

	public Arquivo getArquivo() {
		return arquivo;
	}

	public Codigo getCodigo() {
		return codigo;
	}

	@Override
	public Integer getId() {
		return id;
	}

	public ProjetoCreditoRural getProjetoCreditoRural() {
		return projetoCreditoRural;
	}

	@Override
	public ProjetoCreditoRuralArquivo infoBasica() {
		return new ProjetoCreditoRuralArquivo(this.id, infoBasicaReg(this.arquivo), this.codigo);
	}

	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}

	public void setCodigo(Codigo codigo) {
		this.codigo = codigo;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void setProjetoCreditoRural(ProjetoCreditoRural projetoCreditoRural) {
		this.projetoCreditoRural = projetoCreditoRural;
	}

}
