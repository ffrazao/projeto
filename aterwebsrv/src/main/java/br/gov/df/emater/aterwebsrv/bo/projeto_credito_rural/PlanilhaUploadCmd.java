package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.ArquivoDao;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoCreditoRuralReceitaDespesaApoioDto;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioExcel;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FluxoCaixaTipo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralArquivo;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralReceitaDespesa;

@Service("ProjetoCreditoRuralPlanilhaUploadCmd")
public class PlanilhaUploadCmd extends _Comando {

	@Autowired
	private ArquivoDao arquivoDao;

	public PlanilhaUploadCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ProjetoCreditoRuralReceitaDespesaApoioDto result = (ProjetoCreditoRuralReceitaDespesaApoioDto) contexto.getRequisicao();

		Arquivo arquivo = arquivoDao.findByMd5(result.getArquivo().getMd5());

		List<List<List<Object>>> arqExcel = UtilitarioExcel.lerPlanilha(new ByteArrayInputStream(arquivo.getConteudo()));

		// preparar retorno
		if (result.getProjetoCreditoRural() == null) {
			result.setProjetoCreditoRural(new ProjetoCreditoRural());
		}
		String produto = StringUtils.capitalize(result.getCodigo().toString().substring("EVOLUCAO_REBANHO_".length()).toLowerCase());
		
		captarValores(result, "Despesa", produto, arqExcel, result.getCodigo().getLinhaDespesa());

		captarValores(result, "Receita", produto, arqExcel, result.getCodigo().getLinhaReceita());

		// ajustar o arquivo
		List<ProjetoCreditoRuralArquivo> arquivoList = result.getProjetoCreditoRural().getArquivoList();
		if (CollectionUtils.isEmpty(arquivoList)) {
			arquivoList = new ArrayList<>();
		}

		// identificar o arquivo
		List<ProjetoCreditoRuralArquivo> arquivoCodigoList = arquivoList.stream().filter(p -> p.getCodigo().equals(result.getCodigo())).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(arquivoCodigoList)) {
			arquivoList.add(new ProjetoCreditoRuralArquivo(null, arquivo.infoBasica(), result.getCodigo()));
		} else if (arquivoCodigoList.size() == 1) {
			arquivoCodigoList.get(0).setArquivo(arquivo.infoBasica());
		} else {
			throw new BoException("A relação de arquivos vinculados ao projeto de crédito está inconsistente, contate o Administrador");
		}
		result.getProjetoCreditoRural().setArquivoList(arquivoList);

		contexto.setResposta(result);

		return false;
	}

	@SuppressWarnings("unchecked")
	private void captarValores(ProjetoCreditoRuralReceitaDespesaApoioDto dto, String parte, String produto, List<List<List<Object>>> arqExcel, int linha) throws Exception {

		// captar os dados da planilha excel
		List<Double> excelValores = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			excelValores.add((Double) ((List<Object>) ((List<List<Object>>) arqExcel.get(1)).get(linha)).get(5 + (i * 2)));
		}

		// captar e, caso não existam, criar a lista de valores do resultado
		List<ProjetoCreditoRuralReceitaDespesa> lista = (List<ProjetoCreditoRuralReceitaDespesa>) MethodUtils.invokeMethod(dto.getProjetoCreditoRural(), String.format("get%sList", parte), null);
		if (CollectionUtils.isEmpty(lista)) {
			lista = new ArrayList<>();
		}
		// remover os itens já marcados
		int id = 0;
		if (lista.size() > 0) {
			// captar o menor id registrado
			ProjetoCreditoRuralReceitaDespesa temp = lista.stream().min(Comparator.comparing(i -> (i == null || i.getId() == null) ? 0 : i.getId())).get();
			if (temp != null && temp.getId() != null) {
				id = Math.min(id, temp.getId());
			}

			// remover os itens referentes ao codigo que esta sendo inserido
			lista = lista.stream().filter(p -> !dto.getCodigo().equals(p.getCodigo())).collect(Collectors.toList());
		}
		// inserir os itens captados
		for (int i = 0; i < 10; i++) {
			ProjetoCreditoRuralReceitaDespesa r = new ProjetoCreditoRuralReceitaDespesa();
			r.setId(--id);
			r.setAno(i + 1);
			r.setCodigo(dto.getCodigo());
			r.setDescricao(String.format("%s %s Evolução de Rebanho", parte, produto));
			r.setQuantidade(new BigDecimal("1"));
			r.setTipo(EnumUtils.getEnum(FluxoCaixaTipo.class, parte.substring(0, 1)));
			r.setUnidade("ub");
			r.setValorUnitario(new BigDecimal(excelValores.get(i)));
			lista.add(r);
		}
		// configurar a lista de valores do resultado
		MethodUtils.invokeMethod(dto.getProjetoCreditoRural(), String.format("set%sList", parte), lista);
	}

}