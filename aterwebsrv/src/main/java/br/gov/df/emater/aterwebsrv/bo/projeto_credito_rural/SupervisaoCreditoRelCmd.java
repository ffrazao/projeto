package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.SupervisaoCreditoDao;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.SupervisaoCreditoRelDto;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioPdf;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaRelacionamento;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralFinanciamento;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralPublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.SupervisaoCredito;
import br.gov.df.emater.aterwebsrv.relatorio._Relatorio;
import net.sf.jasperreports.engine.JasperPrint;

@Service("ProjetoCreditoRuralSupervisaoCreditoRelCmd")
public class SupervisaoCreditoRelCmd extends _Comando {

	@Autowired
	private SupervisaoCreditoDao dao;

	@Autowired
	private _Relatorio relatorio;

	public SupervisaoCreditoRelCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer[] idList = (Integer[]) contexto.getRequisicao();

		if (ArrayUtils.isEmpty(idList)) {
			throw new BoException("Nenhuma supervisão de crédito informada");
		}

		List<SupervisaoCredito> lista = dao.findAll(Arrays.asList(idList));

		if (CollectionUtils.isEmpty(lista)) {
			throw new BoException("Nenhuma supervisão de crédito encontrada com os parâmetros informados");
		}

		// gerar os relatorios
		List<byte[]> resultList = new ArrayList<>();
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("Usuario", getUsuario(contexto.getUsuario().getName()));
		
