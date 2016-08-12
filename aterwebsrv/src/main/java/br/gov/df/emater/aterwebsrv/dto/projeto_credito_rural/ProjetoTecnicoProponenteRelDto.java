package br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural;

import java.math.BigDecimal;
import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.Dto;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Coleta;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Email;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Telefone;

public class ProjetoTecnicoProponenteRelDto implements Dto {

	private static final long serialVersionUID = 1L;

	private Coleta beneficioSocialForcaTrabalhoColeta;

	private Telefone celular;

	private PessoaFisica conjuge;

	private List<DividaExistenteRelDto> dividaExistenteList;

	private Email email;

	private Endereco endereco;

	private BigDecimal forcaTrabalhoEventual;

	private BigDecimal forcaTrabalhoSalarioMensal;

	private BigDecimal forcaTrabalhoTrabalhadorPermanente;

	private PessoaFisica mae;

	private PessoaFisica pai;

	private BigDecimal patrimonioBenfeitorias;

	private BigDecimal patrimonioDividas;

	private Coleta patrimonioEDividasColeta;

	private List<RelacaoItemRelDto> patrimonioMaquinaEquipamentoList;

	private BigDecimal patrimonioMaquinasEquipamento;

	private List<RelacaoItemRelDto> patrimonioOutroBemList;

	private BigDecimal patrimonioOutros;

	private List<RelacaoItemRelDto> patrimonioSemoventeList;

	private BigDecimal patrimonioSemoventes;

	private BigDecimal patrimonioTerras;

	private BigDecimal patrimonioTotal;

	private String principalAtividadeProdutiva;

	private PessoaFisica proponente;

	private List<PublicoAlvoPropriedadeRural> propriedadeList;

	private PublicoAlvo publicoAlvo;

	private BigDecimal rendaBrutaAnualAssalariadoPercentual;

	private BigDecimal rendaBrutaAnualAssalariadoValor;

	private BigDecimal rendaBrutaAnualOutrasRendasPercentual;

	private BigDecimal rendaBrutaAnualOutrasRendasValor;

	private BigDecimal rendaBrutaAnualPropriedadePercentual;

	private BigDecimal rendaBrutaAnualPropriedadeValor;

	private BigDecimal rendaBrutaAnualTotalPercentual;

	private BigDecimal rendaBrutaAnualTotalValor;

	private Telefone telefone;

	public Coleta getBeneficioSocialForcaTrabalhoColeta() {
		return beneficioSocialForcaTrabalhoColeta;
	}

	public Telefone getCelular() {
		return celular;
	}

	public PessoaFisica getConjuge() {
		return conjuge;
	}

	public List<DividaExistenteRelDto> getDividaExistenteList() {
		return dividaExistenteList;
	}

