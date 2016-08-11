package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
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

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.RelacionamentoFuncaoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.RelacionamentoTipoDao;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoRuralDao;
import br.gov.df.emater.aterwebsrv.dto.formulario.FormularioColetaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoTecnicoProponenteRelDto;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoSetor;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
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
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralPublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.relatorio._Relatorio;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;

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
			if (email == null) {
				throw new BoException("E-mail não informado!");
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
			captarUltimaColetaFormularios((List<Object[]>) proponente.getDiagnosticoList(), reg, "beneficioSocialForcaTrabalho", "patrimonioEDividas");

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
					// TODO Auto-generated method stub
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
	private void captarUltimaColetaFormularios(List<Object[]> diagnosticoList, ProjetoTecnicoProponenteRelDto reg, String... propriedadeList) throws Exception {
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
				throw new BoException("Há diagnósticos não realizados!");
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

			parametros.put("RelatorioNome", "CAPA");
			JasperPrint capa = relatorio.montarRelatorio("projeto_credito_rural/Capa", parametros, pList);

			parametros.put("RelatorioNome", "CADASTRO DO PROPONENTE");
			JasperPrint proponente = relatorio.montarRelatorio("projeto_credito_rural/Proponente", parametros, captarCadastroProponente(contexto, pList));

			// montar o resultado final
			for (JRPrintPage pagina : proponente.getPages()) {
				capa.addPage(pagina);
			}

			resultList.add(relatorio.imprimir(capa));
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
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> result = mapper.readValue(coleta.getValorString(), new TypeReference<Map<String, Object>>() {
		});
		return result;
	}

	@SuppressWarnings("unchecked")
	private void processarDiagnosticoProponente(ProjetoTecnicoProponenteRelDto reg) throws Exception {
		Map<String, Object> beneficioSocialForcaTrabalho = montaColeta(reg.getBeneficioSocialForcaTrabalhoColeta());
		Map<String, Object> patrimonioEDividas = montaColeta(reg.getPatrimonioEDividasColeta());

		// FORCA DE TRABALHO CONTRATADA
		reg.setForcaTrabalhoEventual(new BigDecimal(beneficioSocialForcaTrabalho.get("eventualDiaHomemAno").toString()));
		reg.setForcaTrabalhoTrabalhadorPermanente(new BigDecimal(beneficioSocialForcaTrabalho.get("trabalhadorPermanente").toString()));
		reg.setForcaTrabalhoSalarioMensal(new BigDecimal(beneficioSocialForcaTrabalho.get("salarioMensal").toString()));

		// RENDA BRUTA ANUAL
		reg.setRendaBrutaAnualPropriedadeValor(patrimonioEDividas.get("rendaBrutaAnualPropriedade") == null ? new BigDecimal(0) : new BigDecimal(patrimonioEDividas.get("rendaBrutaAnualPropriedade").toString()));
		reg.setRendaBrutaAnualAssalariadoValor(patrimonioEDividas.get("rendaBrutaAnualAssalariado") == null ? new BigDecimal(0) : new BigDecimal(patrimonioEDividas.get("rendaBrutaAnualAssalariado").toString()));
		reg.setRendaBrutaAnualOutrasRendasValor(patrimonioEDividas.get("rendaBrutaAnualOutrasRendas") == null ? new BigDecimal(0) : new BigDecimal(patrimonioEDividas.get("rendaBrutaAnualOutrasRendas").toString()));
		reg.setRendaBrutaAnualTotalValor(reg.getRendaBrutaAnualPropriedadeValor().add(reg.getRendaBrutaAnualAssalariadoValor().add(reg.getRendaBrutaAnualOutrasRendasValor())));
		reg.setRendaBrutaAnualTotalPercentual(new BigDecimal("100"));
		reg.setRendaBrutaAnualPropriedadePercentual(reg.getRendaBrutaAnualPropriedadeValor().divide(reg.getRendaBrutaAnualTotalValor(), RoundingMode.HALF_UP).multiply(new BigDecimal("100")));
		reg.setRendaBrutaAnualAssalariadoPercentual(reg.getRendaBrutaAnualAssalariadoValor().divide(reg.getRendaBrutaAnualTotalValor(), RoundingMode.HALF_UP).multiply(new BigDecimal("100")));
		reg.setRendaBrutaAnualOutrasRendasPercentual(reg.getRendaBrutaAnualOutrasRendasValor().divide(reg.getRendaBrutaAnualTotalValor(), RoundingMode.HALF_UP).multiply(new BigDecimal("100")));

		// MAQUINAS E EQUIPAMENTOS
		reg.setPatrimonioMaquinasEquipamento(new BigDecimal("0"));
		List<Map<String, Object>> maquinaEquipamentoList = (List<Map<String, Object>>) patrimonioEDividas.get("maquinaEquipamentoList");
		if (!CollectionUtils.isEmpty(maquinaEquipamentoList)) {
			for (Map<String, Object> registro : maquinaEquipamentoList) {
				BigDecimal quantidade = new BigDecimal(registro.get("quantidade") == null ? "0" : registro.get("quantidade").toString());
				BigDecimal valorUnitario = new BigDecimal(registro.get("valorUnitario") == null ? "0" : registro.get("valorUnitario").toString());
				reg.setPatrimonioMaquinasEquipamento(reg.getPatrimonioMaquinasEquipamento().add(quantidade.multiply(valorUnitario)));
			}
		}

		// SEMOVENTES
		reg.setPatrimonioSemoventes(new BigDecimal("0"));
		List<Map<String, Object>> semoventeList = (List<Map<String, Object>>) patrimonioEDividas.get("semoventeList");
		if (!CollectionUtils.isEmpty(semoventeList)) {
			for (Map<String, Object> registro : semoventeList) {
				BigDecimal quantidade = new BigDecimal(registro.get("quantidade") == null ? "0" : registro.get("quantidade").toString());
				BigDecimal valorUnitario = new BigDecimal(registro.get("valorUnitario") == null ? "0" : registro.get("valorUnitario").toString());
				reg.setPatrimonioSemoventes(reg.getPatrimonioSemoventes().add(quantidade.multiply(valorUnitario)));
			}
		}

		// OUTROS PATRIMONIOS
		reg.setPatrimonioOutros(new BigDecimal("0"));
		List<Map<String, Object>> outroPatrimonioList = (List<Map<String, Object>>) patrimonioEDividas.get("outroPatrimonioList");
		if (!CollectionUtils.isEmpty(outroPatrimonioList)) {
			for (Map<String, Object> registro : outroPatrimonioList) {
				BigDecimal quantidade = new BigDecimal(registro.get("quantidade") == null ? "0" : registro.get("quantidade").toString());
				BigDecimal valorUnitario = new BigDecimal(registro.get("valorUnitario") == null ? "0" : registro.get("valorUnitario").toString());
				reg.setPatrimonioOutros(reg.getPatrimonioOutros().add(quantidade.multiply(valorUnitario)));
			}
		}

		// DIVIDAS EXISTENTES
		reg.setPatrimonioDividas(new BigDecimal("0"));
		List<Map<String, Object>> dividasList = (List<Map<String, Object>>) patrimonioEDividas.get("dividaExistenteList");
		if (!CollectionUtils.isEmpty(dividasList)) {
			for (Map<String, Object> registro : dividasList) {
				BigDecimal valorContratado = new BigDecimal(registro.get("valorContratado") == null ? "0" : registro.get("valorContratado").toString());
				reg.setPatrimonioDividas(reg.getPatrimonioDividas().add(valorContratado));
			}
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
		Map<String, Object> benfeitoriaListMap = (Map<String, Object>) valor.get("benfeitoriaList");

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

		if (!CollectionUtils.isEmpty(benfeitoriaListMap)) {
			// TODO calcular benfeitorias
		}
	}

	private byte[] ziparResultado(List<ProjetoCreditoRural> lista, List<byte[]> resultList) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		int i = 0;
		for (ProjetoCreditoRural projeto : lista) {
			ZipEntry entry = new ZipEntry(projeto.getAtividade().getCodigo().concat(".pdf"));
			byte[] arquivo = resultList.get(i++);
			entry.setSize(arquivo.length);
			zos.putNextEntry(entry);
			zos.write(arquivo);
			zos.closeEntry();
		}
		zos.close();
		return baos.toByteArray();
	}

}