package br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural;

import java.math.BigDecimal;
import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.Dto;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Coleta;

public class ProjetoTecnicoPropriedadeRuralRelDto implements Dto {

	private static final long serialVersionUID = 1L;

	private BigDecimal areasIrrigadasAreaTotal;

	private BigDecimal areasIrrigadasAspersaoConvencional;

	private BigDecimal areasIrrigadasAutoPropelido;

	private BigDecimal areasIrrigadasGotejamento;

	private BigDecimal areasIrrigadasMicroAspersao;

	private BigDecimal areasIrrigadasOutros;

	private BigDecimal areasIrrigadasPivoCentral;

	private BigDecimal areasIrrigadasSuperficie;

	private Coleta avaliacaoDaPropriedadeColeta;

	private List<RelacaoItemRelDto> benfeitoriasList;

	private String fonteDAguaPrincipal;

	private String fonteDAguaSecundaria;

	private BigDecimal latitude;

	private BigDecimal longitude;

	private BigDecimal maoDeObraContratada;

	private BigDecimal maoDeObraFamiliar;

	private BigDecimal maoDeObraTemporaria;

	private BigDecimal moradoresDaPropriedadeFamilias;

	private BigDecimal moradoresDaPropriedadePessoas;

	private PublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural;

	private BigDecimal usoDoSoloAreaTotal;

	private BigDecimal usoDoSoloBenfeitoriasArea;

	private BigDecimal usoDoSoloBenfeitoriasValorUnitario;

	private BigDecimal usoDoSoloCulturasPerenesArea;

	private BigDecimal usoDoSoloCulturasPerenesValorUnitario;

	private BigDecimal usoDoSoloCulturasTemporariasArea;

	private BigDecimal usoDoSoloCulturasTemporariasValorUnitario;

	private BigDecimal usoDoSoloOutrasArea;

	private BigDecimal usoDoSoloOutrasValorUnitario;

	private BigDecimal usoDoSoloPastagensArea;

	private BigDecimal usoDoSoloPastagensValorUnitario;

	private BigDecimal usoDoSoloPreservacaoPermanenteArea;

	private BigDecimal usoDoSoloPreservacaoPermanenteValorUnitario;

	private BigDecimal usoDoSoloReservaLegalArea;

	private BigDecimal usoDoSoloReservaLegalValorUnitario;

	private BigDecimal usoDoSoloValorTotal;

	private BigDecimal vazaoLSPrincipal;

	private BigDecimal vazaoLSSecundaria;

	public BigDecimal getAreasIrrigadasAreaTotal() {
		return areasIrrigadasAreaTotal;
	}

	public BigDecimal getAreasIrrigadasAspersaoConvencional() {
		return areasIrrigadasAspersaoConvencional;
	}

	public BigDecimal getAreasIrrigadasAutoPropelido() {
		return areasIrrigadasAutoPropelido;
	}

	public BigDecimal getAreasIrrigadasGotejamento() {
		return areasIrrigadasGotejamento;
	}

	public BigDecimal getAreasIrrigadasMicroAspersao() {
		return areasIrrigadasMicroAspersao;
	}

	public BigDecimal getAreasIrrigadasOutros() {
		return areasIrrigadasOutros;
	}

	public BigDecimal getAreasIrrigadasPivoCentral() {
		return areasIrrigadasPivoCentral;
	}

	public BigDecimal getAreasIrrigadasSuperficie() {
		return areasIrrigadasSuperficie;
	}

	public Coleta getAvaliacaoDaPropriedadeColeta() {
		return avaliacaoDaPropriedadeColeta;
	}

	public List<RelacaoItemRelDto> getBenfeitoriasList() {
		return benfeitoriasList;
	}

	public String getFonteDAguaPrincipal() {
		return fonteDAguaPrincipal;
	}