	public Email getEmail() {
		return email;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public BigDecimal getForcaTrabalhoEventual() {
		return forcaTrabalhoEventual;
	}

	public BigDecimal getForcaTrabalhoSalarioMensal() {
		return forcaTrabalhoSalarioMensal;
	}

	public BigDecimal getForcaTrabalhoTrabalhadorPermanente() {
		return forcaTrabalhoTrabalhadorPermanente;
	}

	public PessoaFisica getMae() {
		return mae;
	}

	public PessoaFisica getPai() {
		return pai;
	}

	public BigDecimal getPatrimonioBenfeitorias() {
		return patrimonioBenfeitorias;
	}

	public BigDecimal getPatrimonioDividas() {
		return patrimonioDividas;
	}

	public Coleta getPatrimonioEDividasColeta() {
		return patrimonioEDividasColeta;
	}

	public List<RelacaoItemRelDto> getPatrimonioMaquinaEquipamentoList() {
		return patrimonioMaquinaEquipamentoList;
	}

	public BigDecimal getPatrimonioMaquinasEquipamento() {
		return patrimonioMaquinasEquipamento;
	}

	public List<RelacaoItemRelDto> getPatrimonioOutroBemList() {
		return patrimonioOutroBemList;
	}

	public BigDecimal getPatrimonioOutros() {
		return patrimonioOutros;
	}

	public List<RelacaoItemRelDto> getPatrimonioSemoventeList() {
		return patrimonioSemoventeList;
	}

	public BigDecimal getPatrimonioSemoventes() {
		return patrimonioSemoventes;
	}

	public BigDecimal getPatrimonioTerras() {
		return patrimonioTerras;
	}

	public BigDecimal getPatrimonioTotal() {
		return patrimonioTotal;
	}

	public String getPrincipalAtividadeProdutiva() {
		return principalAtividadeProdutiva;
	}

	public PessoaFisica getProponente() {
		return proponente;
	}

	public List<PublicoAlvoPropriedadeRural> getPropriedadeList() {
		return propriedadeList;
	}

	public PublicoAlvo getPublicoAlvo() {
		return publicoAlvo;
	}

	public BigDecimal getRendaBrutaAnualAssalariadoPercentual() {
		return rendaBrutaAnualAssalariadoPercentual;
	}

	public BigDecimal getRendaBrutaAnualAssalariadoValor() {
		return rendaBrutaAnualAssalariadoValor;
	}

	public BigDecimal getRendaBrutaAnualOutrasRendasPercentual() {
		return rendaBrutaAnualOutrasRendasPercentual;
	}

	public BigDecimal getRendaBrutaAnualOutrasRendasValor() {
		return rendaBrutaAnualOutrasRendasValor;
	}

	public BigDecimal getRendaBrutaAnualPropriedadePercentual() {
		return rendaBrutaAnualPropriedadePercentual;
	}

	public BigDecimal getRendaBrutaAnualPropriedadeValor() {
		return rendaBrutaAnualPropriedadeValor;
	}

	public BigDecimal getRendaBrutaAnualTotalPercentual() {
		return rendaBrutaAnualTotalPercentual;
	}

	public BigDecimal getRendaBrutaAnualTotalValor() {
		return rendaBrutaAnualTotalValor;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setBeneficioSocialForcaTrabalhoColeta(Coleta beneficioSocialForcaTrabalhoColeta) {
		this.beneficioSocialForcaTrabalhoColeta = beneficioSocialForcaTrabalhoColeta;
	}

	public void setCelular(Telefone celular) {
		this.celular = celular;
	}

	public void setConjuge(PessoaFisica conjuge) {
		this.conjuge = conjuge;
	}

	public void setDividaExistenteList(List<DividaExistenteRelDto> dividaExistenteList) {
		this.dividaExistenteList = dividaExistenteList;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public void setForcaTrabalhoEventual(BigDecimal forcaTrabalhoEventual) {
		this.forcaTrabalhoEventual = forcaTrabalhoEventual;
	}

	public void setForcaTrabalhoSalarioMensal(BigDecimal forcaTrabalhoSalarioMensal) {
		this.forcaTrabalhoSalarioMensal = forcaTrabalhoSalarioMensal;
	}

	public void setForcaTrabalhoTrabalhadorPermanente(BigDecimal forcaTrabalhoTrabalhadorPermanente) {
		this.forcaTrabalhoTrabalhadorPermanente = forcaTrabalhoTrabalhadorPermanente;
	}

	public void setMae(PessoaFisica mae) {
		this.mae = mae;
	}

	public void setPai(PessoaFisica pai) {
		this.pai = pai;
	}

	public void setPatrimonioBenfeitorias(BigDecimal patrimonioBenfeitorias) {
		this.patrimonioBenfeitorias = patrimonioBenfeitorias;
	}

	public void setPatrimonioDividas(BigDecimal patrimonioDividas) {
		this.patrimonioDividas = patrimonioDividas;
	}

	public void setPatrimonioEDividasColeta(Coleta patrimonioEDividasColeta) {
		this.patrimonioEDividasColeta = patrimonioEDividasColeta;
	}

	public void setPatrimonioMaquinaEquipamentoList(List<RelacaoItemRelDto> patrimonioMaquinaEquipamentoList) {
		this.patrimonioMaquinaEquipamentoList = patrimonioMaquinaEquipamentoList;
	}

	public void setPatrimonioMaquinasEquipamento(BigDecimal patrimonioMaquinasEquipamento) {
		this.patrimonioMaquinasEquipamento = patrimonioMaquinasEquipamento;
	}

	public void setPatrimonioOutroBemList(List<RelacaoItemRelDto> patrimonioOutroBemList) {
		this.patrimonioOutroBemList = patrimonioOutroBemList;
	}

	public void setPatrimonioOutros(BigDecimal patrimonioOutros) {
		this.patrimonioOutros = patrimonioOutros;
	}

	public void setPatrimonioSemoventeList(List<RelacaoItemRelDto> patrimonioSemoventeList) {
		this.patrimonioSemoventeList = patrimonioSemoventeList;
	}

	public void setPatrimonioSemoventes(BigDecimal patrimonioSemoventes) {
		this.patrimonioSemoventes = patrimonioSemoventes;
	}

	public void setPatrimonioTerras(BigDecimal patrimonioTerras) {
		this.patrimonioTerras = patrimonioTerras;
	}

	public void setPatrimonioTotal(BigDecimal patrimonioTotal) {
		this.patrimonioTotal = patrimonioTotal;
	}

	public void setPrincipalAtividadeProdutiva(String principalAtividadeProdutiva) {
		this.principalAtividadeProdutiva = principalAtividadeProdutiva;
	}

	public void setProponente(PessoaFisica proponente) {
		this.proponente = proponente;
	}

	public void setPropriedadeList(List<PublicoAlvoPropriedadeRural> propriedadeList) {
		this.propriedadeList = propriedadeList;
	}

	public void setPublicoAlvo(PublicoAlvo publicoAlvo) {
		this.publicoAlvo = publicoAlvo;
	}

	public void setRendaBrutaAnualAssalariadoPercentual(BigDecimal rendaBrutaAnualAssalariadoPercentual) {
		this.rendaBrutaAnualAssalariadoPercentual = rendaBrutaAnualAssalariadoPercentual;
	}

	public void setRendaBrutaAnualAssalariadoValor(BigDecimal rendaBrutaAnualAssalariadoValor) {
		this.rendaBrutaAnualAssalariadoValor = rendaBrutaAnualAssalariadoValor;
	}

	public void setRendaBrutaAnualOutrasRendasPercentual(BigDecimal rendaBrutaAnualOutrasRendasPercentual) {
		this.rendaBrutaAnualOutrasRendasPercentual = rendaBrutaAnualOutrasRendasPercentual;
	}

	public void setRendaBrutaAnualOutrasRendasValor(BigDecimal rendaBrutaAnualOutrasRendasValor) {
		this.rendaBrutaAnualOutrasRendasValor = rendaBrutaAnualOutrasRendasValor;
	}

	public void setRendaBrutaAnualPropriedadePercentual(BigDecimal rendaBrutaAnualPropriedadePercentual) {
		this.rendaBrutaAnualPropriedadePercentual = rendaBrutaAnualPropriedadePercentual;
	}

	public void setRendaBrutaAnualPropriedadeValor(BigDecimal rendaBrutaAnualPropriedadeValor) {
		this.rendaBrutaAnualPropriedadeValor = rendaBrutaAnualPropriedadeValor;
	}

	public void setRendaBrutaAnualTotalPercentual(BigDecimal rendaBrutaAnualTotalPercentual) {
		this.rendaBrutaAnualTotalPercentual = rendaBrutaAnualTotalPercentual;
	}

	public void setRendaBrutaAnualTotalValor(BigDecimal rendaBrutaAnualTotalValor) {
		this.rendaBrutaAnualTotalValor = rendaBrutaAnualTotalValor;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

}
