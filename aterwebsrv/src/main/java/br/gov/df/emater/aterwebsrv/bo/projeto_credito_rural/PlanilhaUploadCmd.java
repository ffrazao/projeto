package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.ArquivoDao;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoCreditoRuralReceitaDespesaApoioDto;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioExcel;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FluxoCaixaTipo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
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

		List<Double> despesaList = new ArrayList<>();

		List<Double> receitaList = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			despesaList.add((Double) ((List<Object>) ((List<List<Object>>) arqExcel.get(1)).get(44)).get(5 + (i * 2)));
			receitaList.add((Double) ((List<Object>) ((List<List<Object>>) arqExcel.get(1)).get(63)).get(5 + (i * 2)));
		}

		// preparar retorno
		if (result.getProjetoCreditoRural() == null) {
			result.setProjetoCreditoRural(new ProjetoCreditoRural());
		}
		// criar listas caso não existam
		if (CollectionUtils.isEmpty(result.getProjetoCreditoRural().getDespesaList())) {
			result.getProjetoCreditoRural().setDespesaList(new ArrayList<>());
		}
		if (CollectionUtils.isEmpty(result.getProjetoCreditoRural().getReceitaList())) {
			result.getProjetoCreditoRural().setReceitaList(new ArrayList<>());
		}
		// remover os itens já marcados
		result.getProjetoCreditoRural().setDespesaList(result.getProjetoCreditoRural().getDespesaList().stream().filter(p -> !p.getCodigo().equals(result.getCodigo())).collect(Collectors.toList()));
		result.getProjetoCreditoRural().setReceitaList(result.getProjetoCreditoRural().getReceitaList().stream().filter(p -> !p.getCodigo().equals(result.getCodigo())).collect(Collectors.toList()));

		for (int i = 0; i < 10; i++) {
			ProjetoCreditoRuralReceitaDespesa r = new ProjetoCreditoRuralReceitaDespesa();
			r.setAno(i);
			r.setCodigo(result.getCodigo());
			r.setDescricao("Receita Evolução de Rebanho");
			r.setQuantidade(new BigDecimal("1"));
			r.setTipo(FluxoCaixaTipo.R);
			r.setUnidade("ub");
			r.setValorUnitario(new BigDecimal(receitaList.get(i)));
			result.getProjetoCreditoRural().getDespesaList().add(r);
		}

		for (int i = 0; i < 10; i++) {
			ProjetoCreditoRuralReceitaDespesa r = new ProjetoCreditoRuralReceitaDespesa();
			r.setAno(i);
			r.setCodigo(result.getCodigo());
			r.setDescricao("Despesa Evolução de Rebanho");
			r.setQuantidade(new BigDecimal("1"));
			r.setTipo(FluxoCaixaTipo.D);
			r.setUnidade("ub");
			r.setValorUnitario(new BigDecimal(despesaList.get(i)));
			result.getProjetoCreditoRural().getDespesaList().add(r);
		}

		contexto.setResposta(result);

		return false;
	}

}