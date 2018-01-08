package br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural;

import java.math.BigDecimal;
import java.util.Calendar;

import br.gov.df.emater.aterwebsrv.dto.Dto;
import br.gov.df.emater.aterwebsrv.modelo.dominio.EstadoCivil;
import br.gov.df.emater.aterwebsrv.modelo.dominio.GarantiaParticipacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Profissao;

public class ProjetoTecnicoGarantiaAvalistaRelDto implements Dto {

	private static final long serialVersionUID = 1L;

	private String cep;

	private String cpfCnpj;

	private EstadoCivil estadoCivil;

	private String identidadeNumero;

	private String identidadeOrgao;

	private String localidade;

	private String logradouro;

	private String maeNome;

	private Calendar nascimento;

	private String naturalidade;

	private String nome;

	private String paiNome;

	private GarantiaParticipacao participacao;

	private Profissao profissao;

	private BigDecimal rendaLiquida;

	private String telefoneCelular;
	
	private String conjuge;

	public String getCep() {
		return cep;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public String getIdentidadeNumero() {
		return identidadeNumero;
	}

	public String getIdentidadeOrgao() {
		return identidadeOrgao;
	}

	public String getLocalidade() {
		return localidade;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public String getMaeNome() {
		return maeNome;
	}

	public Calendar getNascimento() {
		return nascimento;
	}

	public String getNaturalidade() {
		return naturalidade;
	}

	public String getNome() {
		return nome;
	}

	public String getPaiNome() {
		return paiNome;
	}

	public GarantiaParticipacao getParticipacao() {
		return participacao;
	}

	public Profissao getProfissao() {
		return profissao;
	}

	public BigDecimal getRendaLiquida() {
		return rendaLiquida;
	}

	public String getTelefoneCelular() {
		return telefoneCelular;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public void setIdentidadeNumero(String identidadeNumero) {
		this.identidadeNumero = identidadeNumero;
	}

	public void setIdentidadeOrgao(String identidadeOrgao) {
		this.identidadeOrgao = identidadeOrgao;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public void setMaeNome(String maeNome) {
		this.maeNome = maeNome;
	}

	public void setNascimento(Calendar nascimento) {
		this.nascimento = nascimento;
	}

	public void setNaturalidade(String naturalidade) {
		this.naturalidade = naturalidade;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPaiNome(String paiNome) {
		this.paiNome = paiNome;
	}

	public void setParticipacao(GarantiaParticipacao participacao) {
		this.participacao = participacao;
	}

	public void setProfissao(Profissao profissao) {
		this.profissao = profissao;
	}

	public void setRendaLiquida(BigDecimal rendaLiquida) {
		this.rendaLiquida = rendaLiquida;
	}

	public void setTelefoneCelular(String telefoneCelular) {
		this.telefoneCelular = telefoneCelular;
	}

	public String getConjuge() {
		return conjuge;
	}

	public void setConjuge(String conjuge) {
		this.conjuge = conjuge;
	}

}
