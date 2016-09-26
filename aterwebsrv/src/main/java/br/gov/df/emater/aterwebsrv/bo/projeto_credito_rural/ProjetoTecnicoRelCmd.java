package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vividsolutions.jts.geom.Coordinate;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.RelacionamentoFuncaoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.RelacionamentoTipoDao;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoRuralDao;
import br.gov.df.emater.aterwebsrv.dto.Dto;
import br.gov.df.emater.aterwebsrv.dto.formulario.FormularioColetaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.DividaExistenteRelDto;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoTecnicoFluxoCaixaDto;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoTecnicoFluxoCaixaItemDto;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoTecnicoGarantiaAvalistaRelDto;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoTecnicoGarantiaRelDto;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoTecnicoProponenteRelDto;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoTecnicoPropriedadeRuralRelDto;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.RelacaoItemRelDto;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioData;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioPdf;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoSetor;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FluxoCaixaTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FormularioDestino;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Situacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.TelefoneTipo;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Coleta;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Email;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEmail;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEndereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaRelacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaTelefone;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoFuncao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoTipo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Telefone;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.CustoProducao;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.FluxoCaixaAno;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralArquivo;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralFinanciamento;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralFluxoCaixa;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralGarantia;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralPublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralReceitaDespesa;
import br.gov.df.emater.aterwebsrv.relatorio._Relatorio;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.type.OrientationEnum;

@Service("ProjetoCreditoRuralProjetoTecnicoRelCmd")
public class ProjetoTecnicoRelCmd extends _Comando {

	@Autowired
	private ProjetoCreditoRuralDao dao;

	@Autowired
	private FacadeBo facadeBo;

	private RelacionamentoFuncao funcaoConjuge;

	private RelacionamentoFuncao funcaoPaiMae;

	@Autowired
	private RelacionamentoFuncaoDao relacionamentoFuncaoDao;

	private RelacionamentoTipo relacionamentoTipo;

	@Autowired
	private RelacionamentoTipoDao relacionamentoTipoDao;

	@Autowired
	private _Relatorio relatorio;

	public ProjetoTecnicoRelCmd() {
	}