		List<SupervisaoCreditoRelDto> pList = new ArrayList<>();
		for (SupervisaoCredito supervisaoCredito : lista) {
			SupervisaoCreditoRelDto sc = new SupervisaoCreditoRelDto();

			// de supervisaoCredito para dto
			sc.setProponenteNome(supervisaoCredito.getProjetoCreditoRural().getPublicoAlvo().getPessoa().getNome());
			if (supervisaoCredito.getProjetoCreditoRural().getPublicoAlvo().getPessoa() instanceof PessoaFisica) {
				sc.setProponenteCpfCnpj(((PessoaFisica) supervisaoCredito.getProjetoCreditoRural().getPublicoAlvo().getPessoa()).getCpf());
			} else if (supervisaoCredito.getProjetoCreditoRural().getPublicoAlvo().getPessoa() instanceof PessoaJuridica) {				
				sc.setProponenteCpfCnpj(((PessoaJuridica) supervisaoCredito.getProjetoCreditoRural().getPublicoAlvo().getPessoa()).getCnpj());
			}
			List<String> nomeList = new ArrayList<>();
			UnidadeOrganizacional uo = null;
			for (ProjetoCreditoRuralPublicoAlvoPropriedadeRural papr: supervisaoCredito.getProjetoCreditoRural().getPublicoAlvoPropriedadeRuralList()) {
				nomeList.add(papr.getPublicoAlvoPropriedadeRural().getPropriedadeRural().getNome());
				uo = papr.getPublicoAlvoPropriedadeRural().getComunidade().getUnidadeOrganizacional();
			}
			sc.setPropriedadeNomeList(nomeList);
			sc.setNumeroCedula(supervisaoCredito.getProjetoCreditoRural().getNumeroCedula());
			sc.setAgenteFinanceiro(supervisaoCredito.getProjetoCreditoRural().getAgenteFinanceiro().getPessoaJuridica().getNome());
			sc.setAgencia(supervisaoCredito.getProjetoCreditoRural().getAgencia());
			sc.setLinhaCredito(supervisaoCredito.getProjetoCreditoRural().getLinhaCredito().getNome());
			sc.setUnidadeOrganizacional(uo.getNome());
			for (PessoaRelacionamento pr : supervisaoCredito.getEmprego().getPessoaRelacionamentoList()) {
				if ("Contratado".equalsIgnoreCase(pr.getRelacionamentoFuncao().getNomeSeMasculino())) {
					sc.setResponsavelTecnicoNome(pr.getPessoa().getNome());
				}
			}
			sc.setElaboracao(supervisaoCredito.getProjetoCreditoRural().getAtividade().getInicio());
//			sc.setContratacao(supervisaoCredito.getProjetoCreditoRural().getContratacao());
//			sc.setVencimento(supervisaoCredito.getProjetoCreditoRural().getVencimento());
			boolean custeio = false;
			BigDecimal custeioOrcado = new BigDecimal("0");
			BigDecimal custeioProprio = new BigDecimal("0");
			BigDecimal custeioFinanciado = new BigDecimal("0");
			if (supervisaoCredito.getProjetoCreditoRural().getCusteioList() != null) {
				for (ProjetoCreditoRuralFinanciamento item: supervisaoCredito.getProjetoCreditoRural().getCusteioList()) {
					custeio = true;
					custeioOrcado = custeioOrcado.add(item.getValorOrcado());
					custeioProprio = custeioProprio.add(item.getValorProprio());
					custeioFinanciado = custeioFinanciado.add(item.getValorFinanciado());
				}
			}
			sc.setCusteioOrcado(custeioOrcado);
			sc.setCusteioProprio(custeioProprio);
			sc.setCusteioFinanciado(custeioFinanciado);
			sc.setCusteioList(supervisaoCredito.getProjetoCreditoRural().getCusteioList());
			boolean investimento = false;
			BigDecimal investimentoOrcado = new BigDecimal("0");
			BigDecimal investimentoProprio = new BigDecimal("0");
			BigDecimal investimentoFinanciado = new BigDecimal("0");
			if (supervisaoCredito.getProjetoCreditoRural().getInvestimentoList() != null) {				
				for (ProjetoCreditoRuralFinanciamento item: supervisaoCredito.getProjetoCreditoRural().getInvestimentoList()) {
					investimento = true;
					investimentoOrcado = investimentoOrcado.add(item.getValorOrcado());
					investimentoProprio = investimentoProprio.add(item.getValorProprio());
					investimentoFinanciado = investimentoFinanciado.add(item.getValorFinanciado());
				}
			}
			sc.setInvestimentoOrcado(investimentoOrcado);
			sc.setInvestimentoProprio(investimentoProprio);
			sc.setInvestimentoFinanciado(investimentoFinanciado);
			sc.setInvestimentoList(supervisaoCredito.getProjetoCreditoRural().getInvestimentoList());
			List<String> finalidade = new ArrayList<>();
			if (custeio) {
				finalidade.add("Custeio");
			}
			if (investimento) {
				finalidade.add("Investimento");
			}
			sc.setFinalidade(UtilitarioString.collectionToString(finalidade));
			sc.setStatus(supervisaoCredito.getProjetoCreditoRural().getStatus().toString());
			sc.setNumeroSupervisao(supervisaoCredito.getOrdem());
			sc.setDataPrevista(supervisaoCredito.getDataPrevista());
			sc.setDataRealizacao(supervisaoCredito.getDataRealizacao());
			sc.setObservacao(supervisaoCredito.getObservacaoSituacao());
			sc.setRecomendacao(supervisaoCredito.getRecomendacao());
			sc.setLiberacao(supervisaoCredito.getLiberacaoPrevista());
			sc.setDataEmissao(Calendar.getInstance());			
			pList.add(sc);
		}
		List<JasperPrint> parteList = new ArrayList<>();

		// montar as partes do relatório
		montaParte("SUPERVISÃO DE CRÉDITO", "SupervisaoCredito", parametros, pList, parteList);

		// gerar o resultado
		resultList.add(UtilitarioPdf.juntarPdf(relatorio.imprimir(parteList.get(0))));

		// zipar o resultado
		byte[] result = ziparResultado(resultList);

		contexto.setResposta(result);

		return false;
	}

	private void montaParte(String relatorioNome, String nomeArquivo, Map<String, Object> parametros, List<?> pList, List<JasperPrint> parteList) throws BoException {
		if (pList == null || pList.size() == 0) {
			return;
		}
		parametros.put("RelatorioNome", relatorioNome);
		parametros.put("Parte", parteList.size() + 1);
		
		JasperPrint impressao = relatorio.montarRelatorio(String.format("supervisao_credito/%s", nomeArquivo), parametros, pList);
			
		parteList.add(impressao);
	}

	private byte[] ziparResultado(List<byte[]> resultList) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		ZipEntry entry;
		int i = 0;
		byte[] arquivo;
		
		entry = new ZipEntry("resultado.pdf");
		arquivo = resultList.get(i++);
		entry.setSize(arquivo.length);
		zos.putNextEntry(entry);
		zos.write(arquivo);
		zos.closeEntry();
		
		return baos.toByteArray();
	}

}