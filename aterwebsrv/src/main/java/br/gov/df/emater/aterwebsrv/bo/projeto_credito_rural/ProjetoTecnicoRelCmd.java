package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

			// Captar dados dos diagnósticos

			// beneficio social
			// captar diagnosticos
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
			_Contexto diagnosticoCtx;
			diagnosticoCtx = facadeBo.formularioColetaFiltroExecutar(contexto.getUsuario(), filtro);

			proponente.setDiagnosticoList((List<Object[]>) diagnosticoCtx.getResposta());

			// TODO Auto-generated method stub
			captarFormularios((List<Object[]>) proponente.getDiagnosticoList(), reg, "beneficioSocialForcaTrabalho", "patrimonioDivida");

			// captar dados das propriedades informadas no crédito
			List<PublicoAlvoPropriedadeRural> propriedadeList = null;
			if (!CollectionUtils.isEmpty(pcr.getPublicoAlvoPropriedadeRuralList())) {
				propriedadeList = new ArrayList<>();
				for (ProjetoCreditoRuralPublicoAlvoPropriedadeRural publicoAlvoPropriedadeRural : pcr.getPublicoAlvoPropriedadeRuralList()) {

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
					diagnosticoCtx = facadeBo.formularioColetaFiltroExecutar(contexto.getUsuario(), filtro);
					pr.setDiagnosticoList((List<Object[]>) diagnosticoCtx.getResposta());

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
	private void captarFormularios(List<Object[]> diagnosticoList, ProjetoTecnicoProponenteRelDto reg, String... propriedadeList) throws Exception {
		for (Object[] formulario : diagnosticoList) {
			for (FormularioVersao versao : (List<FormularioVersao>) formulario[8]) {
				for (String propriedade : propriedadeList) {
					if (propriedade.equals(formulario[2])) {
						FormularioVersao temp = (FormularioVersao) MethodUtils.invokeMethod(reg, "get".concat(StringUtils.capitalize(propriedade)).concat("Formulario"));
						if (temp == null || (Integer) versao.getVersao() > temp.getVersao()) {
							MethodUtils.invokeMethod(reg, "set".concat(StringUtils.capitalize(propriedade)).concat("Formulario"), versao);
						}
					}
				}
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