	@SuppressWarnings("unchecked")
	private List<ProjetoTecnicoProponenteRelDto> captarCadastroProponente(_Contexto contexto, List<ProjetoCreditoRural> pList) throws BoException {
		try {
			List<ProjetoTecnicoProponenteRelDto> result = new ArrayList<>();
			ProjetoTecnicoProponenteRelDto reg = new ProjetoTecnicoProponenteRelDto();

			ProjetoCreditoRural pcr = pList.get(0);

			PessoaFisica proponente = (PessoaFisica) pcr.getPublicoAlvo().getPessoa();
			reg.setProponente(proponente);
			reg.setPublicoAlvo(pcr.getPublicoAlvo());

			PessoaFisica pai = null;
			PessoaFisica mae = null;
			PessoaFisica conjuge = null;
			iniciarRelacionamento();
			if (!CollectionUtils.isEmpty(proponente.getRelacionamentoList())) {
				for (PessoaRelacionamento pessoaRelacionamento : proponente.getRelacionamentoList()) {
					if (this.relacionamentoTipo.getId().equals(pessoaRelacionamento.getRelacionamento().getRelacionamentoTipo().getId())) {
						if (this.funcaoConjuge.getId().equals(pessoaRelacionamento.getRelacionamentoFuncao().getId())) {
							if (pessoaRelacionamento.getPessoa() != null) {
								conjuge = (PessoaFisica) pessoaRelacionamento.getPessoa();
							} else {
								conjuge = new PessoaFisica();
								conjuge.setNome(pessoaRelacionamento.getNome());
								conjuge.setGenero(pessoaRelacionamento.getGenero());
								conjuge.setCpf(pessoaRelacionamento.getCpf());
								conjuge.setRgNumero(pessoaRelacionamento.getRgNumero());
								conjuge.setRgOrgaoEmissor(pessoaRelacionamento.getRgOrgaoEmissor());
								conjuge.setRgDataEmissao(pessoaRelacionamento.getRgDataEmissao());
								conjuge.setNascimentoMunicipio(pessoaRelacionamento.getNascimentoMunicipio());
								conjuge.setNascimentoEstado(pessoaRelacionamento.getNascimentoEstado());
								conjuge.setNascimentoPais(pessoaRelacionamento.getNascimentoPais());
								conjuge.setProfissao(pessoaRelacionamento.getProfissao());
							}
						} else if (this.funcaoPaiMae.getId().equals(pessoaRelacionamento.getRelacionamentoFuncao().getId())) {
							if (pessoaRelacionamento.getPessoa() != null) {
								if (PessoaGenero.M.equals(((PessoaFisica) pessoaRelacionamento.getPessoa()).getGenero())) {
									pai = (PessoaFisica) pessoaRelacionamento.getPessoa();
								} else {
									mae = (PessoaFisica) pessoaRelacionamento.getPessoa();
								}
							} else {
								if (PessoaGenero.M.equals(pessoaRelacionamento.getGenero())) {
									pai = new PessoaFisica();
									pai.setNome(pessoaRelacionamento.getNome());
									pai.setGenero(PessoaGenero.M);
								} else {
									mae = new PessoaFisica();
									mae.setNome(pessoaRelacionamento.getNome());
									mae.setGenero(PessoaGenero.F);
								}
							}
						}
					}
				}
			}
			reg.setPai(pai);
			reg.setMae(mae);
			reg.setConjuge(conjuge);

			// identificar o endereço
			Endereco endereco = null;
			if (!CollectionUtils.isEmpty(proponente.getEnderecoList())) {
				for (PessoaEndereco pessoaEndereco : proponente.getEnderecoList()) {
					if (Confirmacao.S.equals(pessoaEndereco.getPrincipal())) {
						endereco = pessoaEndereco.getEndereco();
						break;
					}
				}
			}
			if (endereco == null && pcr.getPublicoAlvo() != null && !CollectionUtils.isEmpty(pcr.getPublicoAlvo().getPublicoAlvoPropriedadeRuralList())) {
				for (PublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural : pcr.getPublicoAlvo().getPublicoAlvoPropriedadeRuralList()) {
					if (Confirmacao.S.equals(publicoAlvoPropriedadeRural.getPrincipal()) && publicoAlvoPropriedadeRural.getPropriedadeRural() != null) {
						endereco = publicoAlvoPropriedadeRural.getPropriedadeRural().getEndereco();
						break;
					}
				}
			}
			if (endereco == null) {
				throw new BoException("Endereço não informado!");
			}
			reg.setEndereco(endereco);

			Telefone telefone = null;
			Telefone celular = null;
			if (!CollectionUtils.isEmpty(proponente.getTelefoneList())) {
				for (PessoaTelefone pessoaTelefone : proponente.getTelefoneList()) {
					if (TelefoneTipo.CE.equals(pessoaTelefone.getTipo())) {
						celular = pessoaTelefone.getTelefone();
					}
					if (TelefoneTipo.TF.equals(pessoaTelefone.getTipo())) {
						if (Confirmacao.S.equals(pessoaTelefone.getPrincipal())) {
							telefone = pessoaTelefone.getTelefone();
						}
					}
				}
			}
			reg.setTelefone(telefone);
			reg.setCelular(celular);

			Email email = null;
			if (!CollectionUtils.isEmpty(proponente.getEmailList())) {
				for (PessoaEmail pessoaEmail : proponente.getEmailList()) {
					if (Confirmacao.S.equals(pessoaEmail.getPrincipal())) {
						email = pessoaEmail.getEmail();
						break;
					}
				}
			}
			reg.setEmail(email);

			List<String> principalAtividadeProdutiva = new ArrayList<>();
			if (pcr.getPublicoAlvo() != null && !CollectionUtils.isEmpty(pcr.getPublicoAlvo().getPublicoAlvoSetorList())) {
				for (PublicoAlvoSetor pas : pcr.getPublicoAlvo().getPublicoAlvoSetorList()) {
					principalAtividadeProdutiva.add(pas.getSetor().getNome());
				}
			}
			reg.setPrincipalAtividadeProdutiva(UtilitarioString.collectionToString(principalAtividadeProdutiva));

			// recuperar os diagnósticos do proponente
			FormularioColetaCadFiltroDto filtro = new FormularioColetaCadFiltroDto();
			Set<FormularioDestino> destinoList = new HashSet<>();
			destinoList.add(FormularioDestino.PE);
			filtro.setDestino(destinoList);
			Set<Confirmacao> subFormularioList = new HashSet<>();
			subFormularioList.add(Confirmacao.N);
			filtro.setSubformulario(subFormularioList);
			Set<Situacao> situacaoList = new HashSet<>();
			situacaoList.add(Situacao.A);
			filtro.setSituacao(situacaoList);
			filtro.setPessoa(proponente);
			proponente.setDiagnosticoList((List<Object[]>) facadeBo.formularioColetaFiltroExecutar(contexto.getUsuario(), filtro).getResposta());

			// captar as últimas coletas dos diagnósticos
			captarUltimaColetaFormularios((List<Object[]>) proponente.getDiagnosticoList(), reg, "beneficioSocialForcaTrabalho", "patrimonioDivida");

			// DADOS SOCIO-ECONÔMICOS
			processarDiagnosticoProponente(reg);

			// captar dados das propriedades informadas no crédito
			reg.setPatrimonioTerras(new BigDecimal("0"));
			reg.setPatrimonioBenfeitorias(new BigDecimal("0"));

			List<PublicoAlvoPropriedadeRural> propriedadeList = null;
			if (!CollectionUtils.isEmpty(pcr.getPublicoAlvoPropriedadeRuralList())) {
				propriedadeList = new ArrayList<>();
				for (ProjetoCreditoRuralPublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural : pcr.getPublicoAlvoPropriedadeRuralList()) {
					// recuperar os diagnósticos da propriedade
					PropriedadeRural pr = publicoAlvoPropriedadeRural.getPublicoAlvoPropriedadeRural().getPropriedadeRural();
					filtro = new FormularioColetaCadFiltroDto();
					destinoList = new HashSet<>();
					destinoList.add(FormularioDestino.PR);
					filtro.setDestino(destinoList);
					subFormularioList = new HashSet<>();
					subFormularioList.add(Confirmacao.N);
					filtro.setSubformulario(subFormularioList);
					situacaoList = new HashSet<>();
					situacaoList.add(Situacao.A);
					filtro.setSituacao(situacaoList);
					filtro.setPropriedadeRural(pr);
					pr.setDiagnosticoList((List<Object[]>) facadeBo.formularioColetaFiltroExecutar(contexto.getUsuario(), filtro).getResposta());
					processarDiagnosticoPropriedade((List<Object[]>) pr.getDiagnosticoList(), reg);
					propriedadeList.add(publicoAlvoPropriedadeRural.getPublicoAlvoPropriedadeRural());
				}
			}
			reg.setPropriedadeList(propriedadeList);

			result.add(reg);
			return result;
		} catch (Exception e) {
			throw new BoException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private List<ProjetoTecnicoPropriedadeRuralRelDto> captarCadastroPropriedadeRural(_Contexto contexto, List<ProjetoCreditoRural> pList) throws BoException {
		try {
			List<ProjetoTecnicoPropriedadeRuralRelDto> result = new ArrayList<>();
			ProjetoCreditoRural pcr = pList.get(0);

			FormularioColetaCadFiltroDto filtro = new FormularioColetaCadFiltroDto();
			Set<FormularioDestino> destinoList = new HashSet<>();
			destinoList.add(FormularioDestino.PR);
			filtro.setDestino(destinoList);
			Set<Confirmacao> subFormularioList = new HashSet<>();
			subFormularioList.add(Confirmacao.N);
			filtro.setSubformulario(subFormularioList);
			Set<Situacao> situacaoList = new HashSet<>();
			situacaoList.add(Situacao.A);
			filtro.setSituacao(situacaoList);

			// captar propriedades
			for (ProjetoCreditoRuralPublicoAlvoPropriedadeRural pr : pcr.getPublicoAlvoPropriedadeRuralList()) {
				ProjetoTecnicoPropriedadeRuralRelDto reg = new ProjetoTecnicoPropriedadeRuralRelDto();
				reg.setPublicoAlvoPropriedadeRural(pr.getPublicoAlvoPropriedadeRural());

				Double latitude = null;
				Double longitude = null;
				if (pr.getPublicoAlvoPropriedadeRural().getPropriedadeRural().getEndereco().getEntradaPrincipal() != null) {
					latitude = pr.getPublicoAlvoPropriedadeRural().getPropriedadeRural().getEndereco().getEntradaPrincipal().getCoordinate().getOrdinate(Coordinate.X);
					longitude = pr.getPublicoAlvoPropriedadeRural().getPropriedadeRural().getEndereco().getEntradaPrincipal().getCoordinate().getOrdinate(Coordinate.Y);
				}
				reg.setLatitude(new BigDecimal(latitude == null ? "0" : latitude.toString()));
				reg.setLongitude(new BigDecimal(longitude == null ? "0" : longitude.toString()));

				// recuperar os diagnósticos do proponente
				filtro.setPropriedadeRural(pr.getPublicoAlvoPropriedadeRural().getPropriedadeRural());
				pr.getPublicoAlvoPropriedadeRural().getPropriedadeRural().setDiagnosticoList((List<Object[]>) facadeBo.formularioColetaFiltroExecutar(contexto.getUsuario(), filtro).getResposta());

				// captar as últimas coletas dos diagnósticos
				captarUltimaColetaFormularios((List<Object[]>) pr.getPublicoAlvoPropriedadeRural().getPropriedadeRural().getDiagnosticoList(), reg, "avaliacaoDaPropriedade");

				processarDiagnosticoPropriedadeRural(reg);

				result.add(reg);
			}

			return result;
		} catch (Exception e) {
			throw new BoException(e);
		}
	}

	// TODO criar
	private List<CustoProducao> captarCustoProducao(ProjetoCreditoRural projetoCreditoRural) {
		List<CustoProducao> result = null;

		Set<CustoProducao> items = new HashSet<>();

		// captar custo de produção do custeio do financiamento
		for (ProjetoCreditoRuralFinanciamento item : projetoCreditoRural.getCusteioList()) {
			if (item.getCustoProducao() != null && item.getCustoProducao().getId() != null) {
				items.add(item.getCustoProducao());
			}
		}

		// captar custo de produção do receita
		for (ProjetoCreditoRuralReceitaDespesa item : projetoCreditoRural.getReceitaList()) {
			if (item.getCustoProducao() != null && item.getCustoProducao().getId() != null) {
				items.add(item.getCustoProducao());
			}
		}

		// captar custo de produção do despesa
		for (ProjetoCreditoRuralReceitaDespesa item : projetoCreditoRural.getDespesaList()) {
			if (item.getCustoProducao() != null && item.getCustoProducao().getId() != null) {
				items.add(item.getCustoProducao());
			}
		}

		// se foi inserido
		if (items.size() > 0) {
			result = new ArrayList<>(items);
			result.sort((cp1, cp2) -> cp1.getNomeFormaProducao().compareTo(cp2.getNomeFormaProducao()));
		}

		return result;
	}

	private List<ProjetoTecnicoFluxoCaixaDto> captarFluxoCaixa(List<ProjetoCreditoRuralFluxoCaixa> fluxoCaixaList) throws Exception {
		List<ProjetoTecnicoFluxoCaixaDto> result = new ArrayList<>();
		ProjetoTecnicoFluxoCaixaDto ptfc = new ProjetoTecnicoFluxoCaixaDto();
		ptfc.setReceitaItemList(new ArrayList<>());
		ptfc.setDespesaItemList(new ArrayList<>());

		for (ProjetoCreditoRuralFluxoCaixa fluxoCaixa : fluxoCaixaList) {
			ProjetoTecnicoFluxoCaixaItemDto item = captarFluxoCaixaItem(fluxoCaixa);

			if (FluxoCaixaTipo.R.equals(fluxoCaixa.getTipo())) {
				ptfc.getReceitaItemList().add(item);
			} else if (FluxoCaixaTipo.D.equals(fluxoCaixa.getTipo())) {
				ptfc.getDespesaItemList().add(item);
			}

			switch (fluxoCaixa.getCodigo()) {
			case DESPESA_AMORTIZACAO_DIVIDAS_EXISTENTE:
				captarFluxoCaixaTotalizador(ptfc, "lucro", Confirmacao.N, item);
				captarFluxoCaixaTotalizador(ptfc, "amortizacao", Confirmacao.N, item);
				captarFluxoCaixaTotalizador(ptfc, "saldoDevedor", Confirmacao.N, item);
				captarFluxoCaixaTotalizador(ptfc, "fluxoLiquido", Confirmacao.N, item);
				break;
			case DESPESA_ATIVIDADE_AGROPECUARIA:
				captarFluxoCaixaTotalizador(ptfc, "lucro", Confirmacao.N, item);
				captarFluxoCaixaTotalizador(ptfc, "saldoDevedor", Confirmacao.N, item);
				captarFluxoCaixaTotalizador(ptfc, "fluxoLiquido", Confirmacao.N, item);
				break;
			case DESPESA_IMPOSTO_TAXA:
				captarFluxoCaixaTotalizador(ptfc, "lucro", Confirmacao.N, item);
				captarFluxoCaixaTotalizador(ptfc, "saldoDevedor", Confirmacao.N, item);
				captarFluxoCaixaTotalizador(ptfc, "fluxoLiquido", Confirmacao.N, item);
				break;
			case DESPESA_MANUTENCAO_BENFEITORIA_MAQUINA:
				captarFluxoCaixaTotalizador(ptfc, "lucro", Confirmacao.N, item);
				captarFluxoCaixaTotalizador(ptfc, "saldoDevedor", Confirmacao.N, item);
				captarFluxoCaixaTotalizador(ptfc, "fluxoLiquido", Confirmacao.N, item);
				break;
			case DESPESA_MAO_DE_OBRA:
				captarFluxoCaixaTotalizador(ptfc, "lucro", Confirmacao.N, item);
				captarFluxoCaixaTotalizador(ptfc, "saldoDevedor", Confirmacao.N, item);
				captarFluxoCaixaTotalizador(ptfc, "fluxoLiquido", Confirmacao.N, item);
				break;
			case DESPESA_OUTRO:
				captarFluxoCaixaTotalizador(ptfc, "lucro", Confirmacao.N, item);
				captarFluxoCaixaTotalizador(ptfc, "saldoDevedor", Confirmacao.N, item);
				captarFluxoCaixaTotalizador(ptfc, "fluxoLiquido", Confirmacao.N, item);
				break;
			case DESPESA_REMUNERACAO_PRODUTOR:
				captarFluxoCaixaTotalizador(ptfc, "lucro", Confirmacao.N, item);
				captarFluxoCaixaTotalizador(ptfc, "saldoDevedor", Confirmacao.N, item);
				captarFluxoCaixaTotalizador(ptfc, "fluxoLiquido", Confirmacao.N, item);
				break;
			case RECEITA_ATIVIDADE_AGROPECUARIA:
				captarFluxoCaixaTotalizador(ptfc, "lucro", Confirmacao.S, item);
				captarFluxoCaixaTotalizador(ptfc, "saldoDevedor", Confirmacao.S, item);
				captarFluxoCaixaTotalizador(ptfc, "fluxoLiquido", Confirmacao.S, item);
				break;
			case RECEITA_OUTRO:
				captarFluxoCaixaTotalizador(ptfc, "lucro", Confirmacao.S, item);
				captarFluxoCaixaTotalizador(ptfc, "saldoDevedor", Confirmacao.S, item);
				captarFluxoCaixaTotalizador(ptfc, "fluxoLiquido", Confirmacao.S, item);
				break;
			}

		}
		result.add(ptfc);
		return result;
	}

	private ProjetoTecnicoFluxoCaixaItemDto captarFluxoCaixaItem(ProjetoCreditoRuralFluxoCaixa fluxoCaixa) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		ProjetoTecnicoFluxoCaixaItemDto result = new ProjetoTecnicoFluxoCaixaItemDto();
		result.setCodigo(fluxoCaixa.getCodigo());
		for (FluxoCaixaAno fca : fluxoCaixa.getFluxoCaixaAnoList()) {
			MethodUtils.invokeMethod(result, String.format("setAno%02d", fca.getAno()), fca.getValor());
		}
		return result;
	}

	private void captarFluxoCaixaTotalizador(ProjetoTecnicoFluxoCaixaDto objeto, String metodo, Confirmacao somar, ProjetoTecnicoFluxoCaixaItemDto item) throws Exception {
		for (int i = 1; i <= 10; i++) {
			BigDecimal valor = (BigDecimal) MethodUtils.invokeMethod(item, String.format("getAno%02d", i));
			BigDecimal total = (BigDecimal) MethodUtils.invokeMethod(objeto, String.format("get%sAno%02d", StringUtils.capitalize(metodo), i));
			if (valor == null) {
				valor = new BigDecimal("0");
			}
			if (total == null) {
				total = new BigDecimal("0");
			}
			MethodUtils.invokeMethod(objeto, String.format("set%sAno%02d", StringUtils.capitalize(metodo), i), Confirmacao.S.equals(somar) ? total.add(valor) : total.subtract(valor));
		}
	}

	private List<ProjetoTecnicoGarantiaRelDto> captarGarantia(List<ProjetoCreditoRural> projetoCreditoRuralList) throws Exception {
		List<ProjetoTecnicoGarantiaAvalistaRelDto> garantiaAvalistaList = new ArrayList<>();
		for (ProjetoCreditoRuralGarantia pcrg : projetoCreditoRuralList.get(0).getGarantiaList()) {
			ProjetoTecnicoGarantiaAvalistaRelDto garantiaAvalista = new ProjetoTecnicoGarantiaAvalistaRelDto();

			// encontrar o endereco principal
			if (!CollectionUtils.isEmpty(pcrg.getPessoaFisica().getEnderecoList())) {
				for (PessoaEndereco pEnd : pcrg.getPessoaFisica().getEnderecoList()) {
					if (garantiaAvalista.getLogradouro() == null || Confirmacao.S.equals(pEnd.getPrincipal())) {
						garantiaAvalista.setLogradouro(String.format("%s, %s, %s", pEnd.getEndereco().getLogradouro(), pEnd.getEndereco().getComplemento(), pEnd.getEndereco().getNumero()));
						garantiaAvalista.setLocalidade(String.format("%s, %s", pEnd.getEndereco().getBairro(), pEnd.getEndereco().getEstado() == null ? "" : pEnd.getEndereco().getEstado().getSigla()));
						garantiaAvalista.setCep(pEnd.getEndereco().getCep());
					}
				}
			}

			// encontrar o telefone principal
			if (!CollectionUtils.isEmpty(pcrg.getPessoaFisica().getTelefoneList())) {
				for (PessoaTelefone pTel : pcrg.getPessoaFisica().getTelefoneList()) {
					if (garantiaAvalista.getTelefoneCelular() == null || Confirmacao.S.equals(pTel.getPrincipal())) {
						garantiaAvalista.setTelefoneCelular(pTel.getTelefone().getNumero());
					}
				}
			}

			// encontrar pai e mae
			if (!CollectionUtils.isEmpty(pcrg.getPessoaFisica().getRelacionamentoList())) {
				for (PessoaRelacionamento pRel : pcrg.getPessoaFisica().getRelacionamentoList()) {
					if (RelacionamentoTipo.Codigo.FAMILIAR.name().equals(pRel.getRelacionamento().getRelacionamentoTipo().getCodigo())) {
						if ("Pai".equals(pRel.getRelacionamentoFuncao().getNomeSeMasculino())) {
							if (pRel.getPessoa() == null) {
								if (PessoaGenero.M.equals(pRel.getGenero())) {
									garantiaAvalista.setPaiNome(pRel.getNome());
								} else if (PessoaGenero.F.equals(pRel.getGenero())) {
									garantiaAvalista.setMaeNome(pRel.getNome());
								}
							} else {
								if (PessoaGenero.M.equals(((PessoaFisica) pRel.getPessoa()).getGenero())) {
									garantiaAvalista.setPaiNome(pRel.getNome());
								} else if (PessoaGenero.F.equals(((PessoaFisica) pRel.getPessoa()).getGenero())) {
									garantiaAvalista.setMaeNome(pRel.getNome());
								}
							}
						}
					}
				}
			}
			garantiaAvalista.setCpfCnpj(pcrg.getPessoaFisica().getCpf());
			garantiaAvalista.setEstadoCivil(pcrg.getPessoaFisica().getEstadoCivil());
			garantiaAvalista.setIdentidadeNumero(pcrg.getPessoaFisica().getRgNumero());
			garantiaAvalista.setIdentidadeOrgao(String.format("%s / %s", pcrg.getPessoaFisica().getRgOrgaoEmissor(), pcrg.getPessoaFisica().getRgUf()));
			garantiaAvalista.setNascimento(pcrg.getPessoaFisica().getNascimento());
			garantiaAvalista.setNaturalidade(String.format("%s %s", pcrg.getPessoaFisica().getNascimentoMunicipio() == null ? "" : pcrg.getPessoaFisica().getNascimentoMunicipio().getNome(),
					pcrg.getPessoaFisica().getNascimentoEstado() == null ? "" : pcrg.getPessoaFisica().getNascimentoEstado().getSigla()));
			garantiaAvalista.setNome(pcrg.getPessoaFisica().getNome());
			garantiaAvalista.setParticipacao(pcrg.getParticipacao());
			garantiaAvalista.setProfissao(pcrg.getPessoaFisica().getProfissao());
			garantiaAvalista.setRendaLiquida(pcrg.getRendaLiquida());

			garantiaAvalistaList.add(garantiaAvalista);
		}
		// ordenar pela ordem de participação
		Collections.sort(garantiaAvalistaList, ((a, b) -> Integer.compare(a.getParticipacao().getOrdem(), b.getParticipacao().getOrdem())));

		List<ProjetoTecnicoGarantiaRelDto> result = new ArrayList<>();
		ProjetoTecnicoGarantiaRelDto garantia = new ProjetoTecnicoGarantiaRelDto();
		garantia.setGarantia(projetoCreditoRuralList.get(0).getGarantiaReal());
		garantia.setGarantiaAvalistaList(garantiaAvalistaList);

		result.add(garantia);

		return result;
	}

	@SuppressWarnings("unchecked")
	private void captarUltimaColetaFormularios(List<Object[]> diagnosticoList, Dto reg, String... propriedadeList) throws Exception {
		Map<String, FormularioVersao> formularioVersaoMap = new HashMap<>();
		// captar a ultima versao do formulario
		for (Object[] formulario : diagnosticoList) {
			for (FormularioVersao versao : (List<FormularioVersao>) formulario[8]) {
				for (String propriedade : propriedadeList) {
					if (propriedade.equals(formulario[2])) {
						if (formularioVersaoMap.get(propriedade) == null || (Integer) versao.getVersao() > formularioVersaoMap.get(propriedade).getVersao()) {
							formularioVersaoMap.put(propriedade, versao);

							// limpar dados da coleta atual
							MethodUtils.invokeMethod(reg, "set".concat(StringUtils.capitalize(propriedade)).concat("Coleta"), (Coleta) null);
							for (Coleta coleta : versao.getColetaList()) {
								Coleta coletaTemp = (Coleta) MethodUtils.invokeMethod(reg, "get".concat(StringUtils.capitalize(propriedade)).concat("Coleta"));
								if (coletaTemp == null || coleta.getDataColeta().after(coletaTemp.getDataColeta())) {
									// captar coleta
									if (Confirmacao.N.equals(coleta.getFinalizada())) {
										throw new BoException("Há diagnósticos não finalizados!");
									}
									MethodUtils.invokeMethod(reg, "set".concat(StringUtils.capitalize(propriedade)).concat("Coleta"), (Coleta) coleta);
								}
							}
						}
					}
				}
			}
		}
		// verificar se alguma coleta não foi realizada
		for (String propriedade : propriedadeList) {
			if ((Coleta) MethodUtils.invokeMethod(reg, "get".concat(StringUtils.capitalize(propriedade)).concat("Coleta")) == null) {
				throw new BoException("Há diagnósticos não realizados! [Diagnostico %s]", propriedade);
			}
		}
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer[] idList = (Integer[]) contexto.getRequisicao();

		if (ArrayUtils.isEmpty(idList)) {
			throw new BoException("Nenhum projeto informado");
		}

		List<ProjetoCreditoRural> lista = dao.findAll(Arrays.asList(idList));

		if (CollectionUtils.isEmpty(lista)) {
			throw new BoException("Nenhum projeto encontrado com os parâmetros informados");
		}

		// gerar os relatorios
		List<byte[]> resultList = new ArrayList<>();
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("Usuario", getUsuario(contexto.getUsuario().getName()));

		for (ProjetoCreditoRural projeto : lista) {

			List<ProjetoCreditoRural> pList = new ArrayList<>();
			pList.add(projeto);

			List<JasperPrint> parteList = new ArrayList<>();

			// montar as partes do relatório
			montaParte("CAPA", "Capa", parametros, pList, parteList);
			montaParte("CADASTRO DO PROPONENTE", "Proponente", parametros, captarCadastroProponente(contexto, pList), parteList);
			montaParte("CADASTRO DA PROPRIEDADE RURAL", "PropriedadeRural", parametros, captarCadastroPropriedadeRural(contexto, pList), parteList);
			montaParte("TRIÊNIO, INVERSÕES, CUSTOS E RECEITAS", "Solicitacao", parametros, pList, parteList);

			// TODO criar
			montaParte("CUSTO DE PRODUÇÃO", "CustoProducao", parametros, captarCustoProducao(pList.get(0)), parteList);

			montaParte("CRONOGRAMA DE REEMBOLSO", "Cronograma", parametros, pList, parteList);
			montaParte("FLUXO DE CAIXA", "FluxoCaixa", parametros, captarFluxoCaixa(pList.get(0).getFluxoCaixaList()), parteList);
			montaParte("OBJETIVOS E PARECER TÉCNICO", "ParecerTecnico", parametros, projeto.getParecerTecnicoList(), parteList);
			montaParte("GARANTIAS - AVALISTAS", "Garantia", parametros, captarGarantia(pList), parteList);

			// juntar as partes do relatório
			// é necessário quebrar por orientação da página
			List<byte[]> arquivoList = new ArrayList<>();
			boolean primeiraPagina = false;
			JasperPrint bloco = null;
			OrientationEnum orientacaoPaginaAtual = null;
			for (JasperPrint parte : parteList) {
				if (!parte.getOrientationValue().equals(orientacaoPaginaAtual)) {
					primeiraPagina = true;
				}
				if (primeiraPagina) {
					if (bloco != null) {
						arquivoList.add(relatorio.imprimir(bloco));
					}
					// procedimento para ignorar a capa
					primeiraPagina = false;
					bloco = parte;
					orientacaoPaginaAtual = parte.getOrientationValue();
					continue;
				}

				// juntar as partes
				for (JRPrintPage pagina : parte.getPages()) {
					bloco.addPage(pagina);
				}
			}
			// captar o último bloco
			if (bloco != null) {
				arquivoList.add(relatorio.imprimir(bloco));
			}

			// gerar o resultado
			resultList.add(UtilitarioPdf.juntarPdf(arquivoList));
			
		}

		// zipar o resultado
		byte[] result = ziparResultado(lista, resultList);

		contexto.setResposta(result);

		return false;
	}

	private void iniciarRelacionamento() {
		if (this.relacionamentoTipo == null) {
			this.relacionamentoTipo = relacionamentoTipoDao.findByCodigo(RelacionamentoTipo.Codigo.FAMILIAR.name());
			this.funcaoPaiMae = relacionamentoFuncaoDao.findOneByNomeSeMasculino("Pai");
			this.funcaoConjuge = relacionamentoFuncaoDao.findOneByNomeSeMasculino("Esposo");
		}
	}

	private Map<String, Object> montaColeta(Coleta coleta) throws Exception {
		if (coleta == null || coleta.getValorString() == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> result = mapper.readValue(coleta.getValorString(), new TypeReference<Map<String, Object>>() {
		});
		return result;
	}

	private void montaParte(String relatorioNome, String nomeArquivo, Map<String, Object> parametros, List<?> pList, List<JasperPrint> parteList) throws BoException {
		if (pList == null || pList.size() == 0) {
			return;
		}
		parametros.put("RelatorioNome", relatorioNome);
		parametros.put("Parte", parteList.size() + 1);
		JasperPrint impressao = relatorio.montarRelatorio(String.format("projeto_credito_rural/%s", nomeArquivo), parametros, pList);
		parteList.add(impressao);
	}

	@SuppressWarnings("unchecked")
	private void processarDiagnosticoProponente(ProjetoTecnicoProponenteRelDto reg) throws Exception {
		Map<String, Object> beneficioSocialForcaTrabalho = montaColeta(reg.getBeneficioSocialForcaTrabalhoColeta());
		Map<String, Object> patrimonioDivida = montaColeta(reg.getPatrimonioDividaColeta());

		// FORCA DE TRABALHO CONTRATADA
		if (beneficioSocialForcaTrabalho != null) {
			reg.setForcaTrabalhoEventual(new BigDecimal(beneficioSocialForcaTrabalho.get("eventualDiaHomemAno").toString()));
			reg.setForcaTrabalhoTrabalhadorPermanente(new BigDecimal(beneficioSocialForcaTrabalho.get("trabalhadorPermanente").toString()));
			reg.setForcaTrabalhoSalarioMensal(new BigDecimal(beneficioSocialForcaTrabalho.get("salarioMensal").toString()));
		}

		// RENDA BRUTA ANUAL
		reg.setRendaBrutaAnualPropriedadeValor(patrimonioDivida == null || patrimonioDivida.get("rendaBrutaAnualPropriedade") == null ? new BigDecimal(0) : new BigDecimal(patrimonioDivida.get("rendaBrutaAnualPropriedade").toString()));
		reg.setRendaBrutaAnualAssalariadoValor(patrimonioDivida == null || patrimonioDivida.get("rendaBrutaAnualAssalariado") == null ? new BigDecimal(0) : new BigDecimal(patrimonioDivida.get("rendaBrutaAnualAssalariado").toString()));
		reg.setRendaBrutaAnualOutrasRendasValor(patrimonioDivida == null || patrimonioDivida.get("rendaBrutaAnualOutrasRendas") == null ? new BigDecimal(0) : new BigDecimal(patrimonioDivida.get("rendaBrutaAnualOutrasRendas").toString()));
		reg.setRendaBrutaAnualTotalValor(reg.getRendaBrutaAnualPropriedadeValor().add(reg.getRendaBrutaAnualAssalariadoValor().add(reg.getRendaBrutaAnualOutrasRendasValor())));
		reg.setRendaBrutaAnualTotalPercentual(new BigDecimal("100"));
		if (reg.getRendaBrutaAnualTotalValor().equals(new BigDecimal("0"))) {
			reg.setRendaBrutaAnualPropriedadePercentual(new BigDecimal("0"));
			reg.setRendaBrutaAnualAssalariadoPercentual(new BigDecimal("0"));
			reg.setRendaBrutaAnualOutrasRendasPercentual(new BigDecimal("0"));
		} else {
			reg.setRendaBrutaAnualPropriedadePercentual(reg.getRendaBrutaAnualPropriedadeValor().divide(reg.getRendaBrutaAnualTotalValor(), RoundingMode.HALF_UP).multiply(new BigDecimal("100")));
			reg.setRendaBrutaAnualAssalariadoPercentual(reg.getRendaBrutaAnualAssalariadoValor().divide(reg.getRendaBrutaAnualTotalValor(), RoundingMode.HALF_UP).multiply(new BigDecimal("100")));
			reg.setRendaBrutaAnualOutrasRendasPercentual(reg.getRendaBrutaAnualOutrasRendasValor().divide(reg.getRendaBrutaAnualTotalValor(), RoundingMode.HALF_UP).multiply(new BigDecimal("100")));
		}

		// MAQUINAS E EQUIPAMENTOS
		reg.setPatrimonioMaquinasEquipamento(new BigDecimal("0"));
		List<Map<String, Object>> maquinaEquipamentoList = patrimonioDivida == null ? new ArrayList<>() : (List<Map<String, Object>>) patrimonioDivida.get("maquinaEquipamentoList");
		if (!CollectionUtils.isEmpty(maquinaEquipamentoList)) {
			List<RelacaoItemRelDto> patrimonioMaquinaEquipamentoList = new ArrayList<>();
			for (Map<String, Object> registro : maquinaEquipamentoList) {
				BigDecimal quantidade = new BigDecimal(registro.get("quantidade") == null ? "0" : registro.get("quantidade").toString());
				BigDecimal valorUnitario = new BigDecimal(registro.get("valorUnitario") == null ? "0" : registro.get("valorUnitario").toString());
				reg.setPatrimonioMaquinasEquipamento(reg.getPatrimonioMaquinasEquipamento().add(quantidade.multiply(valorUnitario)));
				patrimonioMaquinaEquipamentoList.add(new RelacaoItemRelDto((String) registro.get("discriminacao"), (String) registro.get("marca"), (String) registro.get("chassi"), (Integer) registro.get("ano"), new BigDecimal(registro.get("quantidade").toString()),
						new BigDecimal(registro.get("valorUnitario").toString())));
			}
			reg.setPatrimonioMaquinaEquipamentoList(patrimonioMaquinaEquipamentoList);
		}

		// SEMOVENTES
		reg.setPatrimonioSemoventes(new BigDecimal("0"));
		List<Map<String, Object>> semoventeList = (List<Map<String, Object>>) patrimonioDivida.get("semoventeList");
		if (!CollectionUtils.isEmpty(semoventeList)) {
			List<RelacaoItemRelDto> patrimonioSemoventeList = new ArrayList<>();
			for (Map<String, Object> registro : semoventeList) {
				BigDecimal quantidade = new BigDecimal(registro.get("quantidade") == null ? "0" : registro.get("quantidade").toString());
				BigDecimal valorUnitario = new BigDecimal(registro.get("valorUnitario") == null ? "0" : registro.get("valorUnitario").toString());
				reg.setPatrimonioSemoventes(reg.getPatrimonioSemoventes().add(quantidade.multiply(valorUnitario)));
				patrimonioSemoventeList.add(new RelacaoItemRelDto((String) registro.get("discriminacao"), (String) registro.get("unidade"), null, null, new BigDecimal(registro.get("quantidade").toString()), new BigDecimal(registro.get("valorUnitario").toString())));
			}
			reg.setPatrimonioSemoventeList(patrimonioSemoventeList);
		}

		// OUTROS PATRIMONIOS
		reg.setPatrimonioOutros(new BigDecimal("0"));
		List<Map<String, Object>> outroPatrimonioList = (List<Map<String, Object>>) patrimonioDivida.get("outroPatrimonioList");
		if (!CollectionUtils.isEmpty(outroPatrimonioList)) {
			List<RelacaoItemRelDto> patrimonioOutroBemList = new ArrayList<>();
			for (Map<String, Object> registro : outroPatrimonioList) {
				BigDecimal quantidade = new BigDecimal(registro.get("quantidade") == null ? "0" : registro.get("quantidade").toString());
				BigDecimal valorUnitario = new BigDecimal(registro.get("valorUnitario") == null ? "0" : registro.get("valorUnitario").toString());
				reg.setPatrimonioOutros(reg.getPatrimonioOutros().add(quantidade.multiply(valorUnitario)));
				patrimonioOutroBemList.add(new RelacaoItemRelDto((String) registro.get("discriminacao"), (String) registro.get("unidade"), null, null, new BigDecimal(registro.get("quantidade").toString()), new BigDecimal(registro.get("valorUnitario").toString())));
			}
			reg.setPatrimonioOutroBemList(patrimonioOutroBemList);
		}

		// DIVIDAS EXISTENTES
		reg.setPatrimonioDivida(new BigDecimal("0"));
		List<Map<String, Object>> dividasList = (List<Map<String, Object>>) patrimonioDivida.get("dividaExistenteList");
		if (!CollectionUtils.isEmpty(dividasList)) {
			List<DividaExistenteRelDto> dividaExistenteList = new ArrayList<>();
			for (Map<String, Object> registro : dividasList) {
				BigDecimal valorContratado = new BigDecimal(registro.get("valorContratado") == null ? "0" : registro.get("valorContratado").toString());
				reg.setPatrimonioDivida(reg.getPatrimonioDivida().add(valorContratado));
				dividaExistenteList.add(new DividaExistenteRelDto((String) registro.get("finalidade"), (String) registro.get("especificacao"), (Calendar) UtilitarioData.getInstance().stringParaData(registro.get("contratacao")),
						(Calendar) UtilitarioData.getInstance().stringParaData(registro.get("vencimento")), new BigDecimal(registro.get("juroAnual") == null ? "0" : registro.get("juroAnual").toString()),
						new BigDecimal(registro.get("valorContratado") == null ? "0" : registro.get("valorContratado").toString()), new BigDecimal(registro.get("amortizacaoAnual") == null ? "0" : registro.get("amortizacaoAnual").toString()),
						new BigDecimal(registro.get("saldoDevedor") == null ? "0" : registro.get("saldoDevedor").toString())));
			}
			reg.setDividaExistenteList(dividaExistenteList);
		}
	}

	@SuppressWarnings("unchecked")
	private void processarDiagnosticoPropriedade(List<Object[]> diagnosticoList, ProjetoTecnicoProponenteRelDto reg) throws Exception {
		Coleta coletaUltima = null;

		Map<String, FormularioVersao> formularioVersaoMap = new HashMap<>();
		// captar a ultima versao do formulario
		for (Object[] formulario : diagnosticoList) {
			for (FormularioVersao versao : (List<FormularioVersao>) formulario[8]) {
				if ("avaliacaoDaPropriedade".equals(formulario[2])) {
					if (formularioVersaoMap.get("avaliacaoDaPropriedade") == null || (Integer) versao.getVersao() > formularioVersaoMap.get("avaliacaoDaPropriedade").getVersao()) {
						formularioVersaoMap.put("avaliacaoDaPropriedade", versao);
						// limpar dados da coleta atual
						coletaUltima = null;
						for (Coleta coleta : versao.getColetaList()) {
							if (coletaUltima == null || coleta.getDataColeta().after(coletaUltima.getDataColeta())) {
								// captar coleta
								if (Confirmacao.N.equals(coleta.getFinalizada())) {
									throw new BoException("Há diagnósticos não finalizados!");
								}
								coletaUltima = coleta;
							}
						}
					}
				}
			}
		}
		// verificar se alguma coleta não foi realizada
		if (coletaUltima == null) {
			throw new BoException("Há diagnósticos não realizados!");
		}

		Map<String, Object> valor = montaColeta(coletaUltima);

		Map<String, Object> usoDoSoloMap = (Map<String, Object>) valor.get("usoDoSolo");
		List<Map<String, Object>> benfeitoriaListMapList = (List<Map<String, Object>>) valor.get("benfeitoriaList");

		if (!CollectionUtils.isEmpty(usoDoSoloMap)) {
			BigDecimal benfeitoriasArea = new BigDecimal(usoDoSoloMap.get("benfeitoriasArea") == null ? null : usoDoSoloMap.get("benfeitoriasArea").toString());
			BigDecimal benfeitoriasValorUnitario = new BigDecimal(usoDoSoloMap.get("benfeitoriasValorUnitario") == null ? null : usoDoSoloMap.get("benfeitoriasValorUnitario").toString());
			BigDecimal culturasPerenesArea = new BigDecimal(usoDoSoloMap.get("culturasPerenesArea") == null ? null : usoDoSoloMap.get("culturasPerenesArea").toString());
			BigDecimal culturasPerenesValorUnitario = new BigDecimal(usoDoSoloMap.get("culturasPerenesValorUnitario") == null ? null : usoDoSoloMap.get("culturasPerenesValorUnitario").toString());
			BigDecimal culturasTemporariasArea = new BigDecimal(usoDoSoloMap.get("culturasTemporariasArea") == null ? null : usoDoSoloMap.get("culturasTemporariasArea").toString());
			BigDecimal culturasTemporariasValorUnitario = new BigDecimal(usoDoSoloMap.get("culturasTemporariasValorUnitario") == null ? null : usoDoSoloMap.get("culturasTemporariasValorUnitario").toString());
			BigDecimal outrasArea = new BigDecimal(usoDoSoloMap.get("outrasArea") == null ? null : usoDoSoloMap.get("outrasArea").toString());
			BigDecimal outrasValorUnitario = new BigDecimal(usoDoSoloMap.get("outrasValorUnitario") == null ? null : usoDoSoloMap.get("outrasValorUnitario").toString());
			BigDecimal pastagensArea = new BigDecimal(usoDoSoloMap.get("pastagensArea") == null ? null : usoDoSoloMap.get("pastagensArea").toString());
			BigDecimal pastagensValorUnitario = new BigDecimal(usoDoSoloMap.get("pastagensValorUnitario") == null ? null : usoDoSoloMap.get("pastagensValorUnitario").toString());
			BigDecimal preservacaoPermanenteArea = new BigDecimal(usoDoSoloMap.get("preservacaoPermanenteArea") == null ? null : usoDoSoloMap.get("preservacaoPermanenteArea").toString());
			BigDecimal preservacaoPermanenteValorUnitario = new BigDecimal(usoDoSoloMap.get("preservacaoPermanenteValorUnitario") == null ? null : usoDoSoloMap.get("preservacaoPermanenteValorUnitario").toString());
			BigDecimal reservaLegalArea = new BigDecimal(usoDoSoloMap.get("reservaLegalArea") == null ? null : usoDoSoloMap.get("reservaLegalArea").toString());
			BigDecimal reservaLegalValorUnitario = new BigDecimal(usoDoSoloMap.get("reservaLegalValorUnitario") == null ? null : usoDoSoloMap.get("reservaLegalValorUnitario").toString());
			reg.setPatrimonioTerras(reg.getPatrimonioTerras().add(benfeitoriasArea.multiply(benfeitoriasValorUnitario).add(culturasPerenesArea.multiply(culturasPerenesValorUnitario).add(culturasTemporariasArea.multiply(culturasTemporariasValorUnitario)
					.add(outrasArea.multiply(outrasValorUnitario).add(pastagensArea.multiply(pastagensValorUnitario).add(preservacaoPermanenteArea.multiply(preservacaoPermanenteValorUnitario).add(reservaLegalArea.multiply(reservaLegalValorUnitario)))))))));
		}

		if (!CollectionUtils.isEmpty(benfeitoriaListMapList)) {
			for (Map<String, Object> registro : benfeitoriaListMapList) {
				BigDecimal quantidade = new BigDecimal(registro.get("quantidade") == null ? "0" : registro.get("quantidade").toString());
				BigDecimal valorUnitario = new BigDecimal(registro.get("valorUnitario") == null ? "0" : registro.get("valorUnitario").toString());
				reg.setPatrimonioBenfeitorias(reg.getPatrimonioBenfeitorias().add(quantidade.multiply(valorUnitario)));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void processarDiagnosticoPropriedadeRural(ProjetoTecnicoPropriedadeRuralRelDto reg) throws Exception {
		Map<String, Object> avaliacaoDaPropriedade = montaColeta(reg.getAvaliacaoDaPropriedadeColeta());

		Map<String, Object> usoDoSolo = (Map<String, Object>) avaliacaoDaPropriedade.get("usoDoSolo");
		reg.setUsoDoSoloCulturasPerenesArea(new BigDecimal(usoDoSolo.get("culturasPerenesArea") == null ? "0" : usoDoSolo.get("culturasPerenesArea").toString()));
		reg.setUsoDoSoloCulturasTemporariasArea(new BigDecimal(usoDoSolo.get("culturasTemporariasArea") == null ? "0" : usoDoSolo.get("culturasTemporariasArea").toString()));
		reg.setUsoDoSoloPastagensArea(new BigDecimal(usoDoSolo.get("pastagensArea") == null ? "0" : usoDoSolo.get("pastagensArea").toString()));
		reg.setUsoDoSoloBenfeitoriasArea(new BigDecimal(usoDoSolo.get("benfeitoriasArea") == null ? "0" : usoDoSolo.get("benfeitoriasArea").toString()));
		reg.setUsoDoSoloReservaLegalArea(new BigDecimal(usoDoSolo.get("reservaLegalArea") == null ? "0" : usoDoSolo.get("reservaLegalArea").toString()));
		reg.setUsoDoSoloPreservacaoPermanenteArea(new BigDecimal(usoDoSolo.get("preservacaoPermanenteArea") == null ? "0" : usoDoSolo.get("preservacaoPermanenteArea").toString()));
		reg.setUsoDoSoloOutrasArea(new BigDecimal(usoDoSolo.get("outrasArea") == null ? "0" : usoDoSolo.get("outrasArea").toString()));
		reg.setUsoDoSoloCulturasPerenesValorUnitario(new BigDecimal(usoDoSolo.get("culturasPerenesValorUnitario") == null ? "0" : usoDoSolo.get("culturasPerenesValorUnitario").toString()).multiply(reg.getUsoDoSoloCulturasPerenesArea()));
		reg.setUsoDoSoloCulturasTemporariasValorUnitario(new BigDecimal(usoDoSolo.get("culturasTemporariasValorUnitario") == null ? "0" : usoDoSolo.get("culturasTemporariasValorUnitario").toString()).multiply(reg.getUsoDoSoloCulturasTemporariasArea()));
		reg.setUsoDoSoloPastagensValorUnitario(new BigDecimal(usoDoSolo.get("pastagensValorUnitario") == null ? "0" : usoDoSolo.get("pastagensValorUnitario").toString()).multiply(reg.getUsoDoSoloPastagensArea()));
		reg.setUsoDoSoloBenfeitoriasValorUnitario(new BigDecimal(usoDoSolo.get("benfeitoriasValorUnitario") == null ? "0" : usoDoSolo.get("benfeitoriasValorUnitario").toString()).multiply(reg.getUsoDoSoloBenfeitoriasArea()));
		reg.setUsoDoSoloReservaLegalValorUnitario(new BigDecimal(usoDoSolo.get("reservaLegalValorUnitario") == null ? "0" : usoDoSolo.get("reservaLegalValorUnitario").toString()).multiply(reg.getUsoDoSoloReservaLegalArea()));
		reg.setUsoDoSoloPreservacaoPermanenteValorUnitario(new BigDecimal(usoDoSolo.get("preservacaoPermanenteValorUnitario") == null ? "0" : usoDoSolo.get("preservacaoPermanenteValorUnitario").toString()).multiply(reg.getUsoDoSoloPreservacaoPermanenteArea()));
		reg.setUsoDoSoloOutrasValorUnitario(new BigDecimal(usoDoSolo.get("outrasValorUnitario") == null ? "0" : usoDoSolo.get("outrasValorUnitario").toString()).multiply(reg.getUsoDoSoloOutrasArea()));
		reg.setUsoDoSoloAreaTotal(reg.getUsoDoSoloCulturasPerenesArea()
				.add(reg.getUsoDoSoloCulturasTemporariasArea().add(reg.getUsoDoSoloPastagensArea().add(reg.getUsoDoSoloBenfeitoriasArea().add(reg.getUsoDoSoloReservaLegalArea().add(reg.getUsoDoSoloPreservacaoPermanenteArea().add(reg.getUsoDoSoloOutrasArea())))))));
		reg.setUsoDoSoloValorTotal(reg.getUsoDoSoloCulturasPerenesValorUnitario().add(reg.getUsoDoSoloCulturasTemporariasValorUnitario()
				.add(reg.getUsoDoSoloPastagensValorUnitario().add(reg.getUsoDoSoloBenfeitoriasValorUnitario().add(reg.getUsoDoSoloReservaLegalValorUnitario().add(reg.getUsoDoSoloPreservacaoPermanenteValorUnitario().add(reg.getUsoDoSoloOutrasValorUnitario())))))));

		Map<String, Object> areasIrrigadas = (Map<String, Object>) avaliacaoDaPropriedade.get("areasIrrigadas");
		reg.setAreasIrrigadasAutoPropelido(new BigDecimal(areasIrrigadas.get("autoPropelido") == null ? "0" : areasIrrigadas.get("autoPropelido").toString()));
		reg.setAreasIrrigadasGotejamento(new BigDecimal(areasIrrigadas.get("gotejamento") == null ? "0" : areasIrrigadas.get("gotejamento").toString()));
		reg.setAreasIrrigadasMicroAspersao(new BigDecimal(areasIrrigadas.get("microAspersao") == null ? "0" : areasIrrigadas.get("microAspersao").toString()));
		reg.setAreasIrrigadasOutros(new BigDecimal(areasIrrigadas.get("outros") == null ? "0" : areasIrrigadas.get("outros").toString()));
		reg.setAreasIrrigadasPivoCentral(new BigDecimal(areasIrrigadas.get("pivoCentral") == null ? "0" : areasIrrigadas.get("pivoCentral").toString()));
		reg.setAreasIrrigadasSuperficie(new BigDecimal(areasIrrigadas.get("superficie") == null ? "0" : areasIrrigadas.get("superficie").toString()));
		reg.setAreasIrrigadasAspersaoConvencional(new BigDecimal(areasIrrigadas.get("aspersaoConvencional") == null ? "0" : areasIrrigadas.get("aspersaoConvencional").toString()));
		reg.setAreasIrrigadasAreaTotal(reg.getAreasIrrigadasAutoPropelido()
				.add(reg.getAreasIrrigadasGotejamento().add(reg.getAreasIrrigadasMicroAspersao().add(reg.getAreasIrrigadasOutros().add(reg.getAreasIrrigadasPivoCentral().add(reg.getAreasIrrigadasSuperficie().add(reg.getAreasIrrigadasAspersaoConvencional())))))));

		Map<String, Object> moradoresDaPropriedade = (Map<String, Object>) avaliacaoDaPropriedade.get("moradoresDaPropriedade");
		reg.setMoradoresDaPropriedadePessoas(new BigDecimal(moradoresDaPropriedade.get("pessoas") == null ? "0" : moradoresDaPropriedade.get("pessoas").toString()));
		reg.setMoradoresDaPropriedadeFamilias(new BigDecimal(moradoresDaPropriedade.get("familias") == null ? "0" : moradoresDaPropriedade.get("familias").toString()));

		Map<String, Object> maoDeObra = (Map<String, Object>) avaliacaoDaPropriedade.get("maoDeObra");
		reg.setMaoDeObraFamiliar(new BigDecimal(maoDeObra.get("familiar") == null ? "0" : maoDeObra.get("familiar").toString()));
		reg.setMaoDeObraContratada(new BigDecimal(maoDeObra.get("contratada") == null ? "0" : maoDeObra.get("contratada").toString()));
		reg.setMaoDeObraTemporaria(new BigDecimal(maoDeObra.get("temporaria") == null ? "0" : maoDeObra.get("temporaria").toString()));

		// FIXME notebook de casa sem estes dados, remover em producaos
		Map<String, Object> fonteDeAgua = (Map<String, Object>) avaliacaoDaPropriedade.get("fonteDAgua");
		reg.setFonteDAguaPrincipal(fonteDeAgua == null ? "": (String) fonteDeAgua.get("fonteDAguaPrincipal"));
		reg.setVazaoLSPrincipal(new BigDecimal(fonteDeAgua == null || fonteDeAgua.get("vazaoLSPrincpal") == null ? "0" : fonteDeAgua.get("vazaoLSPrincpal").toString()));
		reg.setFonteDAguaSecundaria(fonteDeAgua == null ? "": (String) fonteDeAgua.get("fonteDAguaSecundaria"));
		reg.setVazaoLSSecundaria(new BigDecimal(fonteDeAgua == null || fonteDeAgua.get("vazaoLSSecundaria") == null ? "0" : fonteDeAgua.get("vazaoLSSecundaria").toString()));			

		List<Map<String, Object>> benfeitoriaList = (List<Map<String, Object>>) avaliacaoDaPropriedade.get("benfeitoriaList");
		List<RelacaoItemRelDto> benefList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(benfeitoriaList)) {
			for (Map<String, Object> b : benfeitoriaList) {
				benefList.add(new RelacaoItemRelDto((String) b.get("discriminacaoDasBenfeitorias"), (String) b.get("caracteristica"), (String) b.get("unidade"), null, new BigDecimal(b.get("quantidade") == null ? "0" : b.get("quantidade").toString()),
						new BigDecimal(b.get("valorUnitario") == null ? "0" : b.get("valorUnitario").toString())));
			}
		}
		reg.setBenfeitoriasList(benefList);
	}

	private byte[] ziparResultado(List<ProjetoCreditoRural> lista, List<byte[]> resultList) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		ZipEntry entry;
		int i = 0;
		byte[] arquivo;
		
		for (ProjetoCreditoRural projeto : lista) {
			// inserir o arquivo do projeto de crédito
			entry = new ZipEntry(projeto.getAtividade().getCodigo().concat(".pdf"));
			arquivo = resultList.get(i++);
			entry.setSize(arquivo.length);
			zos.putNextEntry(entry);
			zos.write(arquivo);
			zos.closeEntry();
			
			// inserir os arquivos anexos do projeto de credito
			if (!CollectionUtils.isEmpty(projeto.getArquivoList())) {
				for (ProjetoCreditoRuralArquivo anexo: projeto.getArquivoList()) {
					entry = new ZipEntry(projeto.getAtividade().getCodigo().concat(anexo.getArquivo().getNomeOriginal()).concat(anexo.getArquivo().getExtensao()));
					arquivo = anexo.getArquivo().getConteudo();
					entry.setSize(arquivo.length);
					zos.putNextEntry(entry);
					zos.write(arquivo);
					zos.closeEntry();
				}
			}
		}
		zos.close();
		return baos.toByteArray();
	}

}