	public String getFonteDAguaSecundaria() {
		return fonteDAguaSecundaria;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public BigDecimal getMaoDeObraContratada() {
		return maoDeObraContratada;
	}

	public BigDecimal getMaoDeObraFamiliar() {
		return maoDeObraFamiliar;
	}

	public BigDecimal getMaoDeObraTemporaria() {
		return maoDeObraTemporaria;
	}

	public BigDecimal getMoradoresDaPropriedadeFamilias() {
		return moradoresDaPropriedadeFamilias;
	}

	public BigDecimal getMoradoresDaPropriedadePessoas() {
		return moradoresDaPropriedadePessoas;
	}

	public PublicoAlvoPropriedadeRural getPublicoAlvoPropriedadeRural() {
		return publicoAlvoPropriedadeRural;
	}

	public BigDecimal getUsoDoSoloAreaTotal() {
		return usoDoSoloAreaTotal;
	}

	public BigDecimal getUsoDoSoloBenfeitoriasArea() {
		return usoDoSoloBenfeitoriasArea;
	}

	public BigDecimal getUsoDoSoloBenfeitoriasValorUnitario() {
		return usoDoSoloBenfeitoriasValorUnitario;
	}

	public BigDecimal getUsoDoSoloCulturasPerenesArea() {
		return usoDoSoloCulturasPerenesArea;
	}

	public BigDecimal getUsoDoSoloCulturasPerenesValorUnitario() {
		return usoDoSoloCulturasPerenesValorUnitario;
	}

	public BigDecimal getUsoDoSoloCulturasTemporariasArea() {
		return usoDoSoloCulturasTemporariasArea;
	}

	public BigDecimal getUsoDoSoloCulturasTemporariasValorUnitario() {
		return usoDoSoloCulturasTemporariasValorUnitario;
	}

	public BigDecimal getUsoDoSoloOutrasArea() {
		return usoDoSoloOutrasArea;
	}

	public BigDecimal getUsoDoSoloOutrasValorUnitario() {
		return usoDoSoloOutrasValorUnitario;
	}

	public BigDecimal getUsoDoSoloPastagensArea() {
		return usoDoSoloPastagensArea;
	}

	public BigDecimal getUsoDoSoloPastagensValorUnitario() {
		return usoDoSoloPastagensValorUnitario;
	}

	public BigDecimal getUsoDoSoloPreservacaoPermanenteArea() {
		return usoDoSoloPreservacaoPermanenteArea;
	}

	public BigDecimal getUsoDoSoloPreservacaoPermanenteValorUnitario() {
		return usoDoSoloPreservacaoPermanenteValorUnitario;
	}

	public BigDecimal getUsoDoSoloReservaLegalArea() {
		return usoDoSoloReservaLegalArea;
	}

	public BigDecimal getUsoDoSoloReservaLegalValorUnitario() {
		return usoDoSoloReservaLegalValorUnitario;
	}

	public BigDecimal getUsoDoSoloValorTotal() {
		return usoDoSoloValorTotal;
	}

	public BigDecimal getVazaoLSPrincipal() {
		return vazaoLSPrincipal;
	}

	public BigDecimal getVazaoLSSecundaria() {
		return vazaoLSSecundaria;
	}

	public void setAreasIrrigadasAreaTotal(BigDecimal areasIrrigadasAreaTotal) {
		this.areasIrrigadasAreaTotal = areasIrrigadasAreaTotal;
	}

	public void setAreasIrrigadasAspersaoConvencional(BigDecimal areasIrrigadasAspersaoConvencional) {
		this.areasIrrigadasAspersaoConvencional = areasIrrigadasAspersaoConvencional;
	}

	public void setAreasIrrigadasAutoPropelido(BigDecimal areasIrrigadasAutoPropelido) {
		this.areasIrrigadasAutoPropelido = areasIrrigadasAutoPropelido;
	}

	public void setAreasIrrigadasGotejamento(BigDecimal areasIrrigadasGotejamento) {
		this.areasIrrigadasGotejamento = areasIrrigadasGotejamento;
	}

	public void setAreasIrrigadasMicroAspersao(BigDecimal areasIrrigadasMicroAspersao) {
		this.areasIrrigadasMicroAspersao = areasIrrigadasMicroAspersao;
	}

	public void setAreasIrrigadasOutros(BigDecimal areasIrrigadasOutros) {
		this.areasIrrigadasOutros = areasIrrigadasOutros;
	}

	public void setAreasIrrigadasPivoCentral(BigDecimal areasIrrigadasPivoCentral) {
		this.areasIrrigadasPivoCentral = areasIrrigadasPivoCentral;
	}

	public void setAreasIrrigadasSuperficie(BigDecimal areasIrrigadasSuperficie) {
		this.areasIrrigadasSuperficie = areasIrrigadasSuperficie;
	}

	public void setAvaliacaoDaPropriedadeColeta(Coleta avaliacaoDaPropriedadeColeta) {
		this.avaliacaoDaPropriedadeColeta = avaliacaoDaPropriedadeColeta;
	}

	public void setBenfeitoriasList(List<RelacaoItemRelDto> benfeitoriasList) {
		this.benfeitoriasList = benfeitoriasList;
	}

	public void setFonteDAguaPrincipal(String fonteDAguaPrincipal) {
		this.fonteDAguaPrincipal = fonteDAguaPrincipal;
	}

	public void setFonteDAguaSecundaria(String fonteDAguaSecundaria) {
		this.fonteDAguaSecundaria = fonteDAguaSecundaria;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public void setMaoDeObraContratada(BigDecimal maoDeObraContratada) {
		this.maoDeObraContratada = maoDeObraContratada;
	}

	public void setMaoDeObraFamiliar(BigDecimal maoDeObraFamiliar) {
		this.maoDeObraFamiliar = maoDeObraFamiliar;
	}

	public void setMaoDeObraTemporaria(BigDecimal maoDeObraTemporaria) {
		this.maoDeObraTemporaria = maoDeObraTemporaria;
	}

	public void setMoradoresDaPropriedadeFamilias(BigDecimal moradoresDaPropriedadeFamilias) {
		this.moradoresDaPropriedadeFamilias = moradoresDaPropriedadeFamilias;
	}

	public void setMoradoresDaPropriedadePessoas(BigDecimal moradoresDaPropriedadePessoas) {
		this.moradoresDaPropriedadePessoas = moradoresDaPropriedadePessoas;
	}

	public void setPublicoAlvoPropriedadeRural(PublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural) {
		this.publicoAlvoPropriedadeRural = publicoAlvoPropriedadeRural;
	}

	public void setUsoDoSoloAreaTotal(BigDecimal usoDoSoloAreaTotal) {
		this.usoDoSoloAreaTotal = usoDoSoloAreaTotal;
	}

	public void setUsoDoSoloBenfeitoriasArea(BigDecimal usoDoSoloBenfeitoriasArea) {
		this.usoDoSoloBenfeitoriasArea = usoDoSoloBenfeitoriasArea;
	}

	public void setUsoDoSoloBenfeitoriasValorUnitario(BigDecimal usoDoSoloBenfeitoriasValorUnitario) {
		this.usoDoSoloBenfeitoriasValorUnitario = usoDoSoloBenfeitoriasValorUnitario;
	}

	public void setUsoDoSoloCulturasPerenesArea(BigDecimal usoDoSoloCulturasPerenesArea) {
		this.usoDoSoloCulturasPerenesArea = usoDoSoloCulturasPerenesArea;
	}

	public void setUsoDoSoloCulturasPerenesValorUnitario(BigDecimal usoDoSoloCulturasPerenesValorUnitario) {
		this.usoDoSoloCulturasPerenesValorUnitario = usoDoSoloCulturasPerenesValorUnitario;
	}

	public void setUsoDoSoloCulturasTemporariasArea(BigDecimal usoDoSoloCulturasTemporariasArea) {
		this.usoDoSoloCulturasTemporariasArea = usoDoSoloCulturasTemporariasArea;
	}

	public void setUsoDoSoloCulturasTemporariasValorUnitario(BigDecimal usoDoSoloCulturasTemporariasValorUnitario) {
		this.usoDoSoloCulturasTemporariasValorUnitario = usoDoSoloCulturasTemporariasValorUnitario;
	}

	public void setUsoDoSoloOutrasArea(BigDecimal usoDoSoloOutrasArea) {
		this.usoDoSoloOutrasArea = usoDoSoloOutrasArea;
	}

	public void setUsoDoSoloOutrasValorUnitario(BigDecimal usoDoSoloOutrasValorUnitario) {
		this.usoDoSoloOutrasValorUnitario = usoDoSoloOutrasValorUnitario;
	}

	public void setUsoDoSoloPastagensArea(BigDecimal usoDoSoloPastagensArea) {
		this.usoDoSoloPastagensArea = usoDoSoloPastagensArea;
	}

	public void setUsoDoSoloPastagensValorUnitario(BigDecimal usoDoSoloPastagensValorUnitario) {
		this.usoDoSoloPastagensValorUnitario = usoDoSoloPastagensValorUnitario;
	}

	public void setUsoDoSoloPreservacaoPermanenteArea(BigDecimal usoDoSoloPreservacaoPermanenteArea) {
		this.usoDoSoloPreservacaoPermanenteArea = usoDoSoloPreservacaoPermanenteArea;
	}

	public void setUsoDoSoloPreservacaoPermanenteValorUnitario(BigDecimal usoDoSoloPreservacaoPermanenteValorUnitario) {
		this.usoDoSoloPreservacaoPermanenteValorUnitario = usoDoSoloPreservacaoPermanenteValorUnitario;
	}

	public void setUsoDoSoloReservaLegalArea(BigDecimal usoDoSoloReservaLegalArea) {
		this.usoDoSoloReservaLegalArea = usoDoSoloReservaLegalArea;
	}

	public void setUsoDoSoloReservaLegalValorUnitario(BigDecimal usoDoSoloReservaLegalValorUnitario) {
		this.usoDoSoloReservaLegalValorUnitario = usoDoSoloReservaLegalValorUnitario;
	}

	public void setUsoDoSoloValorTotal(BigDecimal usoDoSoloValorTotal) {
		this.usoDoSoloValorTotal = usoDoSoloValorTotal;
	}

	public void setVazaoLSPrincipal(BigDecimal vazaoLSPrincipal) {
		this.vazaoLSPrincipal = vazaoLSPrincipal;
	}

	public void setVazaoLSSecundaria(BigDecimal vazaoLSSecundaria) {
		this.vazaoLSSecundaria = vazaoLSSecundaria;
	}

